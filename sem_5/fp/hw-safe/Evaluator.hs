{-# LANGUAGE FlexibleContexts #-}
{-# LANGUAGE OverloadedStrings #-}
module HW5.Evaluator
  ( eval
  ) where

import HW5.Base
import Control.Monad.Except
import Control.Monad.Trans.Class (lift) 
import Data.Text (Text)
import qualified Data.Text as T
import Data.Sequence (Seq(..))
import qualified Data.Sequence as S
import Data.Map (Map)
import qualified Data.Map as M
import Data.Maybe
import Data.ByteString (ByteString)
import qualified Data.ByteString as BS
import Data.Semigroup (stimes)
import Data.Ratio
import qualified Data.Text.Encoding as TE
import qualified Codec.Compression.Zlib as Z
import Data.ByteString.Lazy (toStrict, fromStrict)
import Data.Time.Clock
import Data.Time.Format (parseTimeM, defaultTimeLocale)
import Data.Foldable (toList)
import Codec.Serialise (serialise, deserialiseOrFail)

eval :: HiMonad m => HiExpr -> m (Either HiError HiValue)
eval expr = runExceptT (evalExpr expr)

type EvalM m = ExceptT HiError m

evalExpr :: HiMonad m => HiExpr -> EvalM m HiValue
evalExpr (HiExprValue v) = return v
evalExpr (HiExprApply fe args) = do
  funcVal <- evalExpr fe
  case funcVal of
    HiValueFunction HiFunIf -> case args of
      [cond, thn, els] -> do
        c <- evalExpr cond
        case c of
          HiValueBool True -> evalExpr thn
          HiValueBool False -> evalExpr els
          _ -> throwError HiErrorInvalidArgument
      _ -> throwError HiErrorArityMismatch

    HiValueFunction HiFunAnd -> case args of
      [x,y] -> do
        xv <- evalExpr x
        if isFalsy xv then return xv else evalExpr y
      _ -> throwError HiErrorArityMismatch

    HiValueFunction HiFunOr -> case args of
      [x,y] -> do
        xv <- evalExpr x
        if isFalsy xv then evalExpr y else return xv
      _ -> throwError HiErrorArityMismatch

    -- Для остальных функций - evalArgsStrict
    HiValueFunction fun -> do
      argVals <- evalArgsStrict args
      evalFun fun argVals

    HiValueString s -> do
      argVals <- evalArgsStrict args
      applyToText s argVals
    HiValueList l -> do
      argVals <- evalArgsStrict args
      applyToList l argVals
    HiValueBytes b -> do
      argVals <- evalArgsStrict args
      applyToBytes b argVals
    HiValueDict d -> do
      argVals <- evalArgsStrict args
      applyToDict d argVals
    _ -> throwError HiErrorInvalidFunction
evalExpr (HiExprRun e) = do
  val <- evalExpr e
  case val of
    HiValueAction a -> doRun a
    _ -> throwError HiErrorInvalidArgument
evalExpr (HiExprDict pairs) = do
  kvs <- mapM (\(k,v) -> (,) <$> evalExpr k <*> evalExpr v) pairs
  return (HiValueDict (M.fromList kvs))

doRun :: HiMonad m => HiAction -> EvalM m HiValue
doRun a = do
  lift (runAction a)

-- Короткое замыкание для if, &&, ||
evalArgsStrict :: HiMonad m => [HiExpr] -> EvalM m [HiValue]
evalArgsStrict = mapM evalExpr

-- evalArgsStrictOrShort :: HiMonad m => [HiExpr] -> EvalM m [HiValue]
-- evalArgsStrictOrShort args = return ([]) >> mapM evalExpr args -- будет модифицироваться в логике ниже

evalFun :: HiMonad m => HiFun -> [HiValue] -> EvalM m HiValue
evalFun fun args = case fun of
  -- Арифметика
  HiFunAdd         -> binOp2 args add
  HiFunSub         -> binOp2 args sub
  HiFunMul         -> binOp2 args mul
  HiFunDiv         -> binOp2 args divide

  -- Логика
  HiFunNot         -> unary args notF
  HiFunAnd         -> evalAnd args
  HiFunOr          -> evalOr args

  -- Сравнения
  HiFunLessThan    -> binOp2 args (cmpF (<))
  HiFunGreaterThan -> binOp2 args (cmpF (>))
  HiFunEquals      -> binOp2 args (cmpEq (==))
  HiFunNotLessThan    -> binOp2 args cmpNotLessThan
  HiFunNotGreaterThan -> binOp2 args cmpNotGreaterThan
  HiFunNotEquals   -> binOp2 args (cmpEq (/=))

  -- if
  HiFunIf          -> evalIf args

  -- Task4
  HiFunLength      -> unary args lengthF
  HiFunToUpper     -> unary args toUpperF
  HiFunToLower     -> unary args toLowerF
  HiFunReverse     -> unary args reverseF
  HiFunTrim        -> unary args trimF

  -- Task5
  HiFunList        -> return $ HiValueList (S.fromList args)
  HiFunRange       -> binOp2 args rangeF
  HiFunFold        -> binOp2 args foldF

  -- Task6
  HiFunPackBytes   -> unary args packBytesF
  HiFunUnpackBytes -> unary args unpackBytesF
  HiFunEncodeUtf8  -> unary args encodeUtf8F
  HiFunDecodeUtf8  -> unary args decodeUtf8F
  HiFunZip         -> unary args zipF
  HiFunUnzip       -> unary args unzipF
  HiFunSerialise   -> unary args serialiseF
  HiFunDeserialise -> unary args deserialiseF

  -- Task7
  HiFunRead        -> unary args readF
  HiFunWrite       -> binOp2 args writeF
  HiFunMkDir       -> unary args mkdirF
  HiFunChDir       -> unary args chdirF

  -- Task8
  HiFunParseTime   -> unary args parseTimeF

  -- Task9
  HiFunRand        -> binOp2 args randF

  -- Task10
  HiFunEcho        -> unary args echoF

  -- Task11
  HiFunCount       -> unary args countF
  HiFunKeys        -> unary args keysF
  HiFunValues      -> unary args valuesF
  HiFunInvert      -> unary args invertF

--------------------------------------------------------------------------------
-- Короткое замыкание if,&&,||
evalIf :: HiMonad m => [HiValue] -> EvalM m HiValue
evalIf [HiValueBool c, a, b] = return (if c then a else b)
evalIf _ = throwError HiErrorArityMismatch

evalAnd :: HiMonad m => [HiValue] -> EvalM m HiValue
evalAnd [x, y] =
  if isFalsy x then return x else return y
evalAnd _ = throwError HiErrorArityMismatch

evalOr :: HiMonad m => [HiValue] -> EvalM m HiValue
evalOr [x, y] =
  if isFalsy x then return y else return x
evalOr _ = throwError HiErrorArityMismatch

isFalsy :: HiValue -> Bool
isFalsy (HiValueBool False) = True
isFalsy HiValueNull = True
isFalsy _ = False

-- Определение функций для обработки арности
unary :: HiMonad m => [HiValue] -> (HiValue -> EvalM m HiValue) -> EvalM m HiValue
unary [x] op = op x
unary _ _   = throwError HiErrorArityMismatch

binOp2 :: HiMonad m => [HiValue] -> (HiValue -> HiValue -> EvalM m HiValue) -> EvalM m HiValue
binOp2 [x, y] op = op x y
binOp2 _ _ = throwError HiErrorArityMismatch

-- Арифметика, строки, списки, байты
add :: HiMonad m => HiValue -> HiValue -> EvalM m HiValue
add (HiValueNumber x) (HiValueNumber y) = return $ HiValueNumber (x + y)
add (HiValueString s1) (HiValueString s2) = return $ HiValueString (T.append s1 s2)
add (HiValueList l1) (HiValueList l2) = return $ HiValueList (l1 <> l2)
add (HiValueBytes b1) (HiValueBytes b2) = return $ HiValueBytes (BS.append b1 b2)
add _ _ = throwError HiErrorInvalidArgument

sub :: HiMonad m => HiValue -> HiValue -> EvalM m HiValue
sub (HiValueNumber x) (HiValueNumber y) = return $ HiValueNumber (x - y)
sub (HiValueTime t1) (HiValueTime t2) = return $ HiValueNumber (toRational (diffUTCTime t1 t2))
sub _ _ = throwError HiErrorInvalidArgument

mul :: HiMonad m => HiValue -> HiValue -> EvalM m HiValue
mul (HiValueNumber x) (HiValueNumber y) = return $ HiValueNumber (x * y)
mul (HiValueString s) (HiValueNumber n) = repString s n
mul (HiValueList l) (HiValueNumber n) = repList l n
mul (HiValueBytes b) (HiValueNumber n) = repBytes b n
mul (HiValueNumber n) (HiValueString s) = repString s n
mul (HiValueNumber n) (HiValueList l) = repList l n
mul (HiValueNumber n) (HiValueBytes b) = repBytes b n
mul _ _ = throwError HiErrorInvalidArgument

repString :: HiMonad m => Text -> Rational -> EvalM m HiValue
repString s r = do
  n <- toNonNegInt r
  return (HiValueString (stimes n s))

repList :: HiMonad m => Seq HiValue -> Rational -> EvalM m HiValue
repList l r = do
  n <- toNonNegInt r
  return (HiValueList (stimes n l))

repBytes :: HiMonad m => ByteString -> Rational -> EvalM m HiValue
repBytes b r = do
  n <- toNonNegInt r
  return $ HiValueBytes (BS.concat (replicate n b))

divide :: HiMonad m => HiValue -> HiValue -> EvalM m HiValue
divide (HiValueNumber _) (HiValueNumber 0) = throwError HiErrorDivideByZero
divide (HiValueNumber x) (HiValueNumber y) = return $ HiValueNumber (x / y)
divide (HiValueString s1) (HiValueString s2) = return $ HiValueString (s1 <> "/" <> s2)
divide _ _ = throwError HiErrorInvalidArgument

toNonNegInt :: HiMonad m => Rational -> EvalM m Int
toNonNegInt r =
  if denominator r == 1 && numerator r >= 0
    then return (fromInteger (numerator r))
    else throwError HiErrorInvalidArgument

--------------------------------------------------------------------------------
-- Логика
notF :: HiMonad m => HiValue -> EvalM m HiValue
notF (HiValueBool b) = return $ HiValueBool (not b)
notF _ = throwError HiErrorInvalidArgument

--------------------------------------------------------------------------------
-- Сравнение
cmpF :: HiMonad m => (HiValue -> HiValue -> Bool) -> HiValue -> HiValue -> EvalM m HiValue
cmpF cmp x y = return $ HiValueBool (cmp x y)

cmpEq :: HiMonad m => (HiValue -> HiValue -> Bool) -> HiValue -> HiValue -> EvalM m HiValue
cmpEq eq x y = return $ HiValueBool (eq x y)

cmpNotLessThan :: HiMonad m => HiValue -> HiValue -> EvalM m HiValue
cmpNotLessThan x y = do
  result <- cmpF (<) x y
  case result of
    HiValueBool res -> return (HiValueBool (not res))
    _ -> throwError HiErrorInvalidArgument

cmpNotGreaterThan :: HiMonad m => HiValue -> HiValue -> EvalM m HiValue
cmpNotGreaterThan x y = do
  res <- cmpF (>) x y
  case res of
    HiValueBool b -> return (HiValueBool (not b))
    _             -> throwError HiErrorInvalidArgument

--------------------------------------------------------------------------------
-- Task4: String functions
lengthF :: HiMonad m => HiValue -> EvalM m HiValue
lengthF (HiValueString s) = return (HiValueNumber (toRational (T.length s)))
lengthF (HiValueList l)   = return (HiValueNumber (toRational (S.length l)))
lengthF (HiValueBytes b)  = return (HiValueNumber (toRational (BS.length b)))
lengthF _ = throwError HiErrorInvalidArgument

toUpperF :: HiMonad m => HiValue -> EvalM m HiValue
toUpperF (HiValueString s) = return (HiValueString (T.toUpper s))
toUpperF _ = throwError HiErrorInvalidArgument

toLowerF :: HiMonad m => HiValue -> EvalM m HiValue
toLowerF (HiValueString s) = return (HiValueString (T.toLower s))
toLowerF _ = throwError HiErrorInvalidArgument

reverseF :: HiMonad m => HiValue -> EvalM m HiValue
reverseF (HiValueString s) = return (HiValueString (T.reverse s))
reverseF (HiValueList l) = return (HiValueList (S.reverse l))
reverseF (HiValueBytes b) = return (HiValueBytes (BS.reverse b))
reverseF _ = throwError HiErrorInvalidArgument

trimF :: HiMonad m => HiValue -> EvalM m HiValue
trimF (HiValueString s) = return (HiValueString (T.strip s))
trimF _ = throwError HiErrorInvalidArgument

--------------------------------------------------------------------------------
-- Indexing/slicing for strings, lists, bytes
applyToText :: HiMonad m => Text -> [HiValue] -> EvalM m HiValue
applyToText s [] = return (HiValueString s)
applyToText s [HiValueNumber n] =
  case idx s n of
    Just c -> return (HiValueString (T.singleton c))
    Nothing -> return HiValueNull
  where
    idx txt r = do
      i <- rationalToIndex r (T.length txt)
      if i >= 0 && i < T.length txt then Just (T.index txt i) else Nothing
applyToText s [start, end] = sliceText s start end
applyToText _ _ = throwError HiErrorArityMismatch

sliceText :: HiMonad m => Text -> HiValue -> HiValue -> EvalM m HiValue
sliceText s start end = do
  st <- toIndexMaybe (T.length s) start
  en <- toIndexMaybe (T.length s) end
  let st' = fromMaybe 0 st
      en' = fromMaybe (T.length s) en
  return (HiValueString (takeSlice s st' en'))
  where
    takeSlice txt a b = if b < a then T.empty else T.take (b - a) (T.drop a txt)

applyToList :: HiMonad m => Seq HiValue -> [HiValue] -> EvalM m HiValue
applyToList l [] = return (HiValueList l)
applyToList l [HiValueNumber n] =
  case idx l n of
    Just v -> return v
    Nothing -> return HiValueNull
  where
    idx lst r = do
      i <- rationalToIndex r (S.length lst)
      if i >= 0 && i < S.length lst then Just (S.index lst i) else Nothing
applyToList l [start, end] = sliceList l start end
applyToList _ _ = throwError HiErrorArityMismatch

sliceList :: HiMonad m => Seq HiValue -> HiValue -> HiValue -> EvalM m HiValue
sliceList l start end = do
  st <- toIndexMaybe (S.length l) start
  en <- toIndexMaybe (S.length l) end
  let st' = fromMaybe 0 st
      en' = fromMaybe (S.length l) en
  return (HiValueList (takeSlice l st' en'))
  where
    takeSlice lst a b = if b < a then S.empty else S.take (b - a) (S.drop a lst)

applyToBytes :: HiMonad m => ByteString -> [HiValue] -> EvalM m HiValue
applyToBytes b [] = return (HiValueBytes b)
applyToBytes b [HiValueNumber n] = do
  let maybeByte = idx b n
  case maybeByte of
    Just byte -> return $ HiValueNumber (toRational byte)
    Nothing -> return HiValueNull
  where
    idx by r = do
      i <- rationalToIndex r (BS.length by)
      if i >= 0 && i < BS.length by then Just (by `BS.index` i) else Nothing
applyToBytes b [start, end] = sliceBytes b start end
applyToBytes _ _ = throwError HiErrorArityMismatch

sliceBytes :: HiMonad m => ByteString -> HiValue -> HiValue -> EvalM m HiValue
sliceBytes b start end = do
  st <- toIndexMaybe (BS.length b) start
  en <- toIndexMaybe (BS.length b) end
  let st' = fromMaybe 0 st
      en' = fromMaybe (BS.length b) en
  if en' < st' then return (HiValueBytes BS.empty)
  else return (HiValueBytes (BS.take (en' - st') (BS.drop st' b)))

applyToDict :: HiMonad m => Map HiValue HiValue -> [HiValue] -> EvalM m HiValue
applyToDict d [k] = return (M.findWithDefault HiValueNull k d)
applyToDict _ _ = throwError HiErrorArityMismatch

rationalToIndex :: Rational -> Int -> Maybe Int
rationalToIndex r len = if denominator r == 1
                          then let i = fromInteger (numerator r) in Just (if i < 0 then len + i else i)
                          else Nothing

toIndexMaybe :: HiMonad m => Int -> HiValue -> EvalM m (Maybe Int)
toIndexMaybe _ HiValueNull = return Nothing
toIndexMaybe len (HiValueNumber n) = return (fmap (\x -> if x < 0 then len + x else x) (toIndex n))
  where
    toIndex rr 
      | denominator rr == 1 = Just (fromInteger (numerator rr))
      | otherwise = Nothing
toIndexMaybe _ _ = throwError HiErrorInvalidArgument

--------------------------------------------------------------------------------
-- Task5: range, fold
rangeF :: HiMonad m => HiValue -> HiValue -> EvalM m HiValue
rangeF (HiValueNumber from) (HiValueNumber to) =
  let step = if from <= to then 1 else -1
      go x | step > 0 && x > to = []
           | step < 0 && x < to = []
           | otherwise = x : go (x + step)
  in return (HiValueList (S.fromList (map HiValueNumber (go (fromRational from)))))
rangeF _ _ = throwError HiErrorInvalidArgument

foldF :: HiMonad m => HiValue -> HiValue -> EvalM m HiValue
foldF (HiValueFunction f) (HiValueList lst) = foldLeft f (toList lst)
foldF _ _ = throwError HiErrorInvalidArgument

foldLeft :: HiMonad m => HiFun -> [HiValue] -> EvalM m HiValue
foldLeft _ [] = return HiValueNull
foldLeft _ [x] = return x
foldLeft f (x:y:xs) = do
  res <- evalFun f [x, y]
  foldLeft f (res:xs)

--------------------------------------------------------------------------------
-- Task6: bytes and serialisation
packBytesF :: HiMonad m => HiValue -> EvalM m HiValue
packBytesF (HiValueList lst) = do
  bytes <- mapM toByte (toList lst)
  return (HiValueBytes (BS.pack bytes))
  where
    toByte (HiValueNumber n) =
      if denominator n == 1 && numerator n >= 0 && numerator n <= 255
        then return (fromInteger (numerator n))
        else throwError HiErrorInvalidArgument
    toByte _ = throwError HiErrorInvalidArgument
packBytesF _ = throwError HiErrorInvalidArgument

unpackBytesF :: HiMonad m => HiValue -> EvalM m HiValue
unpackBytesF (HiValueBytes b) = return (HiValueList (S.fromList (map (HiValueNumber . toRational . toInteger) (BS.unpack b))))
unpackBytesF _ = throwError HiErrorInvalidArgument

encodeUtf8F :: HiMonad m => HiValue -> EvalM m HiValue
encodeUtf8F (HiValueString s) = return (HiValueBytes (TE.encodeUtf8 s))
encodeUtf8F _ = throwError HiErrorInvalidArgument

decodeUtf8F :: HiMonad m => HiValue -> EvalM m HiValue
decodeUtf8F (HiValueBytes b) = case TE.decodeUtf8' b of
  Left _ -> return HiValueNull
  Right txt -> return (HiValueString txt)
decodeUtf8F _ = throwError HiErrorInvalidArgument

zipF :: HiMonad m => HiValue -> EvalM m HiValue
zipF (HiValueBytes b) = return (HiValueBytes (toStrict (Z.compress (fromStrict b))))
zipF _ = throwError HiErrorInvalidArgument

unzipF :: HiMonad m => HiValue -> EvalM m HiValue
unzipF (HiValueBytes b) = return (HiValueBytes (toStrict (Z.decompress (fromStrict b))))
unzipF _ = throwError HiErrorInvalidArgument

serialiseF :: HiMonad m => HiValue -> EvalM m HiValue
serialiseF v = return (HiValueBytes (toStrict (serialise v)))

deserialiseF :: HiMonad m => HiValue -> EvalM m HiValue
deserialiseF (HiValueBytes b) = case deserialiseOrFail (fromStrict b) of
  Left _ -> return HiValueNull
  Right v -> return v
deserialiseF _ = throwError HiErrorInvalidArgument

--------------------------------------------------------------------------------
-- Task7: read/write/mkdir/cd
readF :: HiMonad m => HiValue -> EvalM m HiValue
readF (HiValueString s) = return (HiValueAction (HiActionRead (T.unpack s)))
readF _ = throwError HiErrorInvalidArgument

writeF :: HiMonad m => HiValue -> HiValue -> EvalM m HiValue
writeF (HiValueString path) (HiValueString s) = return (HiValueAction (HiActionWrite (T.unpack path) (TE.encodeUtf8 s)))
writeF (HiValueString path) (HiValueBytes b) = return (HiValueAction (HiActionWrite (T.unpack path) b))
writeF _ _ = throwError HiErrorInvalidArgument

mkdirF :: HiMonad m => HiValue -> EvalM m HiValue
mkdirF (HiValueString s) = return (HiValueAction (HiActionMkDir (T.unpack s)))
mkdirF _ = throwError HiErrorInvalidArgument

chdirF :: HiMonad m => HiValue -> EvalM m HiValue
chdirF (HiValueString s) = return (HiValueAction (HiActionChDir (T.unpack s)))
chdirF _ = throwError HiErrorInvalidArgument

--------------------------------------------------------------------------------
-- Task8: parse-time, now
parseTimeF :: HiMonad m => HiValue -> EvalM m HiValue
parseTimeF (HiValueString s) =
  case parseTimeM True defaultTimeLocale "%F %T%Q UTC" (T.unpack s) of
    Just t -> return (HiValueTime t)
    Nothing -> return HiValueNull
parseTimeF _ = throwError HiErrorInvalidArgument

--------------------------------------------------------------------------------
-- Task9: rand
randF :: HiMonad m => HiValue -> HiValue -> EvalM m HiValue
randF (HiValueNumber from) (HiValueNumber to) = do
  f <- intVal from
  t <- intVal to
  return (HiValueAction (HiActionRand f t))
randF _ _ = throwError HiErrorInvalidArgument

intVal :: HiMonad m => Rational -> EvalM m Int
intVal r = 
  if denominator r == 1
    then return (fromInteger (numerator r))
    else throwError HiErrorInvalidArgument

--------------------------------------------------------------------------------
-- Task10: echo
echoF :: HiMonad m => HiValue -> EvalM m HiValue
echoF (HiValueString s) = return (HiValueAction (HiActionEcho s))
echoF _ = throwError HiErrorInvalidArgument

--------------------------------------------------------------------------------
-- Task11: count, keys, values, invert
countF :: HiMonad m => HiValue -> EvalM m HiValue
countF (HiValueString s) =
  return (HiValueDict (freq (map (HiValueString . T.singleton) (T.unpack s))))

countF (HiValueList l) =
  return (HiValueDict (freq (toList l)))

countF (HiValueBytes b) =
  return (HiValueDict (freq (map (HiValueNumber . toRational . toInteger) (BS.unpack b))))
countF _ = throwError HiErrorInvalidArgument

freq :: Ord a => [a] -> Map a HiValue
freq xs = M.fromListWith combine (map (\x -> (x, HiValueNumber 1)) xs)
  where
    combine (HiValueNumber x) (HiValueNumber y) = HiValueNumber (x + y)
    combine _ _ = HiValueNumber 0  -- Или другая логика по вашему усмотрению

keysF :: HiMonad m => HiValue -> EvalM m HiValue
keysF (HiValueDict d) =
  return (HiValueList (S.fromList (map fst (M.toList d))))
keysF _ = throwError HiErrorInvalidArgument

valuesF :: HiMonad m => HiValue -> EvalM m HiValue
valuesF (HiValueDict d) =
  return (HiValueList (S.fromList (map snd (M.toList d))))
valuesF _ = throwError HiErrorInvalidArgument

invertF :: HiMonad m => HiValue -> EvalM m HiValue
invertF (HiValueDict d) = return (HiValueDict (invertDict d))
invertF _ = throwError HiErrorInvalidArgument

invertDict :: Map HiValue HiValue -> Map HiValue HiValue
invertDict d =
  let kvs = M.toList d
      rev = M.fromListWith combine $
              map (\(k, v) -> (v, HiValueList (S.singleton k))) kvs
      combine (HiValueList l1) (HiValueList l2) = HiValueList (S.sort (l1 <> l2))
      combine _ _ = HiValueList S.empty  -- Или другая логика по вашему усмотрению
  in M.map (\v -> case v of
                   HiValueList l -> HiValueList (S.sort l)
                   _ -> HiValueList S.empty) rev