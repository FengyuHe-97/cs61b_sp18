package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int N;
    private int openNumber = 0;
    private String[][] model;
    private WeightedQuickUnionUF wqu;
    private int TOP;


    /**
     *  create N-by-N grid, with all sites initially blocked
     * @param N N-by-N grid
     */
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        this.N = N;
        model = new String[N][N];
        wqu = new WeightedQuickUnionUF(N * N + 1);
        TOP = N * N;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                model[i][j] = "Blocked";
            }
        }
    }

    /**
     *  open the site (row, col) if it is not open already
     */
    public void open(int row, int col) {
        validate(row, col);
        if (model[row][col].equals("Blocked")) {
            model[row][col] = "Open";
            openNumber += 1;

            if (row == 0) { //up
                wqu.union(TOP, xyTo1D(row, col));
            } else if (model[row - 1][col].equals("Open")) {
                wqu.union(xyTo1D(row, col), xyTo1D(row - 1, col));
            }
            if (row != N - 1) { //down
                if (model[row + 1][col].equals("Open")) {
                    wqu.union(xyTo1D(row, col), xyTo1D(row + 1, col));
                }
            }
            if (col != 0) { //left
                if (model[row][col - 1].equals("Open")) {
                    wqu.union(xyTo1D(row, col), xyTo1D(row, col - 1));
                }
            }
            if (col != N - 1) { //right
                if (model[row][col + 1].equals("Open")) {
                    wqu.union(xyTo1D(row, col), xyTo1D(row, col + 1));
                }
            }
        }
    }

    /**
     * transf row and col to a number
     */
    private int xyTo1D(int row, int col) {
        return row * N + col;
    }

    private void validate(int row, int col) {
        if (!(row >= 0 && row <= N - 1 && col >= 0 && col <= N - 1)) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     *  is the site (row, col) open?
     */
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return model[row][col].equals("Open");
    }

    /**
     *  is the site (row, col) full?
     */
    public boolean isFull(int row, int col) {
        validate(row, col);
        if (!isOpen(row, col)) {
            return false;
        }
        return wqu.find(TOP) == wqu.find(xyTo1D(row, col));
    }

    /**
     *  number of open sites
     */
    public int numberOfOpenSites() {
        return openNumber;
    }

    /**
     * does the system percolate?
     */
    public boolean percolates()  {
        for (int i = 0; i < N; i += 1) {
            if (isFull(N - 1,  i)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {

    }  // use for unit testing (not required)


}
