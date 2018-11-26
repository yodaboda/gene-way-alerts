#!/bin/sh

gradle test
GRADLE_RESULT=$?
exit $GRADLE_RESULT