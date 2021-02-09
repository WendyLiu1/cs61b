package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int n;
    private int[][] grid;
    private WeightedQuickUnionUF uf;
    private int numOpenSite;
    private int topIdx;
    private boolean perculates;
    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N must be larger than 0");
        }
        this.n = N;
        // the last one is used to indicate it's top row
        this.uf = new WeightedQuickUnionUF(N * N + 1);
        this.topIdx = N * N;
        this.grid = new int[N][N];
        this.numOpenSite = 0;
        this.perculates = false;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < n; j++) {
                this.grid[i][j] = 0;
            }
        }
    }

    /**
     * open the site (row, col) if it is not open already
     * @param row
     * @param col
     */
    public void open(int row, int col) {
        this.inputCheck(row);
        this.inputCheck(col);
        if (this.grid[row][col] == 0) {
            int idx = this.convert2dTo1d(row, col);
            this.grid[row][col] = 1;
            if (row > 0 && this.isOpen(row - 1, col)) {
                this.uf.union(idx, this.convert2dTo1d(row - 1, col));
            }
            if (row < this.n - 1 && this.isOpen(row + 1, col)) {
                this.uf.union(idx, this.convert2dTo1d(row + 1, col));
            }
            if (col > 0 && this.isOpen(row, col - 1)) {
                this.uf.union(idx, this.convert2dTo1d(row, col - 1));
            }
            if (col < this.n - 1 && this.isOpen(row, col + 1)) {
                this.uf.union(idx, this.convert2dTo1d(row, col + 1));
            }
            if (row == 0) {
                this.uf.union(idx, this.topIdx);
            }
            if (!this.perculates && row == this.n - 1) {
                this.perculates = this.uf.connected(idx, this.topIdx);
            }
            this.numOpenSite++;
        }
    }

    /**
     * is the site (row, col) open?
     * @param row
     * @param col
     * @return
     */
    public boolean isOpen(int row, int col) {
        this.inputCheck(row);
        this.inputCheck(col);
        return this.grid[row][col] == 1;
    }

    /**
     * is the site (row, col) full?
     * @param row
     * @param col
     * @return
     */
    public boolean isFull(int row, int col) {
        this.inputCheck(row);
        this.inputCheck(col);
        return this.uf.connected(this.convert2dTo1d(row, col), this.topIdx);
    }

    /**
     * @return number of open sites
     */
    public int numberOfOpenSites() {
        return this.numOpenSite;
    }

    /**
     * does the system percolate?
     * @return
     */
    public boolean percolates() {
        return this.perculates;
    }

    /**
     * check if the input is valid or not
     */
    private void inputCheck(int idx) {
        if (idx < 0 || idx >= this.n) {
            throw new IndexOutOfBoundsException("input is out of bound");
        }
    }

    /**
     * Convert 2d index to 1d
     * @param row row number
     * @param col col number
     * @return return 1d index
     */
    private int convert2dTo1d(int row, int col) {
        return col + row * this.n;
    }
}
