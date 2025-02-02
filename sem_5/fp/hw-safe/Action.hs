{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE DerivingVia #-}

module HW5.Action
  ( HiPermission(..)
  , PermissionException(..)
  , HIO(..)
--   , runHIO
  ) where

import Control.Exception (Exception, throwIO)
import Data.Set (Set)
import qualified Data.Set as S
import HW5.Base
import qualified Data.ByteString as BS
import qualified Data.Text as T
import Data.Text.Encoding (decodeUtf8')
import System.Directory
import Data.Time.Clock (getCurrentTime)
import System.Random (randomRIO)
import Data.List (sort)
import Control.Monad.Trans.Reader (ReaderT(..))  -- Импорт конструктора

data HiPermission =
    AllowRead
  | AllowWrite
  | AllowTime
  deriving (Eq, Ord, Show)

data PermissionException =
  PermissionRequired HiPermission
  deriving (Show)

instance Exception PermissionException

newtype HIO a = HIO { runHIO' :: Set HiPermission -> IO a }
  deriving (Functor, Applicative, Monad)
  via (ReaderT (Set HiPermission) IO)

instance HiMonad HIO where
  runAction act = HIO $ \perms -> do
    case act of
      HiActionCwd -> do
        require perms AllowRead
        cwd <- getCurrentDirectory
        return $ HiValueString (T.pack cwd)
      HiActionChDir path -> do
        require perms AllowRead
        setCurrentDirectory path
        return HiValueNull
      HiActionRead path -> do
        require perms AllowRead
        isDir <- doesDirectoryExist path
        if isDir
          then do
            contents <- listDirectory path
            return $ HiValueList (foldMap (pure . HiValueString . T.pack) (sort contents))
          else do
            exists <- doesFileExist path
            if not exists then return HiValueNull
            else do
              bs <- BS.readFile path
              case decodeUtf8' bs of
                Left _ -> return (HiValueBytes bs)
                Right txt -> return (HiValueString txt)

      HiActionWrite path bytes -> do
        require perms AllowWrite
        BS.writeFile path bytes
        return HiValueNull
      HiActionMkDir path -> do
        require perms AllowWrite
        createDirectoryIfMissing True path
        return HiValueNull
      HiActionNow -> do
        require perms AllowTime
        t <- getCurrentTime
        return (HiValueTime t)
      HiActionRand l r -> do
        x <- randomRIO (l,r)
        return (HiValueNumber (toRational x))
      HiActionEcho txt -> do
        require perms AllowWrite
        putStrLn (T.unpack txt)
        return HiValueNull
    where
      require pset p = if p `S.member` pset then return () else throwIO (PermissionRequired p)

-- runHIO :: HIO a -> Set HiPermission -> IO a
-- runHIO (HIO f) = f