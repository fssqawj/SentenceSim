#!/bin/bash
set -e
rm -r data/finacial/*
cp ../train.txt ../dev.txt ../test.txt data/finacial
CLASSPATH="lib:lib/stanford-parser/stanford-parser.jar:lib/stanford-parser/stanford-parser-3.5.1-models.jar"
javac -cp $CLASSPATH lib/*.java
python scripts/preprocess-finacial.py
