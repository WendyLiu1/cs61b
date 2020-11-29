public class UnionFind {

    private int[] unionArray;

    /* Creates a UnionFind data structure holding n vertices. Initially, all
       vertices are in disjoint sets. */
    public UnionFind(int n) {
        if (n <=0 ) {
            throw new IllegalArgumentException("Input must be larger than 0");
        }
        this.unionArray = new int[n];
        for (int i = 0; i < n; i += 1) {
            this.unionArray[i] = -1;
        }
    }

    /* Throws an exception if v1 is not a valid index. */
    private void validate(int vertex) {
        if (vertex >= this.unionArray.length || vertex < 0) {
            throw new IllegalArgumentException("Input vertex is not valid");
        }
    }

    /* Returns the size of the set v1 belongs to. */
    public int sizeOf(int v1) {
        int rootIndex = this.find(v1);
        return Math.abs(this.unionArray[rootIndex]);
    }

    /* Returns the parent of v1. If v1 is the root of a tree, returns the
       negative size of the tree for which v1 is the root. */
    public int parent(int v1) {
        this.validate(v1);
        int parent = this.unionArray[v1];
        return parent;
    }

    /* Returns true if nodes v1 and v2 are connected. */
    public boolean connected(int v1, int v2) {
        int root1 = this.find(v1);
        int root2 = this.find(v2);
        return root1 == root2;
    }

    /* Connects two elements v1 and v2 together. v1 and v2 can be any valid 
       elements, and a union-by-size heuristic is used. If the sizes of the sets
       are equal, tie break by connecting v1's root to v2's root. Unioning a 
       vertex with itself or vertices that are already connected should not 
       change the sets but may alter the internal structure of the data. */
    public void union(int v1, int v2) {
        int root1 = this.find(v1);
        int root2 = this.find(v2);
        if (root1 == root2) {
            return;
        }

        int size1 = this.unionArray[root1];
        int size2 = this.unionArray[root2];
        if (size1 >= size2) {
            this.unionArray[root1] = root2;
            this.unionArray[root2] = size1 + size2;
        }
        else {
            this.unionArray[root2] = root1;
            this.unionArray[root1] = size1 + size2;
        }
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. */
    public int find(int vertex) {
        int root = vertex;
        while (this.parent(root) >= 0) {
            root = this.parent(root);
        }
        // path compression
        while (vertex != root) {
            int parent = this.parent(vertex);
            this.unionArray[vertex] = root;
            vertex = parent;
        }
        return root;
    }
}
