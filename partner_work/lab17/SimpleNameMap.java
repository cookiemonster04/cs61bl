import java.util.*;

public class SimpleNameMap {
    LinkedList<Entry>[] arr;
    int size;

    public SimpleNameMap() {
        arr = new LinkedList[26];
        for (int i = 0; i < 26; i++) {
            arr[i] = new LinkedList<>();
        }
        size = 0;
    }

    /* Returns the number of items contained in this map. */
    public int size() {
        return size;
    }

    /* Returns true if the map contains the KEY. */
    public boolean containsKey(String key) {
        for (Entry e : arr[Math.floorMod(key.hashCode(), arr.length)]) {
            if (e.key.equals(key)) return true;
        }
        return false;
    }

    /* Returns the value for the specified KEY. If KEY is not found, return
       null. */
    public String get(String key) {
        for (Entry e : arr[Math.floorMod(key.hashCode(), arr.length)]) {
            if (e.key.equals(key)) return e.value;
        }
        return null;
    }

    /* Puts a (KEY, VALUE) pair into this map. If the KEY already exists in the
       SimpleNameMap, replace the current corresponding value with VALUE. */
    public void put(String key, String value) {
        Entry n = new Entry(key, value);
        Entry rem = null;
        for (Entry e : arr[Math.floorMod(key.hashCode(), arr.length)]) {
            if (e.keyEquals(n)) {
                rem = e;
            }
        }
        if (rem != null) {
            arr[Math.floorMod(key.hashCode(), arr.length)].remove(rem);
            size--;
        }
        if (size+1 > 0.75 * arr.length) {
            resize();
        }
        arr[Math.floorMod(key.hashCode(), arr.length)].addLast(n);
        size++;
    }

    /* Removes a single entry, KEY, from this table and return the VALUE if
       successful or NULL otherwise. */
    public String remove(String key) {
        Entry rem = null;
        for (Entry e : arr[Math.floorMod(key.hashCode(), arr.length)]) {
            if (e.key.equals(key)) {
                rem = e;
            }
        }
        if (rem != null) {
            arr[Math.floorMod(key.hashCode(), arr.length)].remove(rem);
            size--;
            return rem.value;
        } else {
            return null;
        }
    }

    public void resize() {
        LinkedList<Entry> entries = new LinkedList<>();
        for (LinkedList<Entry> e : arr) {
            entries.addAll(e);
        }
        arr = new LinkedList[2*arr.length];
        for (int i = 0; i < 2*arr.length; i++) {
            arr[i] = new LinkedList<>();
        }
        for (Entry e : entries) {
            arr[Math.floorMod(e.key.hashCode(), arr.length)].add(e);
        }
    }

    private static class Entry {

        private String key;
        private String value;

        Entry(String key, String value) {
            this.key = key;
            this.value = value;
        }

        /* Returns true if this key matches with the OTHER's key. */
        public boolean keyEquals(Entry other) {
            return key.equals(other.key);
        }

        /* Returns true if both the KEY and the VALUE match. */
        @Override
        public boolean equals(Object other) {
            return (other instanceof Entry
                    && key.equals(((Entry) other).key)
                    && value.equals(((Entry) other).value));
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }
}