{-# LANGUAGE OverloadedStrings #-}

module HW5.Parser
  ( parse
  ) where

import Data.Void (Void)
import Text.Megaparsec hiding (parse)
import Text.Megaparsec.Char
import qualified Text.Megaparsec.Char.Lexer as L
import Control.Monad (void)
import Data.Scientific (toRealFloat)
import HW5.Base
import Control.Monad.Combinators.Expr
import qualified Data.Text as T
import qualified Data.ByteString as BS
import Data.Word (Word8)
import Numeric (readHex)
import Data.Char (isHexDigit)
import qualified Data.Set as Set

type Parser = Parsec Void String

parse :: String -> Either (ParseErrorBundle String Void) HiExpr
parse = runParser (pToplevel <* eof) ""

sc :: Parser ()
sc = L.space space1 (L.skipLineComment "//") (L.skipBlockComment "/*" "*/")

lexeme :: Parser a -> Parser a
lexeme = L.lexeme sc

symbol :: String -> Parser String
symbol = L.symbol sc

pToplevel :: Parser HiExpr
pToplevel = do
  expr <- pExpr
  runExpr <- optional (symbol "!" >> return ())
  case runExpr of
    Just _ -> return (HiExprRun expr)
    Nothing -> return expr

pParens :: Parser a -> Parser a
pParens = between (symbol "(") (symbol ")")

pBrackets :: Parser a -> Parser a
pBrackets = between (symbol "[") (symbol "]")

pBraces :: Parser a -> Parser a
pBraces = between (symbol "{") (symbol "}")

pCommaSep :: Parser a -> Parser [a]
pCommaSep p = p `sepBy` symbol ","

-- pCommaSep1 :: Parser a -> Parser [a]
-- pCommaSep1 p = p `sepBy1` symbol ","

pStringLiteral :: Parser HiValue
pStringLiteral = do
  str <- lexeme (char '"' >> manyTill L.charLiteral (char '"'))
  return (HiValueString (T.pack str))

pNull :: Parser HiValue
pNull = HiValueNull <$ lexeme (string "null")

pBool :: Parser HiValue
pBool = (HiValueBool True <$ lexeme (string "true"))
    <|> (HiValueBool False <$ lexeme (string "false"))

pNumber :: Parser HiValue
pNumber = do
  sign <- optional (char '-')
  num <- lexeme L.scientific
  let val = toRational (toRealFloat num :: Double)
  return $ HiValueNumber (case sign of
                             Just _ -> -val
                             Nothing -> val)

pFunIdent :: Parser HiValue
pFunIdent = lexeme $ choice
  [ HiValueFunction HiFunDiv         <$ string "div"
  , HiValueFunction HiFunMul         <$ string "mul"
  , HiValueFunction HiFunAdd         <$ string "add"
  , HiValueFunction HiFunSub         <$ string "sub"
  , HiValueFunction HiFunNot         <$ string "not"
  , HiValueFunction HiFunAnd         <$ string "and"
  , HiValueFunction HiFunOr          <$ string "or"
  , HiValueFunction HiFunLessThan    <$ string "less-than"
  , HiValueFunction HiFunGreaterThan <$ string "greater-than"
  , HiValueFunction HiFunEquals      <$ string "equals"
  , HiValueFunction HiFunNotLessThan <$ string "not-less-than"
  , HiValueFunction HiFunNotGreaterThan <$ string "not-greater-than"
  , HiValueFunction HiFunNotEquals   <$ string "not-equals"
  , HiValueFunction HiFunIf          <$ string "if"
  -- Task4
  , HiValueFunction HiFunLength      <$ string "length"
  , HiValueFunction HiFunToUpper     <$ string "to-upper"
  , HiValueFunction HiFunToLower     <$ string "to-lower"
  , HiValueFunction HiFunReverse     <$ string "reverse"
  , HiValueFunction HiFunTrim        <$ string "trim"
  -- Task5
  , HiValueFunction HiFunList        <$ string "list"
  , HiValueFunction HiFunRange       <$ string "range"
  , HiValueFunction HiFunFold        <$ string "fold"
  -- Task6
  , HiValueFunction HiFunPackBytes   <$ string "pack-bytes"
  , HiValueFunction HiFunUnpackBytes <$ string "unpack-bytes"
  , HiValueFunction HiFunEncodeUtf8  <$ string "encode-utf8"
  , HiValueFunction HiFunDecodeUtf8  <$ string "decode-utf8"
  , HiValueFunction HiFunZip         <$ string "zip"
  , HiValueFunction HiFunUnzip       <$ string "unzip"
  , HiValueFunction HiFunSerialise   <$ string "serialise"
  , HiValueFunction HiFunDeserialise <$ string "deserialise"
  -- Task7
  , HiValueFunction HiFunRead        <$ string "read"
  , HiValueFunction HiFunWrite       <$ string "write"
  , HiValueFunction HiFunMkDir       <$ string "mkdir"
  , HiValueFunction HiFunChDir       <$ string "cd"
  -- Task8
  , HiValueFunction HiFunParseTime   <$ string "parse-time"
  -- Task9
  , HiValueFunction HiFunRand        <$ string "rand"
  -- Task10
  , HiValueFunction HiFunEcho        <$ string "echo"
  -- Task11
  , HiValueFunction HiFunCount       <$ string "count"
  , HiValueFunction HiFunKeys        <$ string "keys"
  , HiValueFunction HiFunValues      <$ string "values"
  , HiValueFunction HiFunInvert      <$ string "invert"
  ]

pActionValue :: Parser HiValue
pActionValue =
      (HiValueAction HiActionCwd <$ lexeme (string "cwd"))
  <|> (HiValueAction HiActionNow <$ lexeme (string "now"))

pListLiteral :: Parser HiExpr
pListLiteral = do
  elems <- pBrackets (pCommaSep pExpr)
  return $ HiExprApply (HiExprValue (HiValueFunction HiFunList)) elems

pByteArray :: Parser HiValue
pByteArray = do
  _ <- symbol "[#"
  bytes <- manyTill (lexeme pHexByte) (try (symbol "#]"))
  return $ HiValueBytes (BS.pack bytes)

pHexByte :: Parser Word8
pHexByte = do
  h1 <- satisfy isHexDigit
  h2 <- satisfy isHexDigit
  case readHex [h1, h2] of
    [(n, "")] -> return (fromInteger n)
    _         -> fail "Invalid hexadecimal byte"

pDict :: Parser HiExpr
pDict = do
  pairs <- pBraces (pCommaSep (do
                                k <- pExpr
                                _ <- symbol ":"
                                v <- pExpr
                                return (k,v)))
  return (HiExprDict pairs)

pValueExpr :: Parser HiExpr
pValueExpr = choice
  [ HiExprValue <$> pNull
  , HiExprValue <$> pBool
  , HiExprValue <$> pStringLiteral
  , HiExprValue <$> pNumber
  , HiExprValue <$> pFunIdent
  , HiExprValue <$> pActionValue
  , HiExprValue <$> pByteArray
  , pListLiteral
  , pDict
  , pParens pExpr
  ]

pDotAccess :: HiExpr -> Parser HiExpr
pDotAccess e = do
  void (char '.')
  fld <- lexeme (some (alphaNumChar <|> satisfy (`Set.member` Set.fromList "_-")))
  let fieldStr = HiExprValue (HiValueString (T.pack fld))
  return (HiExprApply e [fieldStr])

pFunCall :: Parser HiExpr
pFunCall = do
  f <- pAtomicExpr
  argsOrDotOrRun f

argsOrDotOrRun :: HiExpr -> Parser HiExpr
argsOrDotOrRun expr =
  (do args <- pParens (pCommaSep pExpr)
      argsOrDotOrRun (HiExprApply expr args))
  <|> (try (pDotAccess expr) >>= argsOrDotOrRun)
  <|> (symbol "!" >> argsOrDotOrRun (HiExprRun expr))
  <|> return expr

pAtomicExpr :: Parser HiExpr
pAtomicExpr = pValueExpr

pExpr :: Parser HiExpr
pExpr = makeExprParser pFunCall table

table :: [[Operator Parser HiExpr]]
table =
  [ [ prefix "-" negExpr
    ]
  , [ binary "/" HiFunDiv
    , binary "*" HiFunMul
    ]
  , [ binary "+" HiFunAdd
    , binary "-" HiFunSub
    ]
  , [ binary "<"  HiFunLessThan
    , binary ">"  HiFunGreaterThan
    , binary ">=" HiFunNotLessThan
    , binary "<=" HiFunNotGreaterThan
    , binary "==" HiFunEquals
    , binary "/=" HiFunNotEquals
    ]
  , [ binary "&&" HiFunAnd
    ]
  , [ binary "||" HiFunOr
    ]
  ]
  where
    negExpr x = HiExprApply (HiExprValue (HiValueFunction HiFunSub)) [HiExprValue (HiValueNumber 0), x]

binary :: String -> HiFun -> Operator Parser HiExpr
binary name fun = InfixL (do
  void $ symbol name
  return (\x y -> HiExprApply (HiExprValue (HiValueFunction fun)) [x,y]))

prefix :: String -> (HiExpr -> HiExpr) -> Operator Parser HiExpr
prefix name f = Prefix (f <$ symbol name)