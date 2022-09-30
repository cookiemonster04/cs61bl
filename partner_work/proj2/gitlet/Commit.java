package gitlet;

import java.io.*;
import java.util.*;

/** Represents a gitlet commit object.
 *  Represents a commit node in the commit tree
 *  @author Xuanhao Cui and Frederick Zhang
 */

public class Commit implements Serializable {
    /* List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private final String message;
    private final Date timestamp;
    private final String parentCommitHash;
    private String mergeCommitHash;
    /** filename -> blob hash */
    public HashMap<String, String> blobs = new HashMap<>();
    public String hashVal;

    public Commit(String msg, Commit p) {
        timestamp = new Date(System.currentTimeMillis());
        message = msg;
        parentCommitHash = p.hash();
        for (Map.Entry<String, String> e : p.blobs.entrySet()) {
            blobs.put(e.getKey(), e.getValue());
        }
        hashVal = Utils.sha1(message, timestamp.toString(),
                (parentCommitHash == null) ? "No parent" : parentCommitHash,
                (mergeCommitHash == null) ? "No merge" : mergeCommitHash,
                blobs.toString());
        Utils.writeObject(Utils.join(Repository.COMMIT_DIR, hash()), this);
    }

    public Commit(String msg, Date time, Commit parent, HashMap<String, String> fileList) {
        message = msg;
        timestamp = time;
        if (parent == null) {
            parentCommitHash = null;
        }
        else {
            parentCommitHash = parent.hashVal;
        }
        blobs = fileList;
        hashVal = Utils.sha1(message, timestamp.toString(),
                (parentCommitHash == null) ? "No parent" : parentCommitHash,
                (mergeCommitHash == null) ? "No merge" : mergeCommitHash,
                blobs.toString());
        Utils.writeObject(Utils.join(Repository.COMMIT_DIR, hash()), this);
    }

    public Commit(String msg, Commit p, HashMap<String, String> StagingAdd, HashSet<String> StagingRemove) {
        timestamp = new Date(System.currentTimeMillis());
        message = msg;
        parentCommitHash = p.hash();
        for (Map.Entry<String, String> e : p.blobs.entrySet()) {
            blobs.put(e.getKey(), e.getValue());
        }
        for (Map.Entry<String, String> e : StagingAdd.entrySet()) {
            blobs.put(e.getKey(), e.getValue());
        }
        for (String filename : StagingRemove) {
            blobs.remove(filename);
        }
        hashVal = Utils.sha1(message, timestamp.toString(),
                (parentCommitHash == null) ? "No parent" : parentCommitHash,
                (mergeCommitHash == null) ? "No merge" : mergeCommitHash,
                blobs.toString());
        Utils.writeObject(Utils.join(Repository.COMMIT_DIR, hash()), this);
    }

    public String getMessage(){
        return message;
    }

    public String getDateString() {
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(timestamp.toString());
        while(st.countTokens() != 2) {
            sb.append(st.nextToken());
            sb.append(" ");
        }
        st.nextToken();
        sb.append(st.nextToken());
        sb.append(" ");
        sb.append(String.format("%tz", timestamp));
        return sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("===\n");
        sb.append("commit ");
        sb.append(hashVal);
        sb.append("\n");
        sb.append("Date: ");
        sb.append(getDateString());
        sb.append("\n");
        sb.append(getMessage());
        return sb.toString();
    }

    public void printLog() {
        /**
         * Format:
            ===
            commit a0da1ea5a15ab613bf9961fd86f010cf74c7ee48
            Date: Thu Nov 9 20:00:05 2017 -0800
            A commit message.
        */
        System.out.println(toString());
        if (parentCommitHash == null) return;
        System.out.println();
        loadCommit(parentCommitHash).printLog();
    }

    public static Commit loadCommit(String commitHash) {
        File r = Utils.join(Repository.COMMIT_DIR, commitHash);
        if (r.exists()) {
            return Utils.readObject(r, Commit.class);
        } else {
            return null;
        }
    }

    public String getParentCommitHash() {
        return parentCommitHash;
    }
    public String getMergeCommitHash() {
        return mergeCommitHash;
    }
    public Commit getParent(){
        return loadCommit(parentCommitHash);
    }
    public Commit getMergeParent() {
        return loadCommit(mergeCommitHash);
    }
    public Date getTimestamp(){
        return timestamp;
    }

    public String hash() {
        return hashVal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Commit commit = (Commit) o;
        return hashVal.equals(commit.hash());
    }

}
