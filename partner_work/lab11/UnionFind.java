import java.util.Arrays;

public class UnionFind {

    int[] uf;

    /* Creates a UnionFind data structure holding N items. Initially, all
       items are in disjoint sets. */
    public UnionFind(int N) {
        uf = new int[N];
        Arrays.fill(uf, -1);
    }

    /* Returns the size of the set V belongs to. */
    public int sizeOf(int v) {
        return -uf[find(v)];
    }

    /* Returns the parent of V. If V is the root of a tree, returns the
       negative size of the tree for which V is the root. */
    public int parent(int v) {
        return uf[v];
    }

    /* Returns true if nodes V1 and V2 are connected. */
    public boolean connected(int v1, int v2) {
        return find(v1) == find(v2);
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. If invalid items are passed into this
       function, throw an IllegalArgumentException. */
    public int find(int v) {
        if (v < 0 || v >= uf.length) {
            throw new IllegalArgumentException();
        }
        if (uf[v] < 0) { return v; }
        return uf[v] = find(uf[v]);
    }

    /* Connects two items V1 and V2 together by connecting their respective
       sets. V1 and V2 can be any element, and a union-by-size heuristic is
       used. If the sizes of the sets are equal, tie break by connecting V1's
       root to V2's root. Union-ing a item with itself or items that are
       already connected should not change the structure. */
    public void union(int v1, int v2) {
        if (connected(v1, v2)) { return; }
        v1 = find(v1);
        v2 = find(v2);
        if (sizeOf(v1) <= sizeOf(v2)) {
            uf[v2] += uf[v1];
            uf[v1] = v2;
        } else{
            uf[v1] += uf[v2];
            uf[v2] = v1;
        }
    }
}
