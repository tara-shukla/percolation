import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {

    // array to store results of trials
    private double[] results;
    // number of trials
    private int trials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        // throw exeption if grid size or trials <= 0
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("grid size and trial # should be > 0");
        }
        this.trials = trials;
        results = new double[trials];

        // monte carlo experiment-- each loop populates results [] with one trial
        for (int i = 0; i < trials; i++) {
            Percolation percolate = new Percolation(n);
            int sitesOpen = percolate.numberOfOpenSites();
            while (!percolate.percolates()) {
                int randomX = StdRandom.uniform(0, n);
                int randomY = StdRandom.uniform(0, n);
                // if the site is already open, keep trying random points until one is closed
                while (percolate.isOpen(randomX, randomY)) {
                    randomX = StdRandom.uniform(0, n);
                    randomY = StdRandom.uniform(0, n);
                }
                // open the site
                percolate.open(randomX, randomY);
                sitesOpen++;
            }

            // if the system percolates, add the ratio to results
            results[i] = ((double) sitesOpen) / ((double) (n * n));
            sitesOpen = 0;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        double lowCon = mean() - ((1.96 * stddev()) / Math.sqrt((double) trials));
        return lowCon;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        double highCon = mean() + ((1.96 * stddev()) / Math.sqrt((double) trials));
        return highCon;
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        Stopwatch watch = new Stopwatch();
        PercolationStats tester = new PercolationStats(n, t);
        StdOut.print("mean()            = ");
        StdOut.printf("%5.3f%n", tester.mean());
        StdOut.print("stddev()          = ");
        StdOut.printf("%5.3f%n", tester.stddev());
        StdOut.print("confidenceLow()   = ");
        StdOut.printf("%5.3f%n", tester.confidenceLow());
        StdOut.print("confidenceHigh()  = ");
        StdOut.printf("%5.3f%n", tester.confidenceHigh());
        StdOut.print("elapsed time      = ");
        StdOut.printf("%5.3f%n", watch.elapsedTime());
    }
}
