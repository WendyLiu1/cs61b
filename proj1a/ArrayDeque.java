public class ArrayDeque<T> {
    private T[] dequeArray;
    private int size;
    private int nextFirst;
    private int nextLast;


    public ArrayDeque() {
        size = 0;
        dequeArray = (T[]) new Object[8];
        nextFirst = 0;
        nextLast = 0;
    }

    public ArrayDeque(ArrayDeque other) {
        this();
        int otherSize = other.size();
        for (int i = 0; i < otherSize - 1; i++) {
            this.addLast((T) other.get(i));
        }
    }

    private void resize(int capacity) {
        T[] newDeque = (T[]) new Object[capacity];
        System.arraycopy(dequeArray, 0, newDeque, 0, size);
        dequeArray = newDeque;
    }

    /**
     * Adds an item of type T to the front of the deque.
     * @param item new element to add to the deque
     */
    public void addFirst(T item) {
        // resize at len - 1 to make sure next first and next last will never meet
        if (size == dequeArray.length - 1) {
            this.resize(size * 2);
        }
        dequeArray[nextFirst] = item;
        if (nextFirst == 0) {
            nextFirst = dequeArray.length - 1;
        }
        else {
            nextFirst--;
        }
        size++;
    }

    /**
     * Adds an item of type T to the back of the deque.
     * @param item new element to add to the deque
     */
    public void addLast(T item) {
        // resize at len - 1 to make sure next first and next last will never meet
        if (size == dequeArray.length - 1) {
            this.resize(size * 2);
        }
        dequeArray[nextLast] = item;
        nextLast = (nextLast + 1) % dequeArray.length;
        size++;
    }

    /**
     * Returns true if deque is empty, false otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of items in the deque.
     * @return the number of items in the deque .
     */
    public int size() {
        return size;
    }

    /**
     * Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line.
     */
    public void printDeque() {
        int begin = (nextFirst + 1) % dequeArray.length;
        int count = 0;
        while (count < size) {
            System.out.print(dequeArray[(begin + count) % dequeArray.length]);
            System.out.print(" ");
            count++;
        }
        System.out.println();
    }

    /**
     * Removes and returns the item at the front of the deque.
     * If no such item exists, returns null
     * @return the item at the front of the deque. If no such item exists, return null
     */
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        nextFirst = (nextFirst + 1) % dequeArray.length;
        T item = dequeArray[nextFirst];
        dequeArray[nextFirst] = null;
        size--;
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
        if (nextLast == 0) {
            nextLast = dequeArray.length - 1;
        }
        else {
            nextLast--;
        }
        T item = dequeArray[nextLast];
        dequeArray[nextLast] = null;
        size--;
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
        int idx = (nextFirst + 1 + index) % dequeArray.length;
        return dequeArray[idx];
    }
}
