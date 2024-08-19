#!/bin/bash

javadoc -d ../javadoc -private -cp \
../../java-advanced-2024/artifacts/info.kgeorgiy.java.advanced.implementor.jar: \
../../java-advanced-2024/artifacts/info.kgeorgiy.java.advanced.iterative.jar \
../java-solutions/info/kgeorgiy/ja/nazarov/iterative/IterativeParallelism.java \
../java-solutions/info/kgeorgiy/ja/nazarov/implementor/Implementor.java \
../java-solutions/info/kgeorgiy/ja/nazarov/iterative/IterativeParallelism.java \
../../java-advanced-2024/modules/info.kgeorgiy.java.advanced.iterative/info/kgeorgiy/java/advanced/iterative/NewScalarIP.java \
../../java-advanced-2024/modules/info.kgeorgiy.java.advanced.mapper/info/kgeorgiy/java/advanced/mapper/ParallelMapper.java \
../../java-advanced-2024/modules/info.kgeorgiy.java.advanced.iterative/info/kgeorgiy/java/advanced/iterative/ScalarIP.java
