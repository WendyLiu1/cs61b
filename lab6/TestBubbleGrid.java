import org.junit.Test;
import static org.junit.Assert.*;

public class TestBubbleGrid {
    @Test
    public void testOutput() {
        int[][] bg = {{1, 1, 0}, {1, 0, 0}, {1, 1, 0}, {1, 1, 1}};
        BubbleGrid BG = new BubbleGrid(bg);
        int[][] darts = {{2, 2}, {2, 0}};
        int[] result = BG.popBubbles(darts);
        int[] expected = {0, 4};
        assertArrayEquals(expected, result);

        int[][] bg1 = {{1, 0, 0, 0}, {1, 1, 1, 0}};
        BubbleGrid BG1 = new BubbleGrid(bg1);
        int[][] darts1 = {{1, 0}};
        int[] result1 = BG1.popBubbles(darts1);
        int[] expected1 = {2};
        assertArrayEquals(expected1, result1);

        int[][] bg2 = {{1, 0, 0, 0}, {1, 1, 0, 0}};
        BubbleGrid BG2 = new BubbleGrid(bg2);
        int[][] darts2 = {{1, 1}, {1, 0}};
        int[] result2 = BG2.popBubbles(darts2);
        int[] expected2 = {0, 0};
        assertArrayEquals(expected2, result2);
    }
}

