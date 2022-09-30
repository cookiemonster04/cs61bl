package gitlet;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import static gitlet.Utils.*;

/** Represents a gitlet repository.
 *  Handles all the operations called on Main.java
 *  @author Xuanhao Cui and Frederick Zhang
 */

public class Repository implements Serializable{
    /**
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = Utils.join(CWD, ".gitlet");
    /** The commits directory. */
    public static final File COMMIT_DIR = Utils.join(CWD, ".gitlet", "commits");
    /** The blobs directory. */
    public static final File BLOB_DIR = Utils.join(CWD, ".gitlet", "blobs");
    /** Hash Code for the commit pointed to by the head. */
    public String HEAD;
    /** Name of the currentBranch */
    public String currentBranch;
    /** Branch Name -> Hash Code for the commit at the end of the branch */
    public HashMap<String, String> ends = new HashMap<>();
    /** File Name -> Hash Code for blob */
    public HashMap<String, String> StagingAdd = new HashMap<>();
    /** List of names of files to be removed */
    public HashSet<String> StagingRemove = new HashSet<>();

    public Repository() {
        if (!GITLET_DIR.exists()) {
            GITLET_DIR.mkdir();
        }
        if (!COMMIT_DIR.exists()) {
            COMMIT_DIR.mkdir();
        }
        if (!BLOB_DIR.exists()) {
            BLOB_DIR.mkdir();
        }
        Commit initialCommit = new Commit("initial commit", new Date(0), null, new HashMap<>());
        String commitHash = initialCommit.hash();
        HEAD = commitHash;
        ends.put("master", commitHash);
        currentBranch = "master";
        Utils.writeObject(Utils.join(GITLET_DIR, "repo"), this);
    }

    public static Repository loadRepo() {
        File r = Utils.join(GITLET_DIR, "repo");
        if (r.exists()) {
            return Utils.readObject(r, Repository.class);
        } else {
            return null;
        }
    }

    public void storeRepo() {
        File r = Utils.join(GITLET_DIR, "repo");
        Utils.writeObject(r, this);
    }

    public void add(String filename) {
        Blob b;
        try {
            b = new Blob(filename);
        }
        catch (IllegalArgumentException e) {
            System.out.println("File does not exist.");
            return;
        }
        Commit c = Commit.loadCommit(HEAD);
        StagingRemove.remove(filename);
        StagingAdd.remove(filename);
        String blobHash = c.blobs.get(filename);
        if (blobHash == null) {
            // the parent does not have this file
            StagingAdd.put(filename, b.hash());
        }
        else {
            Blob cBlob = Blob.loadBlob(blobHash);
            // the parent does have this file
            if (!cBlob.hash().equals(b.hash())) {
                StagingAdd.put(filename, b.hash());
            }
        }
    }

    public void rm(String filename) {
        Commit lastCommit = Commit.loadCommit(HEAD);
        if (lastCommit.blobs.containsKey(filename)) {
            StagingRemove.add(filename);
//            do not remove it unless it is tracked in the current commit:
//            tracked by staging, or last commit?
            restrictedDelete(filename);
        } else {
            if (!StagingAdd.containsKey(filename)) {
                System.out.println("No reason to remove the file.");
                return;
            }
        }
        StagingAdd.remove(filename);
    }

    public void commit(String msg) {
        if (StagingAdd.isEmpty() && StagingRemove.isEmpty()) {
            System.out.println("No changes added to the commit.");
            return;
        }
        Commit lastCommit = Commit.loadCommit(HEAD);
        Commit nc = new Commit(msg, lastCommit, StagingAdd, StagingRemove);
        String commitHash = nc.hash();
        // update HEAD
        HEAD = commitHash;
        ends.put(currentBranch, HEAD);

        // clear staging area
        StagingRemove.clear();
        StagingAdd.clear();
    }

    public void log() {
        Commit lastCommit = Commit.loadCommit(HEAD);
        lastCommit.printLog();
    }

    public void globalLog() {
        List<String> files = Utils.plainFilenamesIn(COMMIT_DIR);
        for (int i = 0; i < files.size() - 1; i++) {
            Commit c = Commit.loadCommit(files.get(i));
            System.out.println(c.toString());
            System.out.println();
        }
        Commit c = Commit.loadCommit(files.get(files.size() - 1));
        System.out.println(c.toString());
    }

    public void find(String msg) {
        List<String> files = Utils.plainFilenamesIn(COMMIT_DIR);
        boolean found = false;
        for (int i = 0; i < files.size(); i++) {
            Commit c = Commit.loadCommit(files.get(i));
            if (c.getMessage().equals(msg)) {
                System.out.println(c.hashVal);
                found = true;
            }
        }
        if (!found) System.out.println("Found no commit with that message.");
    }

