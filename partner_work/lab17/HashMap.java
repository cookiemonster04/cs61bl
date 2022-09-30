import java.util.Iterator;
import java.util.LinkedList;

public class HashMap<K,V> implements Map61BL<K,V> {
    double loadFactor = 0.75;
    LinkedList<Entry<K, V>>[] arr;
    int size;

    public HashMap() {
        arr = new LinkedList[16];
        for (int i = 0; i < 16; i++) {
            arr[i] = new LinkedList<>();
        }
        size = 0;
    }

    public HashMap(int initialCapacity) {
        arr = new LinkedList[initialCapacity];
        for (int i = 0; i < initialCapacity; i++) {
            arr[i] = new LinkedList<>();
        }
        size = 0;
    }

    public HashMap(int initialCapacity, double loadFactor) {
        this.loadFactor = loadFactor;
        arr = new LinkedList[initialCapacity];
        for (int i = 0; i < initialCapacity; i++) {
            arr[i] = new LinkedList<>();
        }
        size = 0;
    }

    @Override
    public void clear() {
        for (LinkedList<Entry<K, V>> entries : arr) {
            entries.clear();
        }
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        for (Entry<K, V> e : arr[Math.floorMod(key.hashCode(), arr.length)]) {
            if (e.key.equals(key)) return true;
        }
        return false;
    }

    @Override
    public V get(K key) {
        for (Entry<K, V> e : arr[Math.floorMod(key.hashCode(), arr.length)]) {
            if (e.key.equals(key)) return e.value;
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        Entry<K, V> n = new Entry<K, V>(key, value);
        Entry<K, V> rem = null;
        for (Entry<K, V> e : arr[Math.floorMod(key.hashCode(), arr.length)]) {
            if (e.keyEquals(n)) {
                rem = e;
            }
        }
        if (rem != null) {
            arr[Math.floorMod(key.hashCode(), arr.length)].remove(rem);
            size--;
        }
        if (size+1 > loadFactor * arr.length) {
            resize();
        }
        arr[Math.floorMod(key.hashCode(), arr.length)].addLast(n);
        size++;
    }

    @Override
    public V remove(K key) {
        Entry<K, V> rem = null;
        for (Entry<K, V> e : arr[Math.floorMod(key.hashCode(), arr.length)]) {
            if (e.key.equals(key)) {
                rem = e;
                break;
            }
        }
        if (rem != null) {
            arr[Math.floorMod(key.hashCode(), arr.length)].remove(rem);
            size--;
            return (V) rem.value;
        } else {
            return null;
        }
    }

    @Override
    public boolean remove(K key, V value) {
        Entry<K, V> rem = null;
        for (Entry<K, V> e : arr[Math.floorMod(key.hashCode(), arr.length)]) {
            if (e.key.equals(key) && e.value.equals(value)) {
                rem = e;
                break;
            }
        }
        if (rem != null) {
            arr[Math.floorMod(key.hashCode(), arr.length)].remove(rem);
            size--;
            return true;
        } else {
            return false;
        }
    }

    /* Returns the number of items contained in this map. */
    public int size() {
        return size;
    }

    @Override
    public Iterator<K> iterator() {
        return new HashMapIterator();
    }

    public void resize() {
        LinkedList<Entry<K, V>> entries = new LinkedList<>();
        for (LinkedList<Entry<K, V>> e : arr) {
            entries.addAll(e);
        }
        arr = new LinkedList[2*arr.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new LinkedList<>();
        }
        for (Entry<K, V> e : entries) {
            arr[Math.floorMod(e.key.hashCode(), arr.length)].add(e);
        }
    }

    /* Returns the length of this HashMap's internal array. */
    public int capacity() {
        return arr.length;
    }

    private static class Entry<K, V> {

        private K key;
        private V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /* Returns true if this key matches with the OTHER's key. */
        public boolean keyEquals(Entry<K, V> other) {
            return key.equals(other.key);
        }

        /* Returns true if both the KEY and the VALUE match. */
        @Override
        public boolean equals(Object other) {
            return (other instanceof Entry
                    && key.equals(((Entry<K, V>) other).key)
                    && value.equals(((Entry<K, V>) other).value));
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }

    private class HashMapIterator implements Iterator<K> {
        K cur;
        int numPassed;
        public HashMapIterator() {
            cur = null;
            numPassed = 0;
        }
        @Override
        public boolean hasNext() {
            return numPassed != size();
        }

        @Override
        public K next() {
            int bucket = 0;
            if (cur != null) {
                bucket = Math.floorMod(cur.hashCode(), arr.length);
                boolean found = false;
                for (Entry<K, V> e : arr[bucket]) {
                    if (found) {
                        numPassed++;
                        return cur = e.key;
                    }
                    if (e.key.equals(cur)) {
                        found = true;
                    }
                }
                bucket++;
            }
            while(bucket < arr.length) {
                if (!arr[bucket].isEmpty()) {
                    numPassed++;
                    return cur = arr[bucket].peekFirst().key;
                }
                bucket++;
            }
            return null;
        }
    }
}
