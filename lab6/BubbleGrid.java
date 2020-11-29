public class BubbleGrid {
    private final int[][] bubbleGrid;
    public BubbleGrid(int[][] grid) {
        this.bubbleGrid = grid;
    }

    /**
     * @param darts and array of 2-element arrays representing the grid positions
     *              (in [row, col] format) at which darts are thrown in sequence(i.e.
     *              a dart is thrown at position darts[t] at time t).
     * @return an array where i-th element is the number of bubbles that fall after
     *              i-th dart is thrown(popped bubbles do not fall)
     */
    int[] popBubbles(int[][] darts) {
        if (darts == null || darts.length < 1) {
            return null;
        }

        int[] res = new int[darts.length];
        int numRows = this.bubbleGrid.length;
        int numCols = this.bubbleGrid[0].length;

        // used to check four adjacent cells
        int[] dr = {1, 0, -1, 0};
        int[] dc = {0, 1, 0, -1};

        // initialize matrix afterPop, represents the grid
        // after all darts are shot
        int[][] afterPop = new int[numRows][numCols];
        for (int row = 0; row < numRows; row += 1) {
            afterPop[row] = this.bubbleGrid[row].clone();
        }
        for (int[] dart: darts) {
            afterPop[dart[0]][dart[1]] = 0;
        }

        // initialize disjoint set structure, the last element is used to
        // keep track of the top row
        int topRowIdx = numRows * numCols;
        UnionFind djSet = new UnionFind(numCols * numRows + 1);
        for (int row = 0; row < numRows; row += 1) {
            for (int col = 0; col < numCols; col += 1) {
                if (afterPop[row][col] == 1) {
                    int idx = this.convertIdx(row, col);
                    // if is top row, connect to last element;
                    if (row == 0) {
                        djSet.union(idx, topRowIdx);
                    }
                    // connect to adjacent elements
                    if (row > 0 && afterPop[row - 1][col] == 1) {
                        djSet.union(idx, this.convertIdx(row - 1, col));
                    }
                    if (col > 0 && afterPop[row][col - 1] == 1) {
                        djSet.union(idx, this.convertIdx(row, col - 1));
                    }
                }
            }
        }

        int time = darts.length - 1;
        // go reverse order to reconnect
        while (time >= 0) {
            int row = darts[time][0];
            int col = darts[time][1];
            // the bubble is popped in corresponding position
            if (this.bubbleGrid[row][col] == 1) {
                // exclude the index element, i.e. numCols * numRows
                // Everything connected to top row could used to determine how many
                // are fallen after dart is shot
                int preTopRowSize = djSet.sizeOf(numCols * numRows) - 1;
                int idx = this.convertIdx(row, col);
                if (row == 0) {
                    djSet.union(idx, topRowIdx);
                }
                // current block could be connected to top row through
                // adjacent cells
                for (int dir = 0; dir < 4; dir += 1) {
                    int adjRow = row + dr[dir];
                    int adjCol = col + dc[dir];
                    if (adjRow >= 0 && adjRow < numRows
                            && adjCol >= 0 && adjCol < numCols
                            && afterPop[adjRow][adjCol] == 1) {
                        djSet.union(idx, this.convertIdx(adjRow, adjCol));
                    }
                }
                int afterTopRowSize = djSet.sizeOf(numCols * numRows) - 1;
                // restore the popped element at time
                afterPop[row][col] = 1;
                // the extra minus one is used to exclude the pop action
                res[time] = Math.max(0, afterTopRowSize - preTopRowSize - 1);
            }
            time--;
        }
        return res;
    }

    /**
     * Convert 2d array index to 1d
     * @param row row number
     * @param col col number
     * @return 1d index
     */
    int convertIdx(int row, int col) {
        return row * this.bubbleGrid[0].length + col;
    }
}
