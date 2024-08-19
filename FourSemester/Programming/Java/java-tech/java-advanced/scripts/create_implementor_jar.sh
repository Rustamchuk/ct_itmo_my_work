#!/bin/bash

javac -cp ../../java-advanced-2024/artifacts/info.kgeorgiy.java.advanced.implementor.jar ../java-solutions/info/kgeorgiy/ja/nazarov/implementor/Implementor.java
jar cfm Implementor.jar MANIFEST.MF -C ../java-solutions/info/kgeorgiy/ja/nazarov/implementor/ .

javac -cp ../../java-advanced-2024/artifacts/info.kgeorgiy.java.advanced.iterative.jar ../java-solutions/info/kgeorgiy/ja/nazarov/iterative/IterativeParallelism.java
jar cfm IterativeParallelism.jar MANIFEST.MF -C ../java-solutions/info/kgeorgiy/ja/nazarov/iterative/ .

javac -cp ../../java-advanced-2024/artifacts/info.kgeorgiy.java.advanced.mapper.jar ../java-solutions/info/kgeorgiy/ja/nazarov/iterative/ParallelMapperImpl.java
jar cfm ParallelMapperImpl.jar MANIFEST.MF -C ../java-solutions/ .