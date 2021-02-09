package hw2;
import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private double[] results;
    /**
     * Perform T independent experiments on an N-by-N grid
     * @param N
     * @param T
     * @param pf
     */
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("Illegal input");
        }
        this.results = new double[T];
        for (int i = 0; i < T; i++) {
            this.results[i] = percolateSimulation(N, pf);
        }
    }

    /**
     * Perform 1 run on N-by-N grid
     * @param N
     * @param pf
     * @return
     */
    private double percolateSimulation(int N, PercolationFactory pf) {
        Percolation p = pf.make(N);
        int numGrid = N * N;
        while (!p.percolates()) {
            int idx = StdRandom.uniform(numGrid);
            int row = idx / N;
            int col = idx % N;
            p.open(row, col);
        }
        return (double) p.numberOfOpenSites() / (double) N;
    }


    public double mean() {
        return StdStats.mean(this.results);
    }

    public double stddev() {
        return StdStats.stddev(this.results);
    }

    public double confidenceLow() {
        return this.mean() - 1.96 * this.stddev() / Math.sqrt(this.results.length);
    }

    public double confidenceHigh() {
        return this.mean() + 1.96 * this.stddev() / Math.sqrt(this.results.length);
    }
}
