/* *****************************************************************************
 *  Name: Charlie Cruzan
 *  Date: Jan 28, 2019
 *  Description: This program uses an A* search algorithm to illustrate the
 *  specific moves required to solve a sliding block puzzle, given an initial
 *  block set up. If the puzzle is unsolvable, the program will return a
 *  message stating it is unsolvable.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private SearchNode currentSearchNode;

    private static class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int moves;
        private SearchNode previousNode;
        private int manhattanDistance;

        public SearchNode(Board board, int moves, SearchNode previousNode) {
            this.board = board;
            this.moves = moves;
            this.previousNode = previousNode;

            manhattanDistance = this.board.manhattan();
        }

        @Override
        public int compareTo(SearchNode that) {
            return ((this.manhattanDistance + this.moves) - (that.manhattanDistance + that.moves));
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new java.lang.IllegalArgumentException(
                    "Passed null argument to Solver constructor.");
        }
        // Run two searches, one for initial, AND one for twin of initial
        // to check for solvability (and prevent infinite loop)

        // Initialize initial Board variables
        MinPQ<SearchNode> minPq = new MinPQ<SearchNode>(SearchNode::compareTo);
        currentSearchNode = new SearchNode(initial, 0, null);
        minPq.insert(currentSearchNode);

        // Initialize twin Board variables
        MinPQ<SearchNode> twinMinPq = new MinPQ<SearchNode>(SearchNode::compareTo);
        SearchNode twinCurrentSearchNode = new SearchNode(initial.twin(), 0, null);
        twinMinPq.insert(twinCurrentSearchNode);

        int n = initial.dimension();

        while (!currentSearchNode.board.isGoal() && !twinCurrentSearchNode.board.isGoal()) {
            // Initial Board Priority Queue Work
            for (Board neighbor : currentSearchNode.board.neighbors()) {
                // DO NOT ADD NEIGHBOR IF IT IS A REPEAT OF PREVIOUS BOARD
                if (currentSearchNode.previousNode == null || !neighbor
                        .equals(currentSearchNode.previousNode.board)) {
                    minPq.insert(
                            new SearchNode(neighbor, currentSearchNode.moves + 1,
                                           currentSearchNode));
                }
            }
            currentSearchNode = minPq.delMin();

            // Twin Board Priority Queue Work
            for (Board neighbor : twinCurrentSearchNode.board.neighbors()) {
                // DO NOT ADD NEIGHBOR IF IT IS A REPEAT OF PREVIOUS BOARD
                if (twinCurrentSearchNode.previousNode == null || !neighbor
                        .equals(twinCurrentSearchNode.previousNode.board)) {
                    twinMinPq.insert(new SearchNode(neighbor, twinCurrentSearchNode.moves + 1,
                                                    twinCurrentSearchNode));
                }
            }
            twinCurrentSearchNode = twinMinPq.delMin();
            // if twin reaches goal node, original board is unsolvable
            // and break out of while loop
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return currentSearchNode.board.isGoal();
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable()) {
            return currentSearchNode.moves;
        }
        return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        // check if unsolvable
        if (isSolvable()) {
            SearchNode temp = currentSearchNode;
            Stack<Board> boardStack = new Stack<Board>();
            while (currentSearchNode != null) {
                boardStack.push(currentSearchNode.board);
                currentSearchNode = currentSearchNode.previousNode;
            }
            currentSearchNode = temp;
            return boardStack;
        }
        return null;
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
