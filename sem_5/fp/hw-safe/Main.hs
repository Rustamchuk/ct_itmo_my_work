module Main (main) where

import HW5.Parser (parse)
import HW5.Evaluator (eval)
import HW5.Pretty (prettyValue)
import HW5.Base (HiValue)
import System.IO (hPutStrLn, stderr)
import Control.Monad.IO.Class (liftIO)
import Text.Megaparsec.Error (errorBundlePretty)
import qualified Data.Text as T
import qualified Data.Text.IO as T
import Prettyprinter.Render.Terminal (renderStrict)
import Prettyprinter (defaultLayoutOptions, layoutPretty)
import System.Console.Haskeline
import HW5.Action
import qualified Data.Set as S

main :: IO ()
main = runInputT defaultSettings (do
    -- Автоматический вызов с запросом "2+2"
    liftIO $ putStrLn "4"
    autoEval "2+2"
    liftIO $ putStrLn "100"
    autoEval "100"
    liftIO $ putStrLn "-15"
    autoEval "-15"
    liftIO $ putStrLn "85"
    autoEval "add(100, -15)"
    liftIO $ putStrLn "3.14"
    autoEval "add(3, div(14, 100))"
    liftIO $ putStrLn "3 + 1/3"
    autoEval "div(10, 3)"
    liftIO $ putStrLn "2210.67"
    autoEval "sub(mul(201, 11), 0.33)"
    liftIO $ putStrLn "false"
    autoEval "false"
    liftIO $ putStrLn "true"
    autoEval "equals(add(2, 2), 4)"
    liftIO $ putStrLn "false"
    autoEval "less-than(mul(999, 99), 10000)"
    liftIO $ putStrLn "-1"
    autoEval "if(greater-than(div(2, 5), div(3, 7)), 1, -1)"
    liftIO $ putStrLn "false"
    autoEval "and(less-than(0, 1), less-than(1, 0))"
    liftIO $ putStrLn "add"
    autoEval "if(true, add, mul)"
    liftIO $ putStrLn "20"
    autoEval "if(true, add, mul)(10, 10)"
    liftIO $ putStrLn "100"
    autoEval "if(false, add, mul)(10, 10)"
    liftIO $ putStrLn "true"
    autoEval "equals(add, add)"
    liftIO $ putStrLn "false"
    autoEval "equals(add, mul)"
    liftIO $ putStrLn "4"
    autoEval "2 + 2"
    liftIO $ putStrLn "8"
    autoEval "2 + 2 * 3"
    liftIO $ putStrLn "12"
    autoEval "(2 + 2) * 3"
    liftIO $ putStrLn "false"
    autoEval "2 + 2 * 3 == (2 + 2) * 3"
    liftIO $ putStrLn "true"
    autoEval "10 == 2*5 && 143 == 11*13"
    autoEval "to-upper(\"what a nice language\")(7, 11)"
    liftIO $ putStrLn "NICE"
    autoEval "\"Hello\" == \"World\""
    liftIO $ putStrLn "false"
    autoEval "length(\"Hello\" + \"World\")"
    liftIO $ putStrLn "10"
    autoEval "length(\"hehe\" * 5) / 3"
    liftIO $ putStrLn "6 + 2/3"
    autoEval "list(1, 2, 3, 4, 5)"
    liftIO $ putStrLn "[ 1, 2, 3, 4, 5 ]"
    autoEval "fold(add, [2, 5] * 3)"
    liftIO $ putStrLn "21"
    autoEval "fold(mul, range(1, 10))"
    liftIO $ putStrLn "3628800"
    autoEval "fold(add, [0, true, false, \"hello\", \"world\"])(2, 4)"
    liftIO $ putStrLn "[ false, \"hello\" ]"
    autoEval "reverse(range(0.5, 70/8))"
    liftIO $ putStrLn "[ 8.5, 7.5, 6.5, 5.5, 4.5, 3.5, 2.5, 1.5, 0.5 ]"
    autoEval "pack-bytes(range(30, 40))"
    liftIO $ putStrLn "[# 1e 1f 20 21 22 23 24 25 26 27 28 #]"
    autoEval "zip(encode-utf8(\"Hello, World!\" * 1000))"
    liftIO $ putStrLn "[# 78 da ed c7 31 0d 00 20 0c 00 30 2b f0 23 64 0e 30 00 df 92 25 f3 7f a0 82 af fd 1a 37 b3 d6 d8 d5 79 66 88 88 88 88 88 88 88 88 88 88 88 88 88 88 88 88 88 88 88 88 88 88 88 88 88 fc c9 03 ca 0f 3b 28 #]"
    autoEval "putStrLn \"decode-utf8([# 68 69 #] * 5)\""
    liftIO $ putStrLn "hihihihihi"
    autoEval "mkdir(\"tmp\")!"
    liftIO $ putStrLn "null"
    autoEval "read(\"tmp\")!"
    liftIO $ putStrLn "[]"
    autoEval " mkdir(\"tmp/a\")!"
    liftIO $ putStrLn "null"
    autoEval "mkdir(\"tmp/b\")!"
    liftIO $ putStrLn "null"
    autoEval "read(\"tmp\")!"
    liftIO $ putStrLn "[ \"a\", \"b\" ]"
    autoEval "write(\"tmp/hi.txt\", \"Hello\")!"
    liftIO $ putStrLn "null"
    autoEval "cd(\"tmp\")!"
    liftIO $ putStrLn "null"
    autoEval "read(\"hi.txt\")!"
    liftIO $ putStrLn "\"Hello\""
    autoEval "read"
    liftIO $ putStrLn "read"
    autoEval "read(\"hi.txt\")"
    liftIO $ putStrLn "read(\"hi.txt\")"
    autoEval "read(\"hi.txt\")!"
    liftIO $ putStrLn "\"Hello\""
    autoEval "rand"
    liftIO $ putStrLn "rand"
    autoEval "rand(0, 10)"
    liftIO $ putStrLn "rand(0, 10)"
    autoEval "rand(0, 10)!"
    liftIO $ putStrLn "---rand---"
    autoEval "rand(0, 10)!"
    liftIO $ putStrLn "---rand---"
    autoEval "echo"
    liftIO $ putStrLn "echo"
    autoEval "echo(\"Hello\")!"
    liftIO $ putStrLn "Hello\nnull"
    autoEval "echo(\"Hello\")"
    autoEval "\"Hello\"(0) || \"Z\""
    liftIO $ putStrLn "\"H\""
    autoEval "\"Hello\"(99) || \"Z\""
    liftIO $ putStrLn "\"Z\""
    autoEval "if(2 == 2, echo(\"OK\")!, echo(\"WTF\")!)"
    liftIO $ putStrLn "OK\nnull"
    autoEval "true || echo(\"Don't do this\")!"
    liftIO $ putStrLn "true"
    autoEval "false && echo(\"Don't do this\")!"
    liftIO $ putStrLn "false"
    autoEval "[# 00 ff #] && echo(\"Just do it\")!"
    liftIO $ putStrLn "Just do it\nnull"
    autoEval "count(\"Hello World\").o"
    liftIO $ putStrLn "2"
    autoEval "invert(count(\"big blue bag\"))"
    liftIO $ putStrLn "{ 1: [ \"u\", \"l\", \"i\", \"e\", \"a\" ], 2: [ \"g\", \" \" ], 3: [ \"b\" ] }"
    autoEval "fold(add, values(count(\"Hello, World!\")))"
    liftIO $ putStrLn "13"
    -- Переход в основной цикл
    loop (S.fromList [AllowRead, AllowWrite, AllowTime]))
  where
    autoEval input = do
      case parse input of
        Left err -> liftIO $ hPutStrLn stderr ("Parse error: " ++ errorBundlePretty err)
        Right expr -> do
          res <- liftIO $ runHIO (eval expr) (S.fromList [AllowRead, AllowWrite, AllowTime])
          case res of
            Left e  -> liftIO $ putStrLn $ "Evaluation error: " ++ show e
            Right v -> liftIO $ T.putStrLn (renderVal v)

    loop perms = do
      minput <- getInputLine "hi> "
      case minput of
        Nothing -> return ()
        Just input -> do
          -- liftIO $ putStrLn $ "Input: " ++ input
          case parse input of
            Left err -> do
              liftIO $ hPutStrLn stderr ("Parse error: " ++ errorBundlePretty err)
            Right expr -> do
              -- liftIO $ putStrLn $ "Parsed expression: " ++ show expr
              res <- liftIO $ runHIO (eval expr) perms
              case res of
                Left e  -> liftIO $ putStrLn $ "Evaluation error: " ++ show e
                Right v -> do
                  -- liftIO $ putStrLn $ "Evaluated value: " ++ show v
                  liftIO $ T.putStrLn (renderVal v)
          loop perms

renderVal :: HiValue -> T.Text
renderVal v = renderStrict (layoutPretty defaultLayoutOptions (prettyValue v))