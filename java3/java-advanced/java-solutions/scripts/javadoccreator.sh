#!/bin/bash

sources="../info/kgeorgiy/ja/mikhailov/implementor/Implementor.java"
sources="$sources ../module-info.java"

javadoc -private $sources --module-path ../../java-advanced-2023/lib:../../java-advanced-2023/artifacts -d ../javadoc