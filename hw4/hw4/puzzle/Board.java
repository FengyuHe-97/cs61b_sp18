package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;


public class Board implements WorldState{

    private static final int BLANK = 0;
    private int N;
    int[][]board;

    /**
     * Constructs a board from an N-by-N array of tiles where
     * tiles[i][j] = tile at row i, column j
     */
    public Board(int[][] tiles){
        N = tiles.length;
        this.board = new int[N][N];
        for (int r = 0; r < N; r += 1) {
            System.arraycopy(tiles[r], 0, this.board[r], 0, N);
        }
    }

    public int tileAt(int i, int j) {
        if (i < 0 || i >= N || j < 0 || j >= N) {
            throw new java.lang.IndexOutOfBoundsException("Out of boundary");
        }

        return board[i][j];
    }

    public int size() {
        return N;
    }


    /**
     * Returns neighbors of this board.
     * SPOILERZ: This is the answer.
     */
    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == BLANK) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = BLANK;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = BLANK;
                }
            }
        }
        return neighbors;
    }

    /**
     * Hamming estimate described below
     */
    public int hamming() {
        int distance = 0, expectedValue = 1;
        for(int r = 0; r < size(); r += 1) {
            for(int c = 0; c < size(); c += 1) {
                if(expectedValue == N * N) {
                    break;
                }else if(tileAt(r, c) != expectedValue) {
                    distance +=1;
                }
                expectedValue += 1;
            }
        }
        return distance;
    }

    /**
     * Manhattan estimate described below
     */
    public int manhattan() {
        int distance = 0;
        for (int r = 0; r < size(); r++) {
            for (int c = 0; c < size(); c++) {
                int actualValue = tileAt(r, c);
                if (actualValue == 0) {
                    continue;
                }
                int expectedRow = (actualValue - 1) / N;
                int expectedColumn = (actualValue - 1) % N;
                distance += Math.abs(expectedRow - r) + Math.abs(expectedColumn - c);
            }
        }
        return distance;
    }

    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    /**
     * Returns true if this board's tile values are the same
     * position as y's
     */
    @Override
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null || this.getClass() != y.getClass()) {
            return false;
        }
        Board other = (Board) y;
        if (size() != other.size()) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                if (board[i][j] != other.board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Returns the string representation of the board.
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
