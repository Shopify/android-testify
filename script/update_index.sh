#!/bin/bash

BASEDIR=$(dirname "$0")
INDEX_FILE="${BASEDIR}/../index.md"

git checkout master "${BASEDIR}/../LICENSE"
git checkout master "${BASEDIR}/../Plugins/Gradle/README.md"
git checkout master "${BASEDIR}/../README.md"
git checkout master "${BASEDIR}/../RECIPES.md"

echo "---" > $INDEX_FILE
echo "layout: index" >> $INDEX_FILE
echo "---" >> $INDEX_FILE
echo "" >> $INDEX_FILE

cat "${BASEDIR}/../README.md" >> $INDEX_FILE