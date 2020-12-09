package es.datastructur.synthesizer;

public interface BoundedQueue<T> extends Iterable<T> {
    // Return the size of the buffer
    int capacity();

    // Return the number of items currently in the buffer
    int fillCounter();

    // Add item x to the end
    void enqueue(T x);

    // Delete and return item from the front
    T dequeue();

    // Return (but do not delete) item from the front
    T peek();

    // Return whether the buffer is empty
    default boolean isEmpty() {
        return this.fillCounter() == 0;
    }

    // Return whether the buffer is full
    default boolean isFull() {
        return this.fillCounter() == this.capacity();
    }
}
