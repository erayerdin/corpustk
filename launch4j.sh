#!/bin/bash

echo "## Compiling with Launch4j ##"
launch4j launch4j-config.xml
echo "== Compiling Done =="

echo "## Copying Resource Files... ##"
cp -r ./src/main/resources/* ./target/windows/
echo "== Copying Resources Done =="