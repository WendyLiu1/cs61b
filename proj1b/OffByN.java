public class OffByN implements CharacterComparator {
    private int offset;
    /**
     * Constructor taken input as offset
     * @param N offset
     */
    public OffByN(int N) {
        this.offset = N;
    }

    @Override
    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == this.offset;
    }
}
