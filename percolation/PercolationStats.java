import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
/* *****************************************************************************
 *  Name: Charlie Cruzan
 *  Date: 1/10/19
 *  Description:
 **************************************************************************** */

public class PercolationStats {

    private double[] percThreshholdArray;
    private double avg;
    private double stddev;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {

        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Arguments both must be greater than zero.");
        }

        percThreshholdArray = new double[trials];

        for (int t = 0; t < trials; t++) {

            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int i = StdRandom.uniform(1, n + 1);
                int j = StdRandom.uniform(1, n + 1);
                perc.open(i, j);
            }
            percThreshholdArray[t] = (double) perc.numberOfOpenSites() / (n * n);
        }
        avg = StdStats.mean(percThreshholdArray);
        stddev = StdStats.stddev(percThreshholdArray);
    }


    // sample mean of percolation threshold
    public double mean() {
        return avg;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return (avg - ((1.96 * stddev) / Math.sqrt(percThreshholdArray.length)));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return (avg + ((1.96 * stddev) / Math.sqrt(percThreshholdArray.length)));
    }

    // test client
    public static void main(String[] args) {

        int inputN = Integer.parseInt(args[0]);
        int inputTrials = Integer.parseInt(args[1]);

        PercolationStats percStats = new PercolationStats(inputN, inputTrials);

        StdOut.printf("mean = %f\n", percStats.mean());
        StdOut.printf("stddev = %f\n", percStats.stddev());
        StdOut.println("95% Confidence Interval = [" + percStats.confidenceLo() + ", " +
                               percStats.confidenceHi() + "]");
    }
}
