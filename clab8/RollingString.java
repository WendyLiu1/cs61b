import java.util.LinkedList;
/**
 * A String-like class that allows users to add and remove characters in the String
 * in constant time and have a constant-time hash function. Used for the Rabin-Karp
 * string-matching algorithm.
 */
//https://algs4.cs.princeton.edu/53substring/RabinKarp.java.html
class RollingString {

    /**
     * Number of total possible int values a character can take on.
     * DO NOT CHANGE THIS.
     */
    static final int UNIQUECHARS = 128;

    /**
     * The prime base that we are using as our mod space. Happens to be 61B. :)
     * DO NOT CHANGE THIS.
     */
    static final int PRIMEBASE = 6113;

    private LinkedList<Character> strRep;

    private int hash;

    // baseMode = PRIMEBASE^(n - 1)
    private int baseMod;

    /**
     * Initializes a RollingString with a current value of String s.
     * s must be the same length as the maximum length.
     */
    public RollingString(String s, int length) {
        assert (s.length() == length);
        this.strRep = new LinkedList<>();
        this.hash = 0;
        this.baseMod = 1;
        for (int i = 0; i < length; i++) {
            char c = s.charAt(i);
            this.strRep.add(c);
            if (i != 0) {
                this.baseMod = (this.baseMod * RollingString.UNIQUECHARS) % RollingString.PRIMEBASE;
            }
            this.hash = (this.hash * RollingString.UNIQUECHARS + c) % RollingString.PRIMEBASE;
        }
    }

    /**
     * Rabin fingerprint
     * (x + y) mod z = (x mod z + y mod z) mod z
     * (x - y) mod z = (x mod z - y mod z) mod z
     * (x * y) mod z = ((x mod z) * (y mod z)) mod z
     * x^y mod z = ((x mod z)^y) mod z
     *
     * Assumption p>b
     *
     * base(b,n) = b^n mod p = (b * b^n-1) mod p
     *                       = ((b mod p * base(b, n-1)) mod p
     *                       = (b * base(b, n - 1)) mod p
     *
     * s(0, n) = c[0] * b^n-1 + c[1] * b^n-2 + ... + c[n-1]
     * h(0, n) = s(0, n) mod p
     * -> h(0, n+1) = (c[0] * b^n + c[1] * b^n-1 + ... c[n]) mod p
     * -> h(0, n+1) = (b * s(0,n) + c[n]) mod p
     * -> h(0, n+1) = [(b * s(0,n) mod p + c[n] mod p] mod p
     * -> h(0, n+1) = [((b mod p) * h(0,n)) mod p + c[n] mod p] mod p
     * so -> h(0, n+1) = [(b mod p) * h(0,n) + c[n]] mod p
     *                 = [b*h(0,n) + c[n]] mod p
     *
     * h1 = (c[i] * b^n-1 + c[i+1] * b^n-2 + ... + c[i+n-1]) mod p
     * h2 = (c[i+1] * b^n-1 + c[i+2] * b^n-2 + ... + c[i+n]) mod p
     * h1 = s(i, n) mod p
     * h2 = (s(i, n) * b - c[i] * b^n + c[i+n]) mod p
     *    = (s(i, n)*b mod p - c[i]*b^n mod p + c[i+n] mod p) mod p
     *    = ((h1 * (b mod p)) mod p - ((c[i] mod p)*(b^n mod p)mod p + c[i+n] mod p) mod p
     *    = (h1 * b - (c[i] mod p) * (b^n mod p) + c[i+n]) mod p
     */

    /**
     * Adds a character to the back of the stored "string" and 
     * removes the first character of the "string". 
     * Should be a constant-time operation.
     */
    public void addChar(char c) {
        char first = strRep.removeFirst();
        this.strRep.add(c);
        // To prevent overflow
        //Remove leading digit
        this.hash = (this.hash + RollingString.PRIMEBASE - this.baseMod * first % RollingString.PRIMEBASE) % RollingString.PRIMEBASE;
        this.hash = (this.hash * RollingString.UNIQUECHARS + c) % RollingString.PRIMEBASE;
    }

    /**
     * Returns the "string" stored in this RollingString, i.e. materializes
     * the String. Should take linear time in the number of characters in
     * the string.
     */
    public String toString() {
        StringBuilder strb = new StringBuilder();
        for (char c : this.strRep) {
            strb.append(c);
        }
        return strb.toString();
    }

    /**
     * Returns the fixed length of the stored "string".
     * Should be a constant-time operation.
     */
    public int length() {
        return this.strRep.size();
    }


    /**
     * Checks if two RollingStrings are equal.
     * Two RollingStrings are equal if they have the same characters in the same
     * order, i.e. their materialized strings are the same.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RollingString)) {
            return false;
        }
        int len1 = this.strRep.size();
        int len2 = ((RollingString) o).strRep.size();

        if (len1 != len2) {
            return false;
        }

        for (int i = 0; i < len1; i++) {
            char c1 = this.strRep.get(i);
            char c2 = ((RollingString) o).strRep.get(i);
            if (c1 != c2) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the hashcode of the stored "string".
     * Should take constant time.
     */
    @Override
    public int hashCode() {
        return this.hash;
    }
}
