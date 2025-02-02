{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE DeriveGeneric #-}
{-# LANGUAGE DeriveAnyClass #-}

module HW5.Base
  ( HiFun(..)
  , HiValue(..)
  , HiExpr(..)
  , HiError(..)
  , HiAction(..)
  , HiMonad(..)
  ) where

import Data.Text (Text)
import Data.Sequence (Seq)
import Data.ByteString (ByteString)
import Data.Time.Clock (UTCTime)
import Data.Map (Map)
import GHC.Generics (Generic)
import Codec.Serialise (Serialise)

-- Функции
data HiFun
  = HiFunDiv
  | HiFunMul
  | HiFunAdd
  | HiFunSub
  | HiFunNot
  | HiFunAnd
  | HiFunOr
  | HiFunLessThan
  | HiFunGreaterThan
  | HiFunEquals
  | HiFunNotLessThan
  | HiFunNotGreaterThan
  | HiFunNotEquals
  | HiFunIf
  -- Task4
  | HiFunLength
  | HiFunToUpper
  | HiFunToLower
  | HiFunReverse
  | HiFunTrim
  -- Task5
  | HiFunList
  | HiFunRange
  | HiFunFold
  -- Task6
  | HiFunPackBytes
  | HiFunUnpackBytes
  | HiFunEncodeUtf8
  | HiFunDecodeUtf8
  | HiFunZip
  | HiFunUnzip
  | HiFunSerialise
  | HiFunDeserialise
  -- Task7
  | HiFunRead
  | HiFunWrite
  | HiFunMkDir
  | HiFunChDir
  -- Task8
  | HiFunParseTime
  -- Task9
  | HiFunRand
  -- Task10
  | HiFunEcho
  -- Task11
  | HiFunCount
  | HiFunKeys
  | HiFunValues
  | HiFunInvert
  deriving (Eq, Ord, Show, Generic, Serialise)

-- Действия
data HiAction
  = HiActionRead  FilePath
  | HiActionWrite FilePath ByteString
  | HiActionMkDir FilePath
  | HiActionChDir FilePath
  | HiActionCwd
  | HiActionNow
  | HiActionRand Int Int
  | HiActionEcho Text
  deriving (Eq, Ord, Show, Generic, Serialise)

-- Значения
data HiValue
  = HiValueNull
  | HiValueBool Bool
  | HiValueNumber Rational
  | HiValueFunction HiFun
  | HiValueString Text
  -- Task5
  | HiValueList (Seq HiValue)
  -- Task6
  | HiValueBytes ByteString
  -- Task7
  | HiValueAction HiAction
  -- Task8
  | HiValueTime UTCTime
  -- Task11
  | HiValueDict (Map HiValue HiValue)
  deriving (Eq, Ord, Show, Generic, Serialise)

-- Выражения
data HiExpr
  = HiExprValue HiValue
  | HiExprApply HiExpr [HiExpr]
  | HiExprRun HiExpr
  | HiExprDict [(HiExpr, HiExpr)]
  deriving (Eq, Show)

-- Ошибки
data HiError
  = HiErrorInvalidArgument
  | HiErrorInvalidFunction
  | HiErrorArityMismatch
  | HiErrorDivideByZero
  deriving (Eq, Show)

-- Класс монад
class Monad m => HiMonad m where
  runAction :: HiAction -> m HiValue