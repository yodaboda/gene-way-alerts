#!/bin/bash

gradle sonarqube
GRADLE_RESULT=$?
exit $GRADLE_RESULT