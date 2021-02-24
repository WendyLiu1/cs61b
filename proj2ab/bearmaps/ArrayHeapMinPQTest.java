package bearmaps;

import org.junit.Test;
import java.util.NoSuchElementException;
import static org.junit.Assert.*;

public class ArrayHeapMinPQTest {
    @Test
    public void testAdd() {
        ArrayHeapMinPQ<String> ipq = new ArrayHeapMinPQ<>();
        assertEquals(ipq.size(), 0);

        // Add first item
        ipq.add("i1", 0.1);
        assertEquals(ipq.size(), 1);

        // Add second item
        ipq.add("i2", 0.2);
        assertEquals(ipq.size(), 2);

        // Add third item with same priority
        ipq.add("i3", 0.1);
        assertEquals(ipq.size(), 3);

        // Add duplicate item expect exception
        // The following syntax only available for JUnit5
        //Exception exception = assertThrows(IllegalArgumentException.class, ipq.add("i1", 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addExceptionTest() {
        ArrayHeapMinPQ<String> ipq = new ArrayHeapMinPQ<>();
        assertEquals(ipq.size(), 0);

        // Add first item
        ipq.add("i1", 0.1);
        assertEquals(ipq.size(), 1);

        // Add second item
        ipq.add("i1", 0.1);
        assertEquals(ipq.size(), 1);
    }

    @Test
    public void testContains() {
        ArrayHeapMinPQ<String> ipq = new ArrayHeapMinPQ<>();
        assertFalse(ipq.contains("i1"));

        // Add first item
        ipq.add("i1", 0.1);
        assertTrue(ipq.contains("i1"));
    }

    @Test
    public void testGetSmallest() {
        ArrayHeapMinPQ<String> ipq = new ArrayHeapMinPQ<>();
        ipq.add("i1", 0.2);
        ipq.add("i2", 0.3);
        ipq.add("i3", 0.6);
        ipq.add("i4", 0.1);
        ipq.add("i5", 0.05);
        ipq.add("i6", 0.7);
        assertEquals("i5", ipq.getSmallest());
    }

    @Test(expected = NoSuchElementException.class)
    public void getSmallestExceptionTest() {
        ArrayHeapMinPQ<String> ipq = new ArrayHeapMinPQ<>();
        ipq.getSmallest();
    }

    @Test
    public void testRemoveSmallest() {
        ArrayHeapMinPQ<String> ipq = new ArrayHeapMinPQ<>();
        ipq.add("i1", 0.2);
        ipq.add("i2", 0.3);
        ipq.add("i3", 0.6);
        ipq.add("i4", 0.1);
        ipq.add("i5", 0.05);
        ipq.add("i6", 0.7);
        assertEquals("i5", ipq.removeSmallest());
        assertEquals("i4", ipq.getSmallest());
        assertEquals(5, ipq.size());
        assertEquals("i4", ipq.removeSmallest());
        assertEquals("i1", ipq.getSmallest());
    }

    @Test(expected = NoSuchElementException.class)
    public void removeSmallestExceptionTest() {
        ArrayHeapMinPQ<String> ipq = new ArrayHeapMinPQ<>();
        ipq.removeSmallest();
    }

    @Test
    public void testSize() {
        ArrayHeapMinPQ<String> ipq = new ArrayHeapMinPQ<>();
        assertEquals(0, ipq.size());
        ipq.add("i1", 0.2);
        ipq.add("i2", 0.3);
        ipq.add("i3", 0.6);
        assertEquals(3, ipq.size());
        ipq.add("i4", 0.1);
        ipq.add("i5", 0.05);
        ipq.add("i6", 0.7);
        ipq.removeSmallest();
        assertEquals(5, ipq.size());
    }

    @Test
    public void testChangePriority() {
        ArrayHeapMinPQ<String> ipq = new ArrayHeapMinPQ<>();
        ipq.add("i1", 0.2);
        ipq.add("i2", 0.3);
        ipq.add("i3", 0.6);
        ipq.add("i4", 0.1);
        ipq.add("i5", 0.05);
        ipq.add("i6", 0.7);
        assertEquals("i5", ipq.getSmallest());
        ipq.changePriority("i5", 0.8);
        assertEquals("i4", ipq.getSmallest());
        ipq.changePriority("i1", 0.05);
        assertEquals("i1", ipq.getSmallest());
    }

    @Test(expected = NoSuchElementException.class)
    public void testChangePriorityException() {
        ArrayHeapMinPQ<String> ipq = new ArrayHeapMinPQ<>();
        ipq.add("i1", 0.1);
        ipq.changePriority("i2", 0.2);
    }
}
