import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // n = size of one side of nxn array
    private int n;
    // counts number of sites that are open
    private int openSites;
    // size of system (=n*n)
    private int systemSize;
    // 1-d array to track open/closed for each element on grid
    private boolean[] grid;
    // weighted quick union object to union/find the elements
    private WeightedQuickUnionUF uf;
    // virtual topsite
    private int virtualTop;
    // virtual bottomsite
    private int virtualBottom;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        // init class variables
        this.n = n;
        systemSize = n * n;
        grid = new boolean[systemSize];
        // virtual top as second-last element in uf
        virtualTop = n * n;
        // virtual bottom as last element in uf
        virtualBottom = n * n + 1;
        uf = new WeightedQuickUnionUF(n * n + 2);
        // throw exception if n is out of bounds
        if (n <= 0) throw new IllegalArgumentException("n must be greater than 0.");
        // fill the grid with all closed (false) elements
        for (int i = 0; i < systemSize; i++) grid[i] = false;
    }

    // converts 2d grid to 1d UF index
    private int convert(int row, int col) {
        int converted = (row * n) + col;
        return converted;
    }

    // check that row and col are in bounds; throw illegalargumentexception if not
    private void illegalCheck(int row, int col) {
        if (row > n || col > n || row < 0 || col < 0) {
            throw new IllegalArgumentException("out of bounds!");
        }
    }

    // union an open element with open elements around it
    private void connectAround(int converted, int row, int col) {
        if (isOpen(row, col)) {
            uf.union(converted, convert(row, col));
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        illegalCheck(row, col);
        // if row is not already open
        if (!isOpen(row, col)) {
            openSites++;
            int converted = convert(row, col);
            // set the element open on the grid
            grid[converted] = true;
            // merge the element with nearby open elements with uf
            if (row != n - 1) connectAround(converted, row + 1, col);
            if (row != 0) connectAround(converted, row - 1, col);
            if (col != n - 1) connectAround(converted, row, col + 1);
            if (col != 0) connectAround(converted, row, col - 1);

            // if the element is on the top row, merge it with the virtual top site
            if (row == 0) {
                uf.union(converted, virtualTop);
            }
            // if the element is on the bottom row, merge it with the virtual bottom site
            if (row == n - 1) {
                uf.union(converted, virtualBottom);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        illegalCheck(row, col);
        return grid[convert(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        illegalCheck(row, col);
        int converted = convert(row, col);
        return (uf.find(converted) == uf.find(virtualTop));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        if (uf.find(virtualTop) == uf.find(virtualBottom)) return true;
        else return false;
    }

    // print elements for unit testing
    private static void printGrid(boolean[] array, int n) {
        for (int i = 0; i < array.length; i++) {
            if (i % n == 0) StdOut.println();
            StdOut.print(i + "-" + array[i] + "  ");
        }
        StdOut.println();
    }

    // unit testing (required)
    public static void main(String[] args) {
        Percolation tester = new Percolation(10);
        StdOut.println(tester.convert(1, 0));
        tester.open(0, 4);
        tester.open(1, 4);
        printGrid(tester.grid, tester.n);
        StdOut.println("percolates: " + tester.percolates());
        StdOut.println("(0,4) is open: " + tester.isOpen(0, 4));
        StdOut.println("(1,4) is full: " + tester.isFull(1, 4));
        StdOut.println("open sites = " + tester.numberOfOpenSites());
    }
}


