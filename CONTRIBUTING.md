# Maintainer's guide

Thanks for your interest in contributing to these Neo4j exercises!

There are two different kinds of contributions here:

  1. changes of exercise solutions: make sure to push to the `solutions` branch
  1. changes of exercises: this is different. This may look strange but be sure to fix your changes up to **existing `master` commits** (`git commit --fixup [...]`) 
  and rebase (`git rebase -i --autosquash [...]`). 
  Once it's done, make sure to run `retag.sh` to recreate the tags (because the local history will have changed).
  Also, as you may have guessed, you have to `git push --force-with-lease` to `master`.
