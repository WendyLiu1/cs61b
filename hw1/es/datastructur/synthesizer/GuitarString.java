package es.datastructur.synthesizer;
import java.util.HashSet;

//Note: This file will not compile until you complete task 1 (BoundedQueue).
public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final
     * means the values cannot be changed at runtime. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    private BoundedQueue<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        int capacity = (int) Math.round(((double) GuitarString.SR) / frequency);
        this.buffer = new ArrayRingBuffer<>(capacity);
        while (!this.buffer.isFull()) {
            this.buffer.enqueue(0.0);
        }
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        HashSet<Double> randomNumbers = new HashSet<>();
        for (int i = 0; i < this.buffer.fillCounter(); i++) {
            this.buffer.dequeue();

            double r = Math.random() - 0.5;
            while (randomNumbers.contains(r)) {
                r = Math.random() - 0.5;
            }
            randomNumbers.add(r);

            this.buffer.enqueue(r);
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {
        double front = this.buffer.dequeue();
        double newFront = this.buffer.peek();
        double newDouble = GuitarString.DECAY * 0.5 * (front + newFront);
        this.buffer.enqueue(newDouble);

    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return this.buffer.peek();
    }
}
