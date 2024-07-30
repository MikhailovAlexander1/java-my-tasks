#!/bin/bash

mkdir "foo"

javac -d foo ../../java-advanced-2023/modules/info.kgeorgiy.java.advanced.implementor/info/kgeorgiy/java/advanced/implementor/Impler.java ../../java-advanced-2023/modules/info.kgeorgiy.java.advanced.implementor/info/kgeorgiy/java/advanced/implementor/ImplerException.java ../../java-advanced-2023/modules/info.kgeorgiy.java.advanced.implementor/info/kgeorgiy/java/advanced/implementor/JarImpler.java ../info/kgeorgiy/ja/mikhailov/implementor/Implementor.java

jar cvmf MANIFEST.MF implementor.jar -C foo info/kgeorgiy/java/advanced/implementor/Impler.class -C foo info/kgeorgiy/java/advanced/implementor/ImplerException.class -C foo info/kgeorgiy/java/advanced/implementor/JarImpler.class -C foo info/kgeorgiy/ja/mikhailov/implementor/Implementor.class

rm -r "foo"