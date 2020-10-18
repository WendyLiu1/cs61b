/* Invariants
1.  nextFirst is the position used when a new item
    is added to the front of the list.
2.  nextLast is the position used when a new item
    is added to the back of the list.
*/
public class ArrayDeque<T> {
    private T[] dequeArray;
    private int size;
    private int nextFirst;
    private int nextLast;

    private static double usageFactor = 0.25;

    /**
     * Default constructor
     */
    public ArrayDeque() {
        this.size = 0;
        this.dequeArray = (T[]) new Object[8];
        this.nextFirst = 0;
        this.nextLast = 1;
    }

    /**
     * Deep copy constructor
     * @param other other array deque to copy
     */
    public ArrayDeque(ArrayDeque other) {
        this();
        int otherSize = other.size();
        for (int i = 0; i < otherSize - 1; i++) {
            this.addLast((T) other.get(i));
        }
    }

    /**
     * Increases array size to capacity
     * @param capacity new array size
     */
    private void increaseSize(int capacity) {
        T[] newDeque = (T[]) new Object[capacity];
        System.arraycopy(this.dequeArray, 0, newDeque, 0, this.size);
        this.dequeArray = newDeque;
    }

    /**
     * Decreases the array size to capacity
     * @param capacity new array size
     */
    private void decreaseSize(int capacity) {
        T[] newDeque = (T[]) new Object[capacity];
        int originalFirst = this.plusOne(this.nextFirst);
        System.arraycopy(this.dequeArray, originalFirst, newDeque, 0, this.size);
        this.nextFirst = this.minusOne(0);
        this.nextLast = this.size;
        this.dequeArray = newDeque;
    }

    /**
     * For arrays of length 16 or more, the usage factor should always be at least 25%
     */
    private void memeoryManagement() {
        int dequeArraySize = this.dequeArray.length;
        if (dequeArraySize > 15) {
            double usageRatio = this.size / dequeArraySize;
            if (usageRatio < ArrayDeque.usageFactor) {
                this.decreaseSize(dequeArraySize / 2);
            }
        }
    }

    /**
     * Adds an item of type T to the front of the deque.
     * @param item new element to add to the deque
     */
    public void addFirst(T item) {
        // resize at len - 1 to make sure next first and next last will never meet
        if (this.size == this.dequeArray.length - 1) {
            this.increaseSize(size * 2);
        }
        this.dequeArray[this.nextFirst] = item;
        this.nextFirst = this.minusOne(this.nextFirst);
        this.size++;
    }

    /**
     * Adds an item of type T to the back of the deque.
     * @param item new element to add to the deque
     */
    public void addLast(T item) {
        // resize at len - 1 to make sure next first and next last will never meet
        if (this.size == this.dequeArray.length - 1) {
            this.increaseSize(this.size * 2);
        }
        this.dequeArray[this.nextLast] = item;
        this.nextLast = this.plusOne(this.nextLast);
        this.size++;
    }

    /**
     * Returns true if deque is empty, false otherwise.
     */
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * Returns the number of items in the deque.
     * @return the number of items in the deque .
     */
    public int size() {
        return this.size;
    }

    /**
     * Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line.
     */
    public void printDeque() {
        for (int i = 0; i < this.size; i++) {
            System.out.print(this.get(i) + " ");
        }
        System.out.println();
    }

    /**
     * Removes and returns the item at the front of the deque.
     * If no such item exists, returns null
     * @return the item at the front of the deque. If no such item exists, return null
     */
    public T removeFirst() {
        if (this.isEmpty()) {
            return null;
        }
        int firstIndex = this.plusOne(this.nextFirst);
        T item = this.dequeArray[this.nextFirst];
        this.dequeArray[firstIndex] = null;
        this.size--;
        this.memeoryManagement();
        return item;
    }

    /**
     * Removes and returns the item at the back of the deque.
     * If no such item exists, returns null
     * @return the item at the back of the deque. If no such item exists, return null
     */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        int lastIdx = this.minusOne(this.nextLast);
        T item = this.dequeArray[lastIdx];
        this.dequeArray[lastIdx] = null;
        this.size--;
        this.memeoryManagement();
        return item;
    }

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item,
     * and so forth. If no such item exists, returns null.
     * @param index
     * @return
     */
    public T get(int index) {
        if (index < 0 || index > size() - 1) {
            return null;
        }
        int idx = this.plusX(nextFirst, index + 1);
        return dequeArray[idx];
    }

    /**
     * Returns the new idx in the circular array when adding by one
     * @param idx the index to be added to
     * @return the new index
     */
    private int plusOne(int idx) {
        return this.plusX(idx, 1);
    }

    /**
     * Returns the new idx in the circular array when adding by one
     * @param idx the index to be added to
     * @return the new index
     */
    private int plusX(int idx, int x) {
        return (idx + x) % this.dequeArray.length;
    }

    /**
     * Returns the new index when subtracting one from a circular array
     * @param idx the idx to be subtracted from
     * @return the new index
     */
    private int minusOne(int idx) {
        if (idx == 0) {
            return this.dequeArray.length - 1;
        } else {
            return idx - 1;
        }
    }
}
