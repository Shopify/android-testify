#!/bin/bash

BASEDIR=$(dirname "$0")
INDEX_FILE="${BASEDIR}/../index.md"

echo "---" > $INDEX_FILE
echo "layout: index" >> $INDEX_FILE
echo "---" >> $INDEX_FILE
echo "" >> $INDEX_FILE

cat "${BASEDIR}/../README.md" >> $INDEX_FILE