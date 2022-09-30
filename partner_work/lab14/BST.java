import java.util.LinkedList;
import java.util.Iterator;

public class BST<T> {

    BSTNode<T> root;

    public BST(LinkedList<T> list) {
        root = sortedIterToTree(list.iterator(), list.size());
    }

    /* Returns the root node of a BST (Binary Search Tree) built from the given
       iterator ITER  of N items. ITER will output the items in sorted order,
       and ITER will contain objects that will be the item of each BSTNode. */
    private BSTNode<T> sortedIterToTree(Iterator<T> iter, int N) {
        if (N == 0) {
           return null;
        }
        // get array of items, then Binary Search to build
        T[] items = (T[]) new Object[N];
        for (int i = 0; i < N; i++) {
            items[i] = iter.next();
        }
        return help(items, 0, N - 1);
    }

    private BSTNode<T> help(T[] items, int s, int e) {
        int middle = (s + e) / 2;
        BSTNode<T> node = new BSTNode<T>(items[middle]);
        if (s == e) {
            return node;
        }
        node.left = help(items, s, middle - 1);
        node.right = help(items, middle + 1, e);
        return node;
    }

    /* Prints the tree represented by ROOT. */
    private void print() {
        print(root, 0);
    }

    private void print(BSTNode<T> node, int d) {
        if (node == null) {
            return;
        }
        for (int i = 0; i < d; i++) {
            System.out.print("  ");
        }
        System.out.println(node.item);
        print(node.left, d + 1);
        print(node.right, d + 1);
    }

    class BSTNode<T> {
        T item;
        BSTNode<T> left;
        BSTNode<T> right;

        BSTNode(T item) {
            this.item = item;
        }
    }
}
