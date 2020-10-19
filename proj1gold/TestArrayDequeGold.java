import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Random;

public class TestArrayDequeGold {

    @Test(timeout = 1000)
    public void testArrayDeque() {

        StudentArrayDeque<Integer> std = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> sol = new ArrayDequeSolution<>();
        Random randomGenerator = new Random();
        while (true) {
            double numberBetweenZeroAndOne = StdRandom.uniform();
            int input = randomGenerator.nextInt(1000);
            if (numberBetweenZeroAndOne < 0.25) {
                testAddFirst(sol, std, input);
            } else if (numberBetweenZeroAndOne < 0.5) {
                testAddLast(sol, std, input);
            } else if (numberBetweenZeroAndOne < 0.75) {
                testRemoveFirst(sol, std);
            } else {
                testRemoveLast(sol, std);
            }
        }
    }

    /**
     * Test addFirst method
     * @param sol solution implementation
     * @param std student implementation
     * @param input input number
     */
    private void testAddFirst(ArrayDequeSolution<Integer> sol, StudentArrayDeque<Integer> std,
                              int input) {
        sol.addFirst(input);
        std.addFirst(input);
        assertEquals(sol.size(), std.size());
        assertFalse(std.isEmpty());
        assertEquals(sol.get(0), std.get(0));
    }

    /**
     * Test addLast method
     * @param sol solution implementation
     * @param std student implementation
     * @param input input number
     */
    private void testAddLast(ArrayDequeSolution<Integer> sol, StudentArrayDeque<Integer> std,
                             int input) {
        sol.addLast(input);
        std.addLast(input);
        assertEquals(sol.size(), std.size());
        assertFalse(std.isEmpty());
        assertEquals(sol.get(sol.size() - 1), std.get(sol.size() - 1));
    }
    /**
     * Test removeFirst method
     * @param sol solution implementation
     * @param std student implementation
     *
     */
    private void testRemoveFirst(ArrayDequeSolution<Integer> sol, StudentArrayDeque<Integer> std) {
        assertEquals(sol.size(), std.size());
        if (!std.isEmpty()) {
            assertEquals(sol.removeFirst(), std.removeFirst());
            assertEquals(sol.size(), std.size());
        }
    }
    /**
     * Test removeLast method
     * @param sol solution implementation
     * @param std student implementation
     */
    private void testRemoveLast(ArrayDequeSolution<Integer> sol, StudentArrayDeque<Integer> std) {
        assertEquals(sol.size(), std.size());
        if (!std.isEmpty()) {
            assertEquals(sol.removeLast(), std.removeLast());
            assertEquals(sol.size(), std.size());
        }
    }
}
