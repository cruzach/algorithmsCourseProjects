/* *****************************************************************************
 *  Name: Charlie Cruzan
 *  Date: Jan 28, 2019
 *  Description: This data type represent a sliding block puzzle board, and
 *  is to be used in conjunction with Solver.java to solve sliding block
 *  puzzles using an A* search algorithm.
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Board {

    private int[] board;
    private int n;
    private int manhattanDistance;
    private int freeTileIndex;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null) {
            throw new java.lang.IllegalArgumentException(
                    "Passed null argument to board constructor.");
        }

        n = blocks.length;
        board = new int[n * n];

        // build board array (1D to save space)
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                board[i * n + j] = blocks[i][j];
                // Save free tile index for later use
                if (blocks[i][j] == 0) {
                    freeTileIndex = i * n + j;
                }
            }
        }

        // Calculate Manhattan Distance upon construction, keep cached
        int[] currentRowCol;
        int[] goalRowCol;
        for (int i = 0; i < board.length; i++) {
            if (board[i] != 0) {
                goalRowCol = rowAndCol(board[i]
                                               - 1); // -1 accounts for 1 needing to be at index 0, so on and so forth
                currentRowCol = rowAndCol(i);
                manhattanDistance = manhattanDistance + Math.abs(goalRowCol[0] - currentRowCol[0])
                        + Math
                        .abs(goalRowCol[1] - currentRowCol[1]);
            }
        }
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of blocks out of place
    public int hamming() {
        int correctValue;
        int hammingDistance = 0;
        for (int i = 0; i < board.length; i++) {
            correctValue = i + 1;
            if (board[i] != 0 && board[i] != correctValue) {
                hammingDistance++;
            }
        }
        return hammingDistance;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return manhattanDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        if (hamming() == 0) {
            return true;
        }
        return false;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int[][] twinBoardInput = new int[n][n];
        int[] twinArr;
        int counterIndex = 0;

        if (board[0] != 0 && board[1] != 0) {
            twinArr = this.swap(0, 1);
        }
        else {
            twinArr = this.swap(n, n + 1);
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                twinBoardInput[i][j] = twinArr[counterIndex];
                counterIndex++;
            }
        }

        return new Board(twinBoardInput);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }
        if (y == null) {
            return false;
        }
        if (y.getClass() != this.getClass()) {
            return false;
        }

        Board that = (Board) y; // Must succeed based on previous if statement

        // Test each index for value equality
        return Arrays.equals(this.board, that.board);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> boardStack = new Stack<Board>();
        int[] neighborOneDimArray;

        // Add a nieghbor onto stack for each possible swap of empty tile
        int[] freeRowCol = rowAndCol(freeTileIndex); // gets [row, col] of free space

        // Check if free space is NOT in right-most column
        if (freeRowCol[1] + 1 != n) {
            neighborOneDimArray = swap(freeTileIndex + 1, freeTileIndex);
            boardStack.push(new Board(to2d(neighborOneDimArray)));
        }
        // Check if free space is NOT in left-most column
        if (freeRowCol[1] != 0) {
            neighborOneDimArray = swap(freeTileIndex - 1, freeTileIndex);
            boardStack.push(new Board(to2d(neighborOneDimArray)));
        }
        // Is moving down a row possible?
        if (freeTileIndex + n < board.length) {
            neighborOneDimArray = swap(freeTileIndex + n, freeTileIndex);
            boardStack.push(new Board(to2d(neighborOneDimArray)));
        }
        // Is moving up a row possible?
        if (freeTileIndex - n >= 0) {
            neighborOneDimArray = swap(freeTileIndex - n, freeTileIndex);
            boardStack.push(new Board(to2d(neighborOneDimArray)));
        }

        return boardStack;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", board[i * n + j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // return theoretical row and column of 2d array given the 1d array index
    private int[] rowAndCol(int index) {
        int[] position = new int[2];
        position[0] = index / n;
        position[1] = index % n;
        return position;
    }

    // returns new one dimensional board array, after swap has been done
    private int[] swap(int firstTileIndex, int secondTileIndex) {
        int[] newBoard = new int[board.length];
        System.arraycopy(board, 0, newBoard, 0, board.length);

        newBoard[secondTileIndex] = board[firstTileIndex];
        newBoard[firstTileIndex] = board[secondTileIndex];

        return newBoard;
    }

    // Construct and return twin board 2d array from 1d board array
    private int[][] to2d(int[] oneDimBoard) {
        int[][] twoDimBoard = new int[n][n];
        int counterIndex = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                twoDimBoard[i][j] = oneDimBoard[counterIndex];
                counterIndex++;
            }
        }
        return twoDimBoard;
    }

    // unit tests (not graded)
    public static void main(String[] args) {

        int[][] input = { { 0, 1, 3 }, { 4, 2, 5 }, { 7, 8, 6 } };
        Board test = new Board(input);

        int[][] input2 = { { 0, 2, 3 }, { 4, 5, 6 }, { 7, 8, 1 } };
        Board test2 = new Board(input2);

        StdOut.println(test);
        StdOut.println(test.twin());
        StdOut.println(test.twin());
        // StdOut.println(test2);

        /*
        for (Board neighbor : test.neighbors()) {
            StdOut.println(neighbor);
        }*/
        // Tests
        // StdOut.println(test.hamming());
        // StdOut.println(test.manhattan());
        // StdOut.println(test.isGoal());
        // StdOut.println(test.equals(test2));


    }
}
