#!/bin/sh

gradle sonarqube
GRADLE_RESULT=$?
exit $GRADLE_RESULT