    public void status() {
        ArrayList<String> branches = new ArrayList<>();
        System.out.println("=== Branches ===");
        branches.addAll(ends.keySet());
        Collections.sort(branches);
        for (String name : branches) {
            if (name.equals(currentBranch)) {
                System.out.print("*");
            }
            System.out.println(name);
        }
        System.out.println();

        ArrayList<String> staged = new ArrayList<>();
        System.out.println("=== Staged Files ===");
        staged.addAll(StagingAdd.keySet());
        Collections.sort(staged);
        for (String name : staged) {
            System.out.println(name);
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        ArrayList<String> removed = new ArrayList<>(StagingRemove);
        Collections.sort(removed);
        for (String name : removed) {
            System.out.println(name);
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");
        /** Tracked in the current commit, changed in the working directory, but not staged */
        /** Not staged for removal, but tracked in the current commit and deleted from the working directory. */
        Commit lastCommit = Commit.loadCommit(HEAD);
        ArrayList<String> mfiles = new ArrayList<>();
        for (Entry<String, String> e : lastCommit.blobs.entrySet()) {
            String filename = e.getKey();
            String blobhash = e.getValue();
            File f = Utils.join(Repository.CWD, filename);
            if (f.exists()) {
                String contentHash = Utils.sha1(Utils.readContentsAsString(f));
                if (!blobhash.equals(contentHash)) {
                    mfiles.add(filename + " (modified)");
                }
            }
            else if (!StagingRemove.contains(filename)){
                mfiles.add(filename + " (removed)");
            }
        }
        /** Staged for addition, but with different contents than in the working directory */
        /** Staged for addition, but deleted in the working directory */
        for (Entry<String, String> e : StagingAdd.entrySet()) {
            String filename = e.getKey();
            String blobhash = e.getValue();
            File f = Utils.join(Repository.CWD, filename);
            if (f.exists()) {
                String contentHash = Utils.sha1(Utils.readContentsAsString(f));
                if (!blobhash.equals(contentHash)) {
                    mfiles.add(filename + " (modified)");
                }
            }
            else {
                mfiles.add(filename + " (removed)");
            }
        }

        Collections.sort(mfiles);
        for (String filename : mfiles) {
            System.out.println(filename);
        }
        System.out.println();

        /** Untracked Stuff */
        System.out.println("=== Untracked Files ===");
        List<String> files = Utils.plainFilenamesIn(CWD);
        for (String s : files) {
            if (!StagingAdd.containsKey(s) && !lastCommit.blobs.containsKey(s)) {
                System.out.println(s);
            }
        }
    }


    /** Checkout for a specific commit */
    public void checkout(String commitID, String filename) {
        Commit targetCommit = null;
        List<String> files = Utils.plainFilenamesIn(COMMIT_DIR);
        boolean found = false;
        for (String name : files) {
            if (name.startsWith(commitID)) {
                if (found) {
                    System.out.println("There is more than one commit ID with that prefix.");
                    return;
                }
                else {
                    found = true;
                    targetCommit = Commit.loadCommit(name);
                }
            }
        }
        if (targetCommit == null) {
            System.out.println("No commit with that id exists.");
            return;
        }
        if (!targetCommit.blobs.containsKey(filename)) {
            System.out.println("File does not exist in that commit.");
            return;
        }
        String blobHash = targetCommit.blobs.get(filename);
        File blobDir = Utils.join(Repository.BLOB_DIR, blobHash);
        Blob b = Utils.readObject(blobDir, Blob.class);
        Utils.writeContents(Utils.join(Repository.CWD, filename), b.getContents());
    }
    /** Checkout for a specific branch */
    public void checkout(String branch) {
        if (!ends.containsKey(branch)) {
            System.out.println("No such branch exists.");
            return;
        }
        if (currentBranch.equals(branch)) {
            System.out.println("No need to checkout the current branch.");
            return;
        }
        checkoutAll(ends.get(branch));
        currentBranch = branch;
    }

    public void checkoutAll(String commitID) {
        List<String> files = Utils.plainFilenamesIn(CWD);
        Commit targetCommit = Commit.loadCommit(commitID);
        Commit lastCommit = Commit.loadCommit(HEAD);
        for (String filename : files) {
            if (!lastCommit.blobs.containsKey(filename) && targetCommit.blobs.containsKey(filename)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                return;
            }
        }
        for (String filename : lastCommit.blobs.keySet()) {
            Utils.restrictedDelete(Utils.join(CWD, filename));
        }
        for (Entry<String, String> e : targetCommit.blobs.entrySet()) {
            Blob b = Blob.loadBlob(e.getValue());
            Utils.writeContents(Utils.join(CWD, e.getKey()), b.getContents());
        }
        StagingAdd.clear();
        StagingRemove.clear();
        HEAD = commitID;
    }

    public void branch(String branch) {
        if (ends.containsKey(branch)) {
            System.out.println("A branch with that name already exists.");
            return;
        }
        ends.put(branch, HEAD);
    }

    public void rm_branch(String branch) {
        if (!ends.containsKey(branch)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if (currentBranch.equals(branch)) {
            System.out.println("Cannot remove the current branch.");
            return;
        }
        ends.remove(branch);
    }

    public void reset(String commitID) {
        String actualID = null;
        boolean found = false;
        List<String> files = Utils.plainFilenamesIn(COMMIT_DIR);
        for (String name : files) {
            if (name.startsWith(commitID)) {
                if (found) {
                    System.out.println("There is more than one commit ID with that prefix.");
                    return;
                }
                else {
                    found = true;
                    actualID = name;
                }
            }
        }
        if (actualID == null) {
            System.out.println("No commit with that id exists.");
            return;
        }
        checkoutAll(actualID);
        ends.replace(currentBranch, HEAD);
    }
}
