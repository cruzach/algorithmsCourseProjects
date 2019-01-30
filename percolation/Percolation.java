import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/* *****************************************************************************
 *  Name: Charlie Cruzan
 *  Date: 1/10/19
 *  Description:
 **************************************************************************** */
public class Percolation {

    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF ufFull;
    private byte[] openOrClosed;
    private int length;
    private int openCount;
    private int virtualTop;
    private int virtualBottom;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException(
                    "Grid size must be larger than 0.");
        }

        uf = new WeightedQuickUnionUF((n * n) + 2);
        ufFull = new WeightedQuickUnionUF(
                (n * n) + 2); // extra union ADT to track fullness for visualization
        virtualTop = 0;
        virtualBottom = (n * n) + 1;
        openOrClosed = new byte[(n * n) + 1];
        length = n;
        openCount = 0;

        // initiate all sites to 0 (closed)
        for (int i = 1; i < openOrClosed.length; i++) {
            openOrClosed[i] = 0;
        }
    }

    private void validate(int index) {
        if (index <= 0 || index > openOrClosed.length) {
            throw new IllegalArgumentException(
                    "index " + index + " is not between 1 and " + (openOrClosed.length - 1));
        }
    }

    // return index correlating to provided x and y coordinate pair
    private int xyTo1D(int x, int y) {
        if (x <= 0 || y <= 0 || x > length || y > length) {
            throw new IllegalArgumentException("Index is out of bounds.");
        }
        int index = ((x - 1) * length) + y;
        validate(index);
        return index;
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isOpen(row, col)) {
            // open site at that index
            int index = xyTo1D(row, col);
            openOrClosed[index] = 1;
            openCount++;

            // connect top row to virtual top, or bottom row to virtual bottom upon opening
            if (row == 1) {
                uf.union(virtualTop, index);
                ufFull.union(virtualTop, index);
            }

            // DO NOT CONNECT ufFull TO VIRTUAL BOTTOM, THAT IS WOULD RESULT IN BACKWASH
            if (row == length) {
                uf.union(virtualBottom, index);
            }


            // connect with any surrounding open sites
            if (row > 1) {
                if (isOpen(row - 1, col)) {
                    uf.union(index, xyTo1D(row - 1, col));
                    ufFull.union(index, xyTo1D(row - 1, col));
                }
            }
            if (col > 1) {
                if (isOpen(row, col - 1)) {
                    uf.union(index, xyTo1D(row, col - 1));
                    ufFull.union(index, xyTo1D(row, col - 1));
                }
            }
            if (row < length) {
                if (isOpen(row + 1, col)) {
                    uf.union(index, xyTo1D(row + 1, col));
                    ufFull.union(index, xyTo1D(row + 1, col));
                }
            }
            if (col < length) {
                if (isOpen(row, col + 1)) {
                    uf.union(index, xyTo1D(row, col + 1));
                    ufFull.union(index, xyTo1D(row, col + 1));
                }
            }
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        int index = xyTo1D(row, col);
        validate(index);
        if (openOrClosed[index] == 1) {
            return true;
        }
        return false;
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        int index = xyTo1D(row, col);
        validate(index);
        // check ufFull for fullness, since bottom row open sites are NOT automatically connected to virtual bottom in ufFull
        if (ufFull.connected(index, virtualTop)) {
            return true;
        }
        return false;
    }

    // number of open sites
    public int numberOfOpenSites() {
        return openCount;
    }

    // does the system percolate?
    public boolean percolates() {
        if (uf.connected(virtualTop, virtualBottom)) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Percolation perc = new Percolation(2);
        perc.open(1, 1);
        perc.open(2, 1);
        StdOut.println(perc.percolates());
    }
}
