module HW5.Pretty
  ( prettyValue
  ) where

import HW5.Base
import Prettyprinter (Doc, pretty, (<+>))
import Prettyprinter.Render.Terminal (AnsiStyle)
import Data.Ratio
import Data.Text (unpack)
import qualified Data.Foldable as F
import qualified Data.Map as M
import qualified Data.ByteString as BS
import Data.Time.Format (formatTime, defaultTimeLocale)
import Numeric (showHex)
import Data.List (intercalate)
import Data.List (dropWhileEnd)
import qualified Data.Text as T
import Data.Char (toLower)

prettyValue :: HiValue -> Doc AnsiStyle
prettyValue val = case val of
  HiValueNull        -> pretty "null"
  HiValueBool True   -> pretty "true"
  HiValueBool False  -> pretty "false"
  HiValueFunction f  -> pretty (funName f)
  HiValueString txt  -> pretty (show (unpack txt))
  HiValueNumber r    -> prettyRational r
  HiValueList l      -> prettyList (F.toList l)
  HiValueBytes b     -> prettyBytes b
  HiValueAction a    -> prettyAction a
  HiValueTime t      -> pretty $ "parse-time(" ++ show (formatTime defaultTimeLocale "%F %T.%q UTC" t) ++ ")"
  HiValueDict d      -> prettyDict (M.toList d)
  where
    funName f = map toLower (drop 5 (show f)) 
    prettyRational r
      | denominator r == 1 = pretty (numerator r)
      | isFiniteDecimal r =
          let decimalValue = fromRational r :: Double
          in pretty (dropTrailingZeros (show decimalValue))
      | otherwise =
          let n = numerator r
              d = denominator r
              (w, rem') = quotRem n d
          in if w == 0
             then pretty (show (rem') ++ "/" ++ show d)
             else if rem' == 0
                  then pretty w
                  else pretty (show w ++ (if rem' > 0 then "+" else "-") ++ show (abs rem') ++ "/" ++ show d)

    -- prettyFraction w rem' d
    --   | w == 0 = pretty (showRatio rem' d)
    --   | rem' < 0 =
    --       if w /= 0
    --       then pretty (show w) <+> pretty "-" <+> pretty (showRatio (negate rem') d)
    --       else pretty "-" <+> pretty (showRatio (negate rem') d)
    --   | w > 0 =
    --       pretty (show w) <+> pretty "+" <+> pretty (showRatio rem' d)
    --   | w < 0 =
    --       pretty (show w) <+> pretty "-" <+> pretty (showRatio (negate rem') d)
    --   | otherwise = pretty (showRatio rem' d)
    --   where
    --     showRatio a b = show a ++ "/" ++ show b

    prettyList l = pretty "[" <+> mconcat (prettyElems l) <+> pretty "]"
      where
        prettyElems [] = mempty
        prettyElems [x] = [prettyValue x]
        prettyElems (x:xs) = [prettyValue x <> pretty "," <+> mconcat (prettyElems xs)]

    prettyBytes b =
      let hexes = BS.foldr (\x acc -> let hx = if x<16 then "0"++showHex x "" else showHex x "" in hx:acc) [] b
      in pretty "[#" <+> pretty (intercalate " " hexes) <+> pretty "#]"

    prettyAction :: HiAction -> Doc AnsiStyle
    prettyAction a = case a of
      HiActionRead path -> pretty "read(" <> pretty (show path) <> pretty ")"
      HiActionWrite path bytes -> pretty "write(" <> pretty (show path) <> pretty "," <+> prettyValue (HiValueBytes bytes) <> pretty ")"
      HiActionMkDir path -> pretty "mkdir(" <> pretty (show path) <> pretty ")"
      HiActionChDir path -> pretty "cd(" <> pretty (show path) <> pretty ")"
      HiActionCwd -> pretty "cwd"
      HiActionNow -> pretty "now"
      HiActionRand l r -> pretty "rand(" <> pretty l <> pretty "," <+> pretty r <> pretty ")"
      HiActionEcho txt -> pretty "echo(" <> pretty (show (T.unpack txt)) <> pretty ")"

    prettyDict d =
      let showPair (k, v) = prettyValue k <> pretty ":" <+> prettyValue v
          pairs = map showPair d
      in pretty "{" <+> mconcat (intersperseComma pairs) <+> pretty "}"

--------------------------------------------------------------------------------
-- mconcat для [Doc], чтобы легче было склеивать
intersperseComma :: [Doc AnsiStyle] -> [Doc AnsiStyle]
intersperseComma [] = []
intersperseComma (x:[]) = [x]
intersperseComma (x:xs) = x : (pretty "," <+> mconcat (intersperseComma xs)):[]

-- знаменатель представим только степенями 2 и 5
isFiniteDecimal :: Rational -> Bool
isFiniteDecimal r = let d = denominator (normalizeRational r)
                    in onlyFactors d [2,5]
  where
    onlyFactors 1 _ = True
    onlyFactors n fs = case filter (\f -> n `mod` f == 0) fs of
                        [] -> False
                        (f:_) -> onlyFactors (n `div` f) fs

    normalizeRational x = x 

dropTrailingZeros :: String -> String
dropTrailingZeros s =
  let (intPart, fracPart') = break (== '.') s
  in if null fracPart' then s
     else 
       let fracPart = dropWhileEnd (== '0') (tail fracPart')
       in if null fracPart then intPart else intPart ++ "." ++ fracPart