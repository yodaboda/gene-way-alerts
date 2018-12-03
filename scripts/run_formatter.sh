#!/bin/sh

gradle spotlessCheck
GRADLE_RESULT=$?
exit $GRADLE_RESULT