#!/bin/bash

currentDir=`pwd`
cd ..
gradle test
GRADLE_RESULT=$?
cd $currentDir
exit $GRADLE_RESULT