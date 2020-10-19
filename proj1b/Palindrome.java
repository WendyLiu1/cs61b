public class Palindrome {
    /**
     * Given a string, wordToDeque should return a Deque where the
     * characters appear in the same order as in the string.
     * For example, if the word is "persiflage", then the returned Deque
     * should have 'p' at the front, followed by 'e', and so forth.
     * @param word input word
     * @return word deque representation
     */
    public Deque<Character> wordToDeque(String word) {
        if (word == null || word.length() < 1) {
            return null;
        }
        Deque<Character> wordDeque = new ArrayDeque<>();
        int wordLength = word.length();
        for (int i = 0; i < wordLength; i++) {
            wordDeque.addLast(word.charAt(i));
        }
        return wordDeque;
    }

    /**
     * Check if the given word is palindrome or not
     * @param word input word
     * @return True if the given word is palindrome, False otherwise.
     */
    public boolean isPalindrome(String word) {
        if (word == null) {
            return false;
        }
        if (word.length() < 2) {
            return true;
        }
        Deque<Character> wordDeque = this.wordToDeque(word);
        while (wordDeque.size() > 1) {
            Character first = wordDeque.removeFirst();
            Character last = wordDeque.removeLast();
            if (!first.equals(last)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if the given word is palindrome or not with given character comparator
     * @param word word to check
     * @param cc compare rule
     * @return True if the word is a palindrome according to the character comparison,
     * false otherwise.
     */
    public boolean isPalindrome(String word, CharacterComparator cc) {
        if (word == null) {
            return false;
        }
        if (word.length() < 2) {
            return true;
        }
        Deque<Character> wordDeque = this.wordToDeque(word);
        while (wordDeque.size() > 1) {
            Character first = wordDeque.removeFirst();
            Character last = wordDeque.removeLast();
            if (!cc.equalChars(first, last)) {
                return false;
            }
        }
        return true;
    }
}
