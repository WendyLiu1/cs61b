package bearmaps;

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
            Integer swapped = this.swimUP(curIdx);
            if (swapped == null) {
                break;
            } else {
                curIdx = swapped;
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
        //ArrayList has O (n) time complexity for arbitrary indices of add/remove,
        // but O (1) for the operation at the end of the list. 0.
        // arraylist remove last element time complexity
        this.heap.remove(this.size());
        while (curIdx < this.size()) {
            Integer swapped = this.sinkDown(curIdx);
            if (swapped == null) {
                break;
            } else {
                curIdx = swapped;
            }
        }

        return smallest;
    }

    /**
     * Swap the current node and the parent node, also reset the hashmap idx
     * @param idx
     */
    private Integer swimUP(int idx) {
        if (idx == 1) {
            return null;
        }
        int parentIdx = this.getParent(idx);
        PriorityNode parent = this.heap.get(parentIdx);
        PriorityNode current = this.heap.get(idx);
        if (current.compareTo(parent) < 0) {
            this.swap(current.item, parent.item, idx, parentIdx);
            return parentIdx;
        }
        return null;
    }

    private Integer sinkDown(int idx) {
        // If it is the last level, no need to swap
        int level = (int) (Math.log(this.size()) / Math.log(2));
        int lastLevelFirstIdx = (int) Math.pow(2, level);
        if (idx >= lastLevelFirstIdx) {
            return null;
        }
        PriorityNode currentNode = this.heap.get(idx);
        PriorityNode rightChild = null;
        PriorityNode leftChild = null;
        int rightChildIdx = this.getRightChild(idx);
        int leftChildIdx = this.getLeftChild(idx);

        if (rightChildIdx < this.heap.size()) {
            rightChild = this.heap.get(rightChildIdx);
        }

        if (leftChildIdx < this.heap.size()) {
            leftChild = this.heap.get(leftChildIdx);
        }

        if (leftChild == null) {
            return null;
        }

        // No right child or right child is larger than left, then we only compare left
        if (rightChild == null || leftChild.compareTo(rightChild) < 0) {
            if (leftChild.compareTo(currentNode) < 0) {
                this.swap(currentNode.item, leftChild.item, idx, leftChildIdx);
                return leftChildIdx;
            } else {
                return null;
            }
        } else {
            // right <= left
            if (rightChild.compareTo(currentNode) < 0) {
                this.swap(currentNode.item, rightChild.item, idx, rightChildIdx);
                return rightChildIdx;
            } else {
                return null;
            }
        }
    }

    private void swap(T item1, T item2, int idx1, int idx2) {
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
                Integer swapped = this.sinkDown(nodeIdx);
                if (swapped == null) {
                    break;
                } else {
                    nodeIdx = swapped;
                }
            }
        } else if (prevP > priority) {
            while (nodeIdx > 1) {
                Integer swapped = this.swimUP(nodeIdx);
                if (swapped == null) {
                    break;
                } else {
                    nodeIdx = swapped;
                }
            }
        }
    }

    private int getParent(int idx) {
        return idx / 2;
    }

    private int getLeftChild(int idx) {
        return 2 * idx;
    }

    private int getRightChild(int idx) {
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

