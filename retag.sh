#!/usr/bin/env bash
set -e

commit_grep() {
    git --no-pager log --format='%H' --grep="$1"
}

echo 'This script will replace existing tags both locally and remotely.'
read -p 'Continue? (y/n) ' -n 1 -r
echo    # (optional) move to a new line
if [[ $REPLY =~ ^[Yy]$ ]]
then
    echo 'Deleting tags'
    git tag -l | xargs -n 1 git push --delete origin
    git tag -l | xargs -n 1 git tag -d

    echo 'Recreating tags'
    git tag basics $(commit_grep 'warmup')
    git tag core_api $(commit_grep 'core')
    git tag traversal $(commit_grep 'traversal')
    git tag cypher_read $(commit_grep 'Cypher reading')
    git tag cypher_write $(commit_grep 'Cypher writing')
    git tag rest $(commit_grep 'REST API')
    git tag bolt $(commit_grep 'Bolt')
    git tag extension $(commit_grep 'extension')
    git tag csv $(commit_grep 'LOAD CSV')

    echo 'Pushing tags'
    git push --tags
fi


