[![Build Status](https://travis-ci.com/ju851zel/Rummy.svg?branch=master)](https://travis-ci.com/ju851zel/Rummy)

[![Coverage Status](https://coveralls.io/repos/github/ju851zel/Rummy/badge.svg?branch=master)](https://coveralls.io/github/ju851zel/Rummy?branch=master)

1. copy the newest files into lib.
2. Open module settings [cmd + ;]
3. Click libraries on the left
4. Click the plus at the top
5. Click java and select the /lib folder

6. Co to modules on the left
7. CLick on each module -> dependencies -> plus at the bottom left -> library -> select the imported


## Create new module
1. build.sbt add module like the others
2. copy existing playerModule
3. paste in root and rename
4. sync project
5. Notice: No need to add the module by hand into IntelliJ


###TODO
#Robin
The controller has methods that require as first param the desk and always return the new desk on write operations.
It can return something else if it's just a read operation
Implement that Tui and Gui, regardless which of them triggers a command to the controller both get the newest updated desk (sth. like observer)
#Julian
Make the controller not depending Commands. Store the stack directly in the controller including all past objects.

