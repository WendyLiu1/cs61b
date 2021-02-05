import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyTrieSet implements TrieSet61B {

    // Root of the trie
    private TrieNode root;

    private class TrieNode {
        private boolean isKey;
        private Map<Character, TrieNode> next;

        private TrieNode() {
            this.next = new HashMap<>();
        }
    }

    public MyTrieSet() {
        this.root = new TrieNode();
    }

    @Override
    public void add(String key) {
        if (key == null || key.length() < 1) {
            throw new IllegalArgumentException("input is null or empty.");
        }
        this.add(this.root, key, 0);
    }

    /**
     * Add key to trie at each level
     * @param curNode current trie node
     * @param key key to put in the trie
     * @param curIdx current index in key to be processed
     */
    private void add(TrieNode curNode, String key, int curIdx) {
        if (curIdx == key.length()) {
            return;
        }
        char curKey = key.charAt(curIdx);
        boolean isKey = curIdx == key.length() - 1;
        TrieNode nextNode = curNode.next.get(curKey);
        if (nextNode == null) {
            nextNode = new TrieNode();
            curNode.next.put(curKey, nextNode);
        }
        nextNode.isKey = isKey;
        this.add(nextNode, key, curIdx + 1);
    }

    @Override
    public boolean contains(String key) {
        if (key == null || key.length() < 1) {
            throw new IllegalArgumentException("input is null or empty");
        }
        TrieNode prefixNode = this.get(this.root, key, 0);
        return prefixNode != null && prefixNode.isKey;
    }

    /**
     * Returns the trieNode for key
     * @param curNode current trie node
     * @param key key to put in the trie
     * @param curIdx current index in key to be processed
     * @return true if the Trie contains KEY, false otherwise
     */
    private TrieNode get(TrieNode curNode, String key, int curIdx) {
        if (curNode == null) {
            return null;
        }
        char curKey = key.charAt(curIdx);
        TrieNode nextNode = curNode.next.get(curKey);
        if (curIdx == key.length() - 1) {
            return nextNode;
        }
        return this.get(nextNode, key, curIdx + 1);
    }

    @Override
    public List<String> keysWithPrefix(String prefix) {
        if (prefix == null || prefix.length() < 1) {
            throw new IllegalArgumentException("input is invalid");
        }
        List<String> result = new ArrayList<>();
        TrieNode prefixNode = this.get(this.root, prefix, 0);
        this.collect(prefixNode, new StringBuilder(prefix), result);
        return result;
    }

    /**
     * Collect all keys starting with current node
     * @param curNode current node
     * @param sb string builder at current level
     * @param result result container
     */
    private void collect(TrieNode curNode, StringBuilder sb, List<String> result) {
        if (curNode == null) {
            return;
        }
        if (curNode.isKey) {
            result.add(sb.toString());
        }

        for (Map.Entry<Character, TrieNode> entry : curNode.next.entrySet()) {
            sb.append(entry.getKey());
            collect(entry.getValue(), sb, result);
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    @Override
    public void clear() {
        this.root = new TrieNode();
    }

    @Override
    public String longestPrefixOf(String key) {
        throw new UnsupportedOperationException();
    }
}
