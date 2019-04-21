# Growing Object Oriented Software Guided by Tests in Kotlin

This is a follow-along Kotlin implementation of the Auction Sniper described in the book 
[Growing Object Oriented Software Guided by Tests](http://growing-object-oriented-software.com).

Feel free to open a PR if this needs to be updated.

## How to follow the implementation
There are tags for each chapter of the book. E.g. at the tag `chapter-11-the-walking-skeleton` you will find the
code as present at the end of the chapter.

Commit messages include the expected test status for the commit, e.g. `[RED]` at the start indicates a failing stage.

## Setup
1. Install `docker` and `docker-compose` via `brew` and `brew cask`
1. Make sure the shared folder at `/var/lib/docker/openfire` exists and is owned by you so openfire can persist state between container runs.
   
   E.g. on OSX run `sudo mkdir -p /var/lib/docker/openfire && sudo chown -Rv $(whoami):staff /var/lib/docker`
1. Run `docker-compose up`
1. In a browser, open `localhost:9090` and follow the Openfire setup. Make sure to bind to `0.0.0.0`.
1. Navigate to the admin console to create the user accounts:

   *Username*: _Password_
   
   *sniper*: _sniper_
   
   *auction-item-54321*: _auction_
   
   *auction-item-65432*: _auction_

That's all the setup you need - you're good to go!
