import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

// Estimates percolation threshold for an N-by-N percolation system.
public class PercolationStats {

	private double[] x;

	// Perform T independent experiments (Monte Carlo simulations) on an
	// N-by-N grid.
	public PercolationStats(int N, int T) {

		if (N <= 0 || T <= 0)
			throw new IllegalArgumentException("N and T must be greater than 0.");

		x = new double[T];

		for (int i = 0; i < T; i++) {

			Percolation p = new Percolation(N);

			while (!p.percolates())
				p.open(StdRandom.uniform(N), StdRandom.uniform(N));

			x[i] = (double) p.numberOfOpenSites() / (N * N);
		}
	}

	// Sample mean of percolation threshold.
	public double mean() {
		return StdStats.mean(x);
	}

	// Sample standard deviation of percolation threshold.
	public double stddev() {
		return StdStats.stddev(x);
	}

	// Low endpoint of the 95% confidence interval.
	public double confidenceLow() {
		return StdStats.mean(x) - 1.96 * StdStats.stddev(x) / Math.sqrt(x.length);
	}

	// High endpoint of the 95% confidence interval.
	public double confidenceHigh() {
		return StdStats.mean(x) + 1.96 * StdStats.stddev(x) / Math.sqrt(x.length);
	}

	// Test client. [DO NOT EDIT]
	public static void main(String[] args) {
		int N = Integer.parseInt(args[0]);
		int T = Integer.parseInt(args[1]);
		PercolationStats stats = new PercolationStats(N, T);
		StdOut.printf("mean           = %f\n", stats.mean());
		StdOut.printf("stddev         = %f\n", stats.stddev());
		StdOut.printf("confidenceLow  = %f\n", stats.confidenceLow());
		StdOut.printf("confidenceHigh = %f\n", stats.confidenceHigh());
	}
}
