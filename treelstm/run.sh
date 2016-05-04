#!/bin/bash
set -e
rm -r data/finacial/*
cp ../train.txt ../dev.txt ../test.txt data/finacial
python scripts/preprocess-finacial.py
