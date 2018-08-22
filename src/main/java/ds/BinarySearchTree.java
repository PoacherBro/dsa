package ds;

public class BinarySearchTree<Key extends Comparable<Key>, Value> {
    private class Node {
        private Key key;
        private Value val;
        private Node lef, right;
        private int N;  // count of sub-nodes under this node (as root node)

        public Node(Key key, Value val, int N) {
            this.key = key;
            this.val = val;
            this.N = N;
        }
    }

    private Node root;

    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (null == x)
            return 0;
        else
            return x.N;
    }

    public Value get(Key key) {
        return get(root, key);
    }

    /**
     * Find matched Node whose key is same under the root node 'x',
     * and return its value. If not found, return null
     * @param x as root node
     * @param key searched key
     * @return value of matched node, nullable if not found
     */
    private Value get(Node x, Key key) {
        if (null == x)
            return null;

        int cmp = key.compareTo(x.key);
        if (cmp < 0)
            return get(x.lef, key);
        else if (cmp > 0)
            return get(x.right, key);
        else
            return x.val;
    }

    /**
     * Search all nodes based on key,
     * if found, update the value of that node;
     * otherwise, create new one.
     * @param key Key
     * @param val Value
     */
    public void put(Key key, Value val) {
        root = put(root, key, val);
    }

    private Node put(Node x, Key key, Value val) {
        if (null == x)
            return new Node(key, val, 1);

        int cmp = key.compareTo(x.key);
        if (cmp < 0)
            x.lef = put(x.lef, key, val);
        else if (cmp > 0)
            x.right = put(x.right, key, val);
        else
            x.val = val;

        x.N = size(x.lef) + size(x.right) + 1;
        return x;
    }
}







