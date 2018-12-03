#!/bin/sh

red='\033[0;31m'
green='\033[0;32m'
yellow='\033[0;33m'
blue='\033[0;34m'
no_color='\033[0m'

echo -e "\n${yellow}Executing pre-commit hook${no_color}\n"

echo `pwd`

# The following code dealing with stashing was adapted from:
# https://stackoverflow.com/questions/20479794/how-do-i-properly-git-stash-pop-in-pre-commit-hooks-to-get-a-clean-working-tree
# First, stash index and work dir, keeping only the
# to-be-committed changes in the working directory.
old_stash=$(git rev-parse -q --verify refs/stash)
git stash save -q --keep-index
new_stash=$(git rev-parse -q --verify refs/stash)

# If there were no changes (e.g., `--amend` or `--allow-empty`)
# then nothing was stashed, and we should skip everything,
# including the tests themselves.  (Presumably the tests passed
# on the previous commit, so there is no need to re-run them.)
if [ "$old_stash" = "$new_stash" ]; then
    echo "pre-commit script: no changes to test"
    sleep 1 # XXX hack, editor may erase message
    exit 0
fi

#STASH_NAME="pre-commit-$(date +%s)"
#git stash save -q --keep-index $STASH_NAME

scripts/run_tests.sh
TEST_RESULT=$?
echo -e "\n${blue}Test result:$TEST_RESULT${no_color}\n"

sh scripts/run_formatter.sh
FORMATTER_RESULT=$?
echo -e "\n${blue}Formatter result:$FORMATTER_RESULT${no_color}\n"
if [[ $FORMATTER_RESULT -ne 0 ]]; then
	echo -e "\n${red}Run 'gradle spotlessApply' to fix issues${no_color}\n"
fi

scripts/run_sonar.sh
SONAR_RESULT=$?
echo -e "\n${blue}Sonar result:$SONAR_RESULT${no_color}\n"

RESULT=$((TEST_RESULT + FORMATTER_RESULT + SONAR_RESULT))

# Restore changes
git reset --hard -q && git stash apply --index -q && git stash drop -q

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