# Gitlet Design Document

**Name**: Leo Cui and Frederick Zhang

## Classes and Data Structures

### Commit

This class is used to store commits....

#### Fields

1. Log Message: User description of commit
2. Timestamp: Time in current time zone
3. Parent Reference 1: Points to the last commit
4. Parent Reference 2: Points to the merged branch
5. Stores a map where keys are file names and values are the blob hashes

### Repository

This class controls the overall structure. 
This includes keeping track of commit branches.. 

#### Fields

1. HEAD: Hash code for the head commit of the current branch
2. Ends: Maps the name of the branch to the hash code for the commit 
   at the end of each branch
3. CommitMap: Maps the commit hash codes to commit references
4. 2 HashMaps for Staging Area (one for add, one for removal):
    - Key: File name
    - Value: Blob object (Maybe blob hash? idk)

### Blobs

#### Fields
1. String representing the file contents
2. String representing sha1 hash value

## Algorithms

### Init
- Creates the .gitlet folder (Implemented)
    - Staging Area (for addition/removal) (Implemented)
    - Commit Tree (Implemented)
    - Blobs: Contents of files (Implemented)
- Creates the commit folder under the .gitlet folder (Implemented)
  - TODO: Store each commit individually in this folder
  - TODO: Each commit will each be a serialized file
- Creates 1 empty initial commit (Implemented)
- Creates the master branch (Implemented)
    - set HEAD to master (Implemented)

### Add [filename]
- Adds/overwrites the file to the staging 
   area
- If contents are the same, do not add to staging area  
- If contents are different but the file is the same,
create a new blob

### Commit [message]
- Clone parent commit
    - Change metadata (timestamp + message)
    - Look at and track the staging area
    - Update HEAD pointer and parent commit
- Saves tracked files from parent commit and
   staging area
- Untrack if staged for removal by rm

### rm [filelname]
- Remove file from staging area
- Only remove file from commit if it is currently
being tracked

### log 
- Start at HEAD and traverse backwards using 
  commit parent reference
- Print commit id, time, and message
- Include merged parent's id if applicable 

### global-log
- log, but all commits instead of HEAD branch
- Util's PlainFileNamesIn returns files in a 
   directory
  
### find
- Iterate over a list of commits and check if commit message is equal
- If so, print

### status
4 Parts:
- Branches
  - Print out the name of each branch (from branch name hashmap)
  - Check which branch the HEAD is pointing to, 
    so we know which branch to star
- Staged/Removed Files
  - Iterate over the keys in the 2 hashmaps for staged/removed files
- Modifications not Staged for Commit
  - If a file is not staged, check last commit to see if there's a file for it.
    If so, check last commit's hash of the file with current hash.
    If not equal, it's modified & not staged for commit.
- Untracked files
  - If a file is not staged & last commit does not have this file, 
    it is untracked.

### checkout
java gitlet.Main checkout -- [file name]:
- Replace the current file with a copy of the file at HEAD.
- New file is not staged.

#### Implementation:
- Remove the current file from staging area if it's there
- Removes current file
- Copy and add the file at HEAD
- Is it tracked? not sure.
- Basically special case of command below (use process for below to implement this)

#### java gitlet.Main checkout [commit id] -- [file name]:
- Replace the current file with a copy of the file of commit [commit id].
- Same stuff as before but commit with [commit id] instead of HEAD

#### java gitlet.Main checkout [branch name]
- Make a copy of everything in the commit at the last commit of branch name
- Stuff the copies into the repository
- Remove all TRACKED FILES not in the branch (only tracked I think)
  - Actually, remove all tracked files from the directory, since
    we're copying everything from the branch anyway so it doesn't matter
    whether the file is in that branch or not - it'll all get replaced.
- Clear staging area
- Set HEAD to the last commit of branch of name [branch name]


### branch

- Make a branch that points at HEAD.
- Does not swap to that branch.
- Apparently we're supposed to hash this? (Says something about SHA-1 identifier)

### rm-branch

- Deletes the pointer associated with the branch.
- (How does this actually work though? You need a pointer to the end of the
  branch or else you lose the branch's commits...)

### reset
- Basically java gitlet.Main checkout [commit id] -- [file name] for all files.
- Can be implemented just by calling checkout in a for loop
- Also need to remove all tracked files that are not in that commit
  - to be honest, we can just remove all tracked files since we're
    replacing them with files from that commit anyway
- Move head pointer to that commit

## Persistence
- Store files in the .gitlet and serialize objects.
- git status has several sections:
    - Branches (master, etc)
    - Staged Files (add)
    - Removed Files
    - Modifications Not Staged for Commit
    - Untracked Files
