public class LinkedListDeque<T> {
    public class ListNode<T> {
        public T item;
        public ListNode prev;
        public ListNode next;

        public ListNode(T i, ListNode pre, ListNode nxt) {
            this.item = i;
            this.prev = pre;
            this.next = nxt;
        }
    }

    private ListNode sentinel;
    private int size;

    /**
     * Constructor to instantiate an empty deque
     */
    public LinkedListDeque() {
        this.sentinel = new ListNode(null, null, null);
        this.sentinel.next = this.sentinel;
        this.sentinel.prev = this.sentinel;
        this.size = 0;
    }

    /**
     * Deepcopy constructor
     * @param other other LinkedListDeque to copy
     */
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
        ListNode newNode = new ListNode(item, this.sentinel, this.sentinel.next);
        newNode.next.prev = newNode;
        this.sentinel.next = newNode;
        this.size++;
    }

    /**
     * Adds an item of type T to the back of the deque.
     * @param item new element to add to the deque
     */
    public void addLast(T item) {
        ListNode newNode = new ListNode(item, this.sentinel.prev, this.sentinel);
        newNode.prev.next = newNode;
        this.sentinel.prev = newNode;
        size++;
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
        ListNode node = this.sentinel.next;
        while (node != this.sentinel) {
            System.out.print(node.item.toString() + " ");
            node = node.next;
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
        ListNode node = this.sentinel.next;
        this.sentinel.next = node.next;
        node.next.prev = this.sentinel;
        this.size--;
        return (T) node.item;
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
        ListNode node = this.sentinel.prev;
        node.prev.next = this.sentinel;
        this.sentinel.prev = node.prev;
        this.size--;
        return (T) node.item;
    }

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item,
     * and so forth. If no such item exists, returns null.
     * @param index
     * @return Item at index
     */
    public T get(int index) {
        if (index < 0 || index > this.size - 1) {
            return null;
        }
        ListNode cur = this.sentinel.next;
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
        if (index < 0 || index > this.size - 1) {
            return null;
        }
        ListNode start = this.sentinel.next;
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
