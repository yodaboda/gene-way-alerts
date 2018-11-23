#!/bin/bash

red='\033[0;31m'
green='\033[0;32m'
yellow='\033[0;33m'
no_color='\033[0m'

echo -e "\n${yellow}Executing pre-commit hook${no_color}\n"

echo `pwd`


#STASH_NAME="pre-commit-$(date +%s)"
#git stash save -q --keep-index $STASH_NAME

scripts/run_tests.sh
RESULT=$?

#STASHES=$(git stash list)
#if [[ $STASHES == "$STASH_NAME" ]]; then
#  git stash pop -q
#fi


if [[ $RESULT -ne 0 ]]; then
	echo -e "\n${red}Git hook failed${no_color}\n"
	exit 1
fi

echo -e "${green}Git hook was SUCCESSFUL!${no_color}\n"
exit 0