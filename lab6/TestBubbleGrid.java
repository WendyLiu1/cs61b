import org.junit.Test;
import static org.junit.Assert.*;

public class TestBubbleGrid {
    @Test
    public void testOutput() {
        int[][] grid = {{1, 1, 0}, {1, 0, 0}, {1, 1, 0}, {1, 1, 1}};
        int[][] darts = {{2, 2}, {2, 0}};
        int[] expected = {0, 4};
        this.validate(grid, darts, expected);

        int[][] grid1 = {{1, 0, 0, 0}, {1, 1, 1, 0}};
        int[][] darts1 = {{1, 0}};
        int[] expected1 = {2};
        this.validate(grid1, darts1, expected1);

        int[][] grid2 = {{1, 0, 0, 0}, {1, 1, 0, 0}};
        int[][] darts2 = {{1, 1}, {1, 0}};
        int[] expected2 = {0, 0};
        this.validate(grid2, darts2, expected2);
    }

    private void validate(int[][] grid, int[][] darts, int[] expected) {
        BubbleGrid bubbleGrid = new BubbleGrid(grid);
        assertArrayEquals(expected, bubbleGrid.popBubbles(darts));
    }
}

