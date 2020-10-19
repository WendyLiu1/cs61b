import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Random;

public class TestArrayDequeGold {

    @Test(timeout = 1000)
    public void testArrayDeque() {

        StudentArrayDeque<Integer> std = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> sol = new ArrayDequeSolution<>();
        StringBuilder callTraces = new StringBuilder("\n");

        Random randomGenerator = new Random();
        while (true) {
            double numberBetweenZeroAndOne = StdRandom.uniform();
            int input = randomGenerator.nextInt(1000);
            if (numberBetweenZeroAndOne < 0.25) {
                testAddFirst(sol, std, input, callTraces);
            } else if (numberBetweenZeroAndOne < 0.5) {
                testAddLast(sol, std, input, callTraces);
            } else if (numberBetweenZeroAndOne < 0.75) {
                testRemoveFirst(sol, std, callTraces);
            } else {
                testRemoveLast(sol, std, callTraces);
            }
        }
    }

    /**
     * Test addFirst method
     * @param sol solution implementation
     * @param std student implementation
     * @param input input number
     * @param callTraces call traces, used for printing out error messages
     */
    private void testAddFirst(ArrayDequeSolution<Integer> sol, StudentArrayDeque<Integer> std,
                              int input, StringBuilder callTraces) {
        String callMessage = "addFirst(" + input + ")" + "\n";
        callTraces.append(callMessage);
        String errorMessage = callTraces.toString();
        sol.addFirst(input);
        std.addFirst(input);
        assertEquals(errorMessage, sol.size(), std.size());
        assertFalse(errorMessage, std.isEmpty());
        assertEquals(errorMessage, sol.get(0), std.get(0));
    }

    /**
     * Test addLast method
     * @param sol solution implementation
     * @param std student implementation
     * @param input input number
     * @param callTraces call traces, used for printing out error messages
     */
    private void testAddLast(ArrayDequeSolution<Integer> sol, StudentArrayDeque<Integer> std,
                             int input, StringBuilder callTraces) {
        String callMessage = "addLast(" + input + ")" + "\n";
        callTraces.append(callMessage);
        String errorMessage = callTraces.toString();
        sol.addLast(input);
        std.addLast(input);
        assertEquals(errorMessage, sol.size(), std.size());
        assertFalse(errorMessage, std.isEmpty());
        assertEquals(errorMessage, sol.get(sol.size() - 1), std.get(sol.size() - 1));
    }
    /**
     * Test removeFirst method
     * @param sol solution implementation
     * @param std student implementation
     * @param callTraces call traces, used for printing out error messages
     */
    private void testRemoveFirst(ArrayDequeSolution<Integer> sol, StudentArrayDeque<Integer> std,
                                StringBuilder callTraces) {
        assertEquals(sol.size(), std.size());
        if (!std.isEmpty()) {
            String callMessage = "removeFirst()\n";
            callTraces.append(callMessage);
            String errorMessage = callTraces.toString();
            assertEquals(errorMessage, sol.removeFirst(), std.removeFirst());
            assertEquals(errorMessage, sol.size(), std.size());
        }
    }
    /**
     * Test removeLast method
     * @param sol solution implementation
     * @param std student implementation
     * @param callTraces call traces, used for printing out error messages
     */
    private void testRemoveLast(ArrayDequeSolution<Integer> sol, StudentArrayDeque<Integer> std,
                                StringBuilder callTraces) {
        assertEquals(sol.size(), std.size());
        if (!std.isEmpty()) {
            String callMessage = "removeLast()\n";
            callTraces.append(callMessage);
            String errorMessage = callTraces.toString();
            assertEquals(errorMessage, sol.removeLast(), std.removeLast());
            assertEquals(errorMessage, sol.size(), std.size());
        }
    }
}
