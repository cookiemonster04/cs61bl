import com.sun.source.tree.Tree;

public class BinarySearchTree<T extends Comparable<T>> extends BinaryTree<T> {

    /* Creates an empty BST. */
    public BinarySearchTree() {
        super();
    }

    /* Creates a BST with root as ROOT. */
    public BinarySearchTree(TreeNode root) {
        super(root);
    }

    /* Returns true if the BST contains the given KEY. */
    public boolean contains(T key) {
        TreeNode cur = root;
        while(cur != null) {
            if (cur.item.equals(key)) {
                return true;
            }
            if (key.compareTo(cur.item) < 0) {
                cur = cur.left;
            }
            else {
                cur = cur.right;
            }
        }
        return false;
    }

    /* Adds a node for KEY iff KEY isn't in the BST already. */
    public void add(T key) {
        TreeNode cur = root, last = null;
        if (cur == null) {
            root = new TreeNode(key);
            return;
        }
        while(cur != null) {
            if (key.compareTo(cur.item) == 0) { // in case equals is not the same as compareTo == 0
                return;
            }
            last = cur;
            if (key.compareTo(cur.item) < 0) {
                cur = cur.left;
            }
            else {
                cur = cur.right;
            }
        }
        if (key.compareTo(last.item) < 0) {
            last.left = new TreeNode(key);
        }
        else {
            last.right = new TreeNode(key);
        }
    }

    /* Deletes a node from the BST. 
     * Even though you do not have to implement delete, you 
     * should read through and understand the basic steps.
    */
    public T delete(T key) {
        TreeNode parent = null;
        TreeNode curr = root;
        TreeNode delNode = null;
        TreeNode replacement = null;
        boolean rightSide = false;

        while (curr != null && !curr.item.equals(key)) {
            if (curr.item.compareTo(key) > 0) {
                parent = curr;
                curr = curr.left;
                rightSide = false;
            } else {
                parent = curr;
                curr = curr.right;
                rightSide = true;
            }
        }
        delNode = curr;
        if (curr == null) {
            return null;
        }

        if (delNode.right == null) {
            if (root == delNode) {
                root = root.left;
            } else {
                if (rightSide) {
                    parent.right = delNode.left;
                } else {
                    parent.left = delNode.left;
                }
            }
        } else {
            curr = delNode.right;
            replacement = curr.left;
            if (replacement == null) {
                replacement = curr;
            } else {
                while (replacement.left != null) {
                    curr = replacement;
                    replacement = replacement.left;
                }
                curr.left = replacement.right;
                replacement.right = delNode.right;
            }
            replacement.left = delNode.left;
            if (root == delNode) {
                root = replacement;
            } else {
                if (rightSide) {
                    parent.right = replacement;
                } else {
                    parent.left = replacement;
                }
            }
        }
        return delNode.item;
    }
}