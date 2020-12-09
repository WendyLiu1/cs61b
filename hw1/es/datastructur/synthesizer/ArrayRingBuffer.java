package es.datastructur.synthesizer;
import java.util.Iterator;

public class ArrayRingBuffer<T> implements BoundedQueue<T>  {
    /* Index for the next dequeue or peek. */
    private int first;
    /* Index for the next enqueue. */
    private int last;
    /* Variable for the fillCount. */
    private int fillCount;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        this.rb = (T[]) new Object[capacity];
        this.first = 0;
        this.last = 0;
        this.fillCount = 0;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow").
     */
    @Override
    public void enqueue(T x) {
        if (this.isFull()) {
            throw new RuntimeException("Ring buffer overflow");
        }
        this.rb[this.last] = x;
        this.last = this.increaseIndex(this.last);
        this.fillCount++;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    @Override
    public T dequeue() {
        if (this.isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        T firstElement = this.rb[this.first];
        this.rb[this.first] = null;
        this.first = this.increaseIndex(this.first);
        this.fillCount--;
        return firstElement;
    }

    /**
     * Return oldest item, but don't remove it. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    @Override
    public T peek() {
        if (this.isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        return this.rb[this.first];
    }

    @Override
    public int capacity() {
        return this.rb.length;
    }

    @Override
    public int fillCounter() {
        return this.fillCount;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayRingBufferIterator<>();
    }

    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }

        Iterator<T> it1 = this.iterator();
        Iterator<T> it2 = ((ArrayRingBuffer) obj).iterator();
        while (it1.hasNext() && it2.hasNext()) {
            if (!it1.next().equals(it2.next())) {
                return false;
            }
        }

        if (it1.hasNext() || it2.hasNext()) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        Iterator<T> it = this.iterator();
        while (it.hasNext()) {
            hash += it.next().hashCode();
        }
        return hash;
    }

    /**
     * increase the input index by one, if goes over capacity, get the modulo value
     * @param input input index
     * @return get the modulo increased value
     */
    private int increaseIndex(int input) {
        return Math.floorMod(input + 1, this.capacity());
    }

    private class ArrayRingBufferIterator<T> implements Iterator<T> {
        int index;
        int count;
        ArrayRingBufferIterator() {
            this.index = first;
            this.count = fillCount;
        }

        @Override
        public boolean hasNext() {
            return this.count > 0;
        }

        @Override
        public T next() {
            T result = (T) rb[this.index];
            increaseIndex(this.index);
            this.count--;
            return result;
        }
    }
}
