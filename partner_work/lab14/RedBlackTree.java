import java.util.ArrayList;
import java.util.Collections;

public class RedBlackTree<T extends Comparable<T>> {

    /* Root of the tree. */
    RBTreeNode<T> root;

    static class RBTreeNode<T> {

        final T item;
        boolean isBlack;
        RBTreeNode<T> left;
        RBTreeNode<T> right;

        /* Creates a RBTreeNode with item ITEM and color depending on ISBLACK
           value. */
        RBTreeNode(boolean isBlack, T item) {
            this(isBlack, item, null, null);
        }

        /* Creates a RBTreeNode with item ITEM, color depending on ISBLACK
           value, left child LEFT, and right child RIGHT. */
        RBTreeNode(boolean isBlack, T item, RBTreeNode<T> left,
                   RBTreeNode<T> right) {
            this.isBlack = isBlack;
            this.item = item;
            this.left = left;
            this.right = right;
        }
    }

    /* Creates an empty RedBlackTree. */
    public RedBlackTree() {
        root = null;
    }

    /* Creates a RedBlackTree from a given BTree (2-3-4) TREE. */
    public RedBlackTree(BTree<T> tree) {
        Node<T> btreeRoot = tree.root;
        root = buildRedBlackTree(btreeRoot);
    }

    /* Builds a RedBlackTree that has isometry with given 2-3-4 tree rooted at
       given node R, and returns the root node. */
    RBTreeNode<T> buildRedBlackTree(Node<T> r) {
        if (r == null) {
            return null;
        }
        ArrayList<RBTreeNode<T>> ret = new ArrayList<>();
        int childCount = r.getChildrenCount();
        for (int i = 0; i < childCount; i++) {
            ret.add(buildRedBlackTree(r.getChildAt(i)));
        }
        while(ret.size() < 4) {
            ret.add(null);
        }
        if (r.getItemCount() == 1) {
            return new RBTreeNode<T>(true, r.getItemAt(0), ret.get(0), ret.get(1));
        } else if (r.getItemCount() == 2) {
            T first = r.getItemAt(0), second = r.getItemAt(1);
            T smallest, largest;
            if (first.compareTo(second) < 0) {
                largest = second;
                smallest = first;
            }
            else {
                largest = first;
                smallest = second;
            }
            RBTreeNode<T> smallestNode = new RBTreeNode<T>(false, smallest, ret.get(0), ret.get(1));
            return new RBTreeNode<T>(true, largest, smallestNode, ret.get(2));
        } else {
            ArrayList<T> items = new ArrayList<>();
            items.add(r.getItemAt(0)); items.add(r.getItemAt(1)); items.add(r.getItemAt(2));
            Collections.sort(items);
            RBTreeNode<T> smallestNode = new RBTreeNode<T>(false, items.get(0), ret.get(0), ret.get(1));
            RBTreeNode<T> largestNode = new RBTreeNode<T>(false, items.get(2), ret.get(2), ret.get(3));
            return new RBTreeNode<T>(true, items.get(1), smallestNode, largestNode);
        }
    }

    /* Flips the color of NODE and its children. Assume that NODE has both left
       and right children. */
    void flipColors(RBTreeNode<T> node) {
        node.isBlack = !node.isBlack;
        node.left.isBlack = !node.left.isBlack;
        node.right.isBlack = !node.right.isBlack;
    }

    /* Rotates the given node NODE to the right. Returns the new root node of
       this subtree. */
    RBTreeNode<T> rotateRight(RBTreeNode<T> node) {
        boolean oldColor = node.isBlack;
        RBTreeNode<T> left = node.left;
        node.left = left.right;
        left.right = node;
        node.isBlack = false;
        left.isBlack = oldColor;
        return left;
    }

    /* Rotates the given node NODE to the left. Returns the new root node of
       this subtree. */
    RBTreeNode<T> rotateLeft(RBTreeNode<T> node) {
        boolean oldColor = node.isBlack;
        RBTreeNode<T> right = node.right;
        node.right = right.left;
        right.left = node;
        node.isBlack = false;
        right.isBlack = oldColor;
        return right;
    }

    public void insert(T item) {   
        root = insert(root, item);  
        root.isBlack = true;
    }

    /* Inserts the given node into this Red Black Tree*/
    private RBTreeNode<T> insert(RBTreeNode<T> node, T item) {
        // Insert (return) new red leaf node.
        if (node == null) {
            return new RBTreeNode<>(false, item);
        }

        // Handle normal binary search tree insertion.
        int comp = item.compareTo(node.item);
        if (comp == 0) {
            return node; // do nothing.
        } else if (comp < 0) {
            node.left = insert(node.left, item);
        } else {
            node.right = insert(node.right, item);
        }

        // handle case C and "Right-leaning" situation.
        if (node.left == null) {
            node = rotateLeft(node);
        }
        // handle case B
        if (node.left != null && node.left.left != null && !node.left.isBlack && !node.left.left.isBlack) {
            node = rotateRight(node);
        }
        // handle case A
        if (node.left != null && node.right != null && !node.left.isBlack && !node.right.isBlack) {
            flipColors(node);
        }
        return node;
    }

    /* Returns whether the given node NODE is red. Null nodes (children of leaf
       nodes are automatically considered black. */
    private boolean isRed(RBTreeNode<T> node) {
        return node != null && !node.isBlack;
    }

}
