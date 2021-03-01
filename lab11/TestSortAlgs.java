import edu.princeton.cs.algs4.Queue;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Random;

public class TestSortAlgs {

    @Test
    public void testQuickSort() {
        Queue<Integer> q = this.generateRandomQueue(100);
        Queue<Integer> sortedQ = QuickSort.quickSort(q);
        int prev = sortedQ.dequeue();
        while (!sortedQ.isEmpty()) {
            int cur = sortedQ.dequeue();
            assertTrue(cur > prev);
            prev = cur;
        }
    }

    @Test
    public void testMergeSort() {
        Queue<Integer> q = this.generateRandomQueue(100);
        Queue<Integer> sortedQ = MergeSort.mergeSort(q);
        int prev = sortedQ.dequeue();
        while (!sortedQ.isEmpty()) {
            int cur = sortedQ.dequeue();
            assertTrue(cur > prev);
            prev = cur;
        }
    }

    /**
     * Generate a random queue, the size is based on input n
     * @param n
     * @return
     */
    private Queue<Integer> generateRandomQueue(int n) {
        Random random = new Random();
        Queue<Integer> q = new Queue<>();
        for (int i = 0; i < n; i++) {
            q.enqueue(random.nextInt());
        }
        return q;
    }

    /**
     * Returns whether a Queue is sorted or not.
     *
     * @param items  A Queue of items
     * @return       true/false - whether "items" is sorted
     */
    private <Item extends Comparable> boolean isSorted(Queue<Item> items) {
        if (items.size() <= 1) {
            return true;
        }
        Item curr = items.dequeue();
        Item prev = curr;
        while (!items.isEmpty()) {
            prev = curr;
            curr = items.dequeue();
            if (curr.compareTo(prev) < 0) {
                return false;
            }
        }
        return true;
    }
}
