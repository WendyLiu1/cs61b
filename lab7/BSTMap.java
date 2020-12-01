import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V extends Comparable<V>> implements Map61B<K, V> {
    private int size;
    private TreeNode root;
    private TreeNode removedNode;

    BSTMap() {
        this.clear();
    }

    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        TreeNode ptr = this.root;
        while (ptr != null) {
            int cmp = key.compareTo(ptr.key);
            if (cmp == 0) {
                return ptr.val;
            } else if (cmp < 0) {
                ptr = ptr.left;
            } else {
                ptr = ptr.right;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return this.get(key) != null;
    }

    @Override
    public void put(K key, V value) {
        this.root = this.insert(this.root, key, value);
    }

    /**
     * Recursive function to insert a key into BST
     * @param node root node of current tree
     * @param key key of the k/v pair
     * @param value value of the k/v pair
     * @return New tree after node insertion
     */
    private TreeNode insert(TreeNode node, K key, V value) {
        if (node == null) {
            this.size++;
            return new TreeNode(key, value);
        }

        int cmp = key.compareTo(node.key);
        if (cmp == 0) {
            //update K/V pair
            node.val = value;
        } else if (cmp < 0) {
            node.left = this.insert(node.left, key, value);
        } else {
            node.right = this.insert(node.right, key, value);
        }
        return node;
    }

    @Override
    public void clear() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public V remove(K key) {
        return this.removeHelper(key, null, false);
    }

    @Override
    public V remove(K key, V value) {
        return this.removeHelper(key, value, true);
    }

    /**
     * Helper method to remove node
     * @param key
     * @param value
     * @param checkValue
     * @return
     */
    private V removeHelper(K key, V value, boolean checkValue) {

        this.removedNode = null;
        //this.root = this.deleteNodeR(this.root, key, value, checkValue);
        this.root = this.deleteNodeI(this.root, key, value, checkValue);

        if (this.removedNode == null) {
            return null;
        } else {
            this.size--;
            return this.removedNode.val;
        }
    }

    /**
     * Function to delete node from BST iterative version
     * @param rootNode current root
     * @param key key to search
     * @param value value to check
     * @param checkValue whether the finding also need to match value
     * @return new tree after deletion
     */
    private TreeNode deleteNodeI(TreeNode rootNode, K key, V value, boolean checkValue) {
        TreeNode parent = null;
        TreeNode curr = rootNode;

        // search key in BST and set its parent pointer
        while (curr != null && key.compareTo(curr.key) != 0) {
            parent = curr;
            if (key.compareTo(curr.key) < 0) {
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }

        // if not found, do nothing
        if (curr == null || (checkValue && value.compareTo(curr.val) != 0)) {
            return rootNode;
        }

        // find the node
        this.removedNode = curr;
        TreeNode successor;
        if (curr.left == null) {
            successor = curr.right;
        } else if (curr.right == null) {
            successor = curr.left;
        } else {
            // node to be deleted has two children
            TreeNode successorParent = curr;
            successor = curr.right;
            while (successor.left != null) {
                successorParent = successor;
                successor = successor.left;
            }

            if (curr != successorParent) {
                successorParent.left = successor.right;
            } else {
                successorParent.right = successor.right;
            }
            successor.left = curr.left;
            successor.right = curr.right;
        }

        if (curr == rootNode) {
            rootNode = successor;
        } else {
            if (parent.left == curr) {
                parent.left = successor;
            } else {
                parent.right = successor;
            }
        }
        curr.left = null;
        curr.right = null;
        return rootNode;
    }

    /**
     * Function to delete node from BST Recursive version
     * @param node current root
     * @param key key to search
     * @param value value to check
     * @param checkValue whether the finding also need to match value
     * @return new tree after deletion
     */
    private TreeNode deleteNodeR(TreeNode node, K key, V value, boolean checkValue) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = this.deleteNodeR(node.left, key, value, checkValue);
            return node;
        } else if (cmp > 0) {
            node.right = this.deleteNodeR(node.right, key, value, checkValue);
            return node;
        }

        if (checkValue && value.compareTo(node.val) != 0) {
            return node;
        }

        // find the node
        TreeNode newRoot;
        this.removedNode = node;
        if (node.left == null) {
            newRoot = node.right;
        } else if (node.right == null) {
            newRoot = node.left;
        } else {
            // if both children exist
            TreeNode successorParent = node;
            TreeNode successor = node.right;
            // find successor
            while (successor.left != null) {
                successorParent = successor;
                successor = successor.left;
            }

            if (successorParent != node) {
                successorParent.left = successor.right;
            } else {
                successorParent.right = successor.right;
            }
            //replace the node
            successor.left = node.left;
            successor.right = node.right;
            newRoot = successor;
        }
        node.left = null;
        node.right = null;
        return newRoot;
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    private class TreeNode {
        TreeNode left;
        TreeNode right;
        K key;
        V val;

        TreeNode(K k, V v) {
            this.key = k;
            this.val = v;
        }
    }
}
