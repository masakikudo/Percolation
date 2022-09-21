import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

// Models an N-by-N percolation system.
public class Percolation {

	// uf2 is used to avoid back-wash problem by not connecting
	// the sites in the last row with the virtual bottom site
	private WeightedQuickUnionUF uf1;
	private WeightedQuickUnionUF uf2;
	private boolean[][] sites;
	private int sizeOfGrid;
	private int openSites = 0;

	// Create an N-by-N grid, with all sites blocked.
	public Percolation(int N) {

		if (N <= 0)
			throw new IllegalArgumentException("N must be greater than 0.");

		sizeOfGrid = N;

		uf1 = new WeightedQuickUnionUF(N * N + 2);
		uf2 = new WeightedQuickUnionUF(N * N + 2);

		sites = new boolean[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++)
				sites[i][j] = false;
		}
	}

	// Open site (row, col) if it is not open already.
	public void open(int row, int col) {

		if (row < 0 || row >= sizeOfGrid || col < 0 || col >= sizeOfGrid)
			throw new IndexOutOfBoundsException("Index is outside of the grid.");

		if (!isOpen(row, col)) {
			sites[row][col] = true;
			openSites++;

			// Connect adjacent sites

			// Connect sites in first row with the virtual top site
			if (row == 0) {
				uf1.union(encode(row, col), 0);
				uf2.union(encode(row, col), 0);
			}

			else {
				// Check and connect with upper site
				if (isOpen(row - 1, col)) {
					uf1.union(encode(row, col), encode(row - 1, col));
					uf2.union(encode(row, col), encode(row - 1, col));

				}
				// Check and connect with left site, but ignore if it's in first column
				if (col != 0 && isOpen(row, col - 1)) {
					uf1.union(encode(row, col), encode(row, col - 1));
					uf2.union(encode(row, col), encode(row, col - 1));
				}
				// Check and connect with right site, but ignore if it's in last column
				if (col != sizeOfGrid - 1 && isOpen(row, col + 1)) {
					uf1.union(encode(row, col), encode(row, col + 1));
					uf2.union(encode(row, col), encode(row, col + 1));
				}
			}

			// Connect with lower site if it's not in last row
			if (row != sizeOfGrid - 1 && isOpen(row + 1, col)) {
				uf1.union(encode(row, col), encode(row + 1, col));
				uf2.union(encode(row, col), encode(row + 1, col));
			}

			// Connect sites in last row with virtual bottom site
			else if (row == sizeOfGrid - 1) {
				uf1.union(encode(row, col), sizeOfGrid * sizeOfGrid + 1);
			}
		}
	}

	// Is site (row, col) open?
	public boolean isOpen(int row, int col) {

		if (row < 0 || row >= sizeOfGrid || col < 0 || col >= sizeOfGrid)
			throw new IndexOutOfBoundsException("Index is outside of the grid.");

		return sites[row][col];
	}

	// Is site (row, col) full?
	public boolean isFull(int row, int col) {

		if (row < 0 || row >= sizeOfGrid || col < 0 || col >= sizeOfGrid)
			throw new IndexOutOfBoundsException("Index is outside of the grid.");

		return uf2.find(encode(row, col)) == uf1.find(0);
	}

	// Number of open sites.
	public int numberOfOpenSites() {
		return openSites;
	}

	// Does the system percolate?
	public boolean percolates() {
		return uf1.find(sizeOfGrid * sizeOfGrid + 1) == uf1.find(0);
	}

	// An integer ID (1...N) for site (row, col).
	private int encode(int row, int col) {
		return row * sizeOfGrid + col + 1;
	}

	// Test client. [DO NOT EDIT]
	public static void main(String[] args) {
		String filename = args[0];
		In in = new In(filename);
		int N = in.readInt();
		Percolation perc = new Percolation(N);
		while (!in.isEmpty()) {
			int i = in.readInt();
			int j = in.readInt();
			perc.open(i, j);
		}
		StdOut.println(perc.numberOfOpenSites() + " open sites");
		if (perc.percolates()) {
			StdOut.println("percolates");
		} else {
			StdOut.println("does not percolate");
		}

		// Check if site (i, j) optionally specified on the command line
		// is full.
		if (args.length == 3) {
			int i = Integer.parseInt(args[1]);
			int j = Integer.parseInt(args[2]);
			StdOut.println(perc.isFull(i, j));
		}
	}
}
