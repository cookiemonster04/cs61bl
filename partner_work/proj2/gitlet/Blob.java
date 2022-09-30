package gitlet;

import java.io.File;
import java.io.Serializable;

public class Blob implements Serializable {
    private final String contents;
    private final String hashVal;

    public Blob(String filename) {
        File fin = Utils.join(Repository.CWD, filename);
        contents = Utils.readContentsAsString(fin);
        hashVal = hash(contents);
        File f = Utils.join(Repository.BLOB_DIR, hashVal);
        Utils.writeObject(f, this);
    }

    public static Blob loadBlob(String blobHash) {
        if (blobHash == null) {
            return null;
        }
        File r = Utils.join(Repository.BLOB_DIR, blobHash);
        if (r.exists()) {
            return Utils.readObject(r, Blob.class);
        } else {
            return null;
        }
    }

    public String hash() {
        return hashVal;
    }
    public String hash(String s) {
        return Utils.sha1(s);
    }

    public String getContents() {
        return contents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Blob)) {
            return false;
        }
        return hashVal.equals(((Blob)o).hash());
    }
}
