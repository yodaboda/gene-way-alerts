#!/bin/bash

gradle test
GRADLE_RESULT=$?
exit $GRADLE_RESULT