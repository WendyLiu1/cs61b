public class LinkedListDeque<T> {
    public class ListNode<T> {
        public T item;
        public ListNode next;
        public ListNode prev;

        public ListNode(T i) {
            item = i;
            next = null;
            prev = null;
        }
    }

    private ListNode sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new ListNode(null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    public LinkedListDeque(LinkedListDeque other) {
        this();
        int otherSize = other.size();
        for (int i = 0; i < otherSize; i++) {
            addLast((T) other.get(i));
        }
    }

    /**
     * Adds an item of type T to the front of the deque.
     * @param item new element to add to the deque
     */
    public void addFirst(T item) {
        ListNode newNode = new ListNode(item);
        newNode.next = sentinel.next;
        newNode.prev = sentinel;
        newNode.next.prev = newNode;
        sentinel.next = newNode;
        size++;
    }

    /**
     * Adds an item of type T to the back of the deque.
     * @param item new element to add to the deque
     */
    public void addLast(T item) {
        ListNode newNode = new ListNode(item);
        newNode.prev = sentinel.prev;
        newNode.next = sentinel;
        newNode.prev.next = newNode;
        sentinel.prev = newNode;
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
        ListNode node = sentinel.next;
        while (node != sentinel) {
            System.out.print(node.item.toString());
            System.out.print(" ");
            node = node.next;
        }
        System.out.println();
    }

    /**
     * Removes and returns the item at the front of the deque.
     * If no such item exists, returns null
     *
     * @return the item at the front of the deque. If no such item exists, return null
     */
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        ListNode node = sentinel.next;
        sentinel.next = node.next;
        node.next.prev = sentinel;
        size--;
        return (T) node.item;
    }

    /**
     * Removes and returns the item at the back of the deque.
     * If no such item exists, returns null
     *
     * @return the item at the back of the deque. If no such item exists, return null
     */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        ListNode node = sentinel.prev;
        node.prev.next = sentinel;
        sentinel.prev = node.prev;
        size--;
        return (T) node.item;
    }

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item,
     * and so forth. If no such item exists, returns null.
     *
     * @param index
     * @return Item at index
     */
    public T get(int index) {
        if (index < 0 || index > size - 1) {
            return null;
        }
        ListNode cur = sentinel.next;
        while (index > 0) {
            cur = cur.next;
            index--;
        }
        return (T) cur.item;
    }

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item,
     * and so forth. If no such item exists, returns null.
     *
     * @param index
     * @return Item at index
     */
    public T getRecursive(int index) {
        if (index < 0 || index > size - 1) {
            return null;
        }
        ListNode start = sentinel.next;
        return (T) getRecursiveHelper(start, index).item;
    }

    /**
     * Return the node distance away from current node
     * @param cur current node
     * @param distance distance from the current node
     * @return node distance away from current node
     */
    private ListNode getRecursiveHelper(ListNode cur, int distance) {
        if (distance == 0) {
            return cur;
        }
        return getRecursiveHelper(cur.next, distance - 1);
    }
}
