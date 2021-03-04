package bearmaps.proj2ab;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {

    private ArrayList<PriorityNode> heap;
    // this is used to do item searching
    private HashMap<T, Integer> itemMap;

    public ArrayHeapMinPQ() {
        this.heap = new ArrayList<>();
        // heap[0] is unused so parent/child index can be easily calculated
        this.heap.add(null);
        this.itemMap = new HashMap<>();
    }

    @Override
    public void add(T item, double priority) {
        if (this.contains(item)) {
            throw new IllegalArgumentException("item already in PQ");
        }
        PriorityNode newNode = new PriorityNode(item, priority);
        this.heap.add(newNode);
        int curIdx = this.size();
        this.itemMap.put(item, curIdx);
        while (curIdx > 1) {
            int newPos = this.floatUp(curIdx);
            if (newPos == curIdx) {
                break;
            } else {
                curIdx = newPos;
            }
        }
    }

    @Override
    public boolean contains(T item) {
        return this.itemMap.containsKey(item);
    }

    @Override
    public T getSmallest() {
        if (this.size() == 0) {
            throw new NoSuchElementException("PQ is empty");
        }
        return this.heap.get(1).getItem();
    }

    @Override
    public T removeSmallest() {
        if (this.size() == 0) {
            throw new NoSuchElementException("PQ is empty");
        }
        T smallest = this.getSmallest();
        int curIdx = 1;
        int lastIdx = this.size();
        this.swap(smallest, this.heap.get(lastIdx).item, curIdx, lastIdx);
        this.itemMap.remove(smallest);
        // ArrayList has O (n) time complexity for arbitrary indices of add/remove,
        // but O (1) for the operation at the end of the list. 0.
        // arraylist remove last element time complexity
        this.heap.remove(this.size());
        while (curIdx < this.size()) {
            int newPos = this.sinkDown(curIdx);
            if (newPos == curIdx) {
                break;
            } else {
                curIdx = newPos;
            }
        }

        return smallest;
    }

    /**
     * Float up operation for given index, return the new position
     * @param idx current index
     * @return new position in the heap
     */
    private int floatUp(int idx) {
        if (idx == 1) {
            return idx;
        }
        int parentIdx = this.getParentIdx(idx);
        PriorityNode parent = this.heap.get(parentIdx);
        PriorityNode current = this.heap.get(idx);
        if (current.compareTo(parent) < 0) {
            this.swap(current.item, parent.item, idx, parentIdx);
            return parentIdx;
        }
        return idx;
    }

    /**
     * Sink operation for given idx, if the minimal between current
     * idx and its children is itself, we stop the operation
     * @param idx current index
     * @return new position in the heap
     */
    private int sinkDown(int idx) {
        // If it is the last level, no need to swap
        int level = (int) (Math.log(this.size()) / Math.log(2));
        int lastLevelFirstIdx = (int) Math.pow(2, level);
        if (idx >= lastLevelFirstIdx) {
            return idx;
        }
        PriorityNode currentNode = this.heap.get(idx);
        int minOfAll = this.findMinIdx(idx);
        if (minOfAll == idx) {
            return idx;
        } else {
            this.swap(currentNode.item, this.heap.get(minOfAll).item, idx, minOfAll);
            return minOfAll;
        }
    }

    /**
     * Find the minimal between current and it's children
     * @param idx current index
     * @return smallest index
     */
    private int findMinIdx(int idx) {
        int minIdx = idx;
        PriorityNode curNode = this.heap.get(idx);
        int leftIdx = this.getLeftChildIdx(idx);
        int rightIdx = this.getRightChildIdx(idx);

        if (leftIdx <= this.size() && this.heap.get(leftIdx).compareTo(this.heap.get(minIdx)) < 0) {
            minIdx = leftIdx;
        }

        if (rightIdx <= this.size() && this.heap.get(rightIdx).compareTo(this.heap.get(minIdx)) < 0) {
            minIdx = rightIdx;
        }

        return  minIdx;
    }

    /**
     * Swap the item in the heap also update location info in hashmap
     * @param item1 item 1 in the hashmap
     * @param item2 item 2 in the hashmap
     * @param idx1 idx for item1
     * @param idx2 idx for item2
     */
    private void swap(T item1, T item2, int idx1, int idx2) {
        //TODO: Maybe update function signature so we dont need idx1 and idx2
        // since they can be get from hashmap
        Collections.swap(this.heap, idx1, idx2);
        this.itemMap.put(item2, idx1);
        this.itemMap.put(item1, idx2);
    }

    @Override
    public int size() {
        return this.heap.size() - 1;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!this.contains(item)) {
            throw new NoSuchElementException("No such element in the PQ");
        }
        int nodeIdx = this.itemMap.get(item);
        PriorityNode node = this.heap.get(nodeIdx);
        double prevP = node.priority;
        node.setPriority(priority);
        if (prevP < priority) {
            while (nodeIdx < this.size()) {
                // New priority is larger
                int newPos = this.sinkDown(nodeIdx);
                if (newPos == nodeIdx) {
                    break;
                } else {
                    nodeIdx = newPos;
                }
            }
        } else if (prevP > priority) {
            while (nodeIdx > 1) {
                int newPos = this.floatUp(nodeIdx);
                if (newPos == nodeIdx) {
                    break;
                } else {
                    nodeIdx = newPos;
                }
            }
        }
    }

    /**
     * Get parent index
     * @param idx current index
     * @return parent index of current index in the heap
     */
    private int getParentIdx(int idx) {
        return idx / 2;
    }

    /**
     * Get left child index
     * @param idx current index
     * @return left child index in the heap
     */
    private int getLeftChildIdx(int idx) {
        return 2 * idx;
    }

    /**
     * Get right child index
     * @param idx current index
     * @return right child index in the heap
     */
    private int getRightChildIdx(int idx) {
        return 2 * idx + 1;
    }

    private class PriorityNode implements Comparable<ArrayHeapMinPQ.PriorityNode> {
        private T item;
        private double priority;

        PriorityNode(T e, double p) {
            this.item = e;
            this.priority = p;
        }

        T getItem() {
            return item;
        }

        double getPriority() {
            return priority;
        }

        void setPriority(double priority) {
            this.priority = priority;
        }

        @Override
        public int compareTo(ArrayHeapMinPQ.PriorityNode other) {
            if (other == null) {
                return -1;
            }
            return Double.compare(this.getPriority(), other.getPriority());
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object o) {
            if (o == null || o.getClass() != this.getClass()) {
                return false;
            } else {
                return ((ArrayHeapMinPQ.PriorityNode) o).getItem().equals(getItem());
            }
        }

        @Override
        public int hashCode() {
            return item.hashCode();
        }
    }
}

