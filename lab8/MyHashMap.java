import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MyHashMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int DEFAULT_INITIAL_SIZE = 16;
    private static final int DEFAULT_INCREASE_FACTOR = 2;

    private final double loadFactor;
    private final int initialSize;
    private Entry<K, V>[] table;
    private int size;

    public MyHashMap() {
        this(MyHashMap.DEFAULT_INITIAL_SIZE);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, MyHashMap.DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialSize, double loadFactor) {
        this.size = 0;
        this.loadFactor = loadFactor;
        this.initialSize = initialSize;
        this.table = (Entry<K, V>[]) new Entry[initialSize];
    }

    @Override
    public void clear() {
        this.table = (Entry<K, V>[]) new Entry[this.initialSize];
        this.size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return this.get(key) != null;
    }

    @Override
    public V get(K key) {
        if (this.table == null || this.table.length < 1) {
            return null;
        }
        Entry<K, V> entry = this.table[this.getIndex(key)];
        while (entry != null) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
            entry = entry.next;
        }
        return null;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void put(K key, V value) {
        if (this.table == null || this.table.length < 1) {
            return;
        }

        int index = this.getIndex(key);
        Entry<K, V> entry = this.table[index];
        Entry<K, V> prev = entry;
        while (entry != null) {
            // if key is found, update the value
            if (entry.key == key) {
                entry.value = value;
                return;
            }
            prev = entry;
            entry = entry.next;
        }

        entry = new Entry(key, value);
        // if entry is empty
        if (prev == null) {
            this.table[index] = entry;
        } else {
            prev.next = entry;
        }
        this.size++;
        this.rehash();
    }

    @Override
    public Set<K> keySet() {
        Set<K> result = new HashSet<>();
        for (Entry<K, V> element : this.table) {
            while (element != null) {
                result.add(element.key);
                element = element.next;
            }
        }
        return result;
    }

    @Override
    public V remove(K key) {
        return this.removeHelper(key, null, false);
    }

    @Override
    public V remove(K key, V value) {
        return this.removeHelper(key, value, true);
    }

    @Override
    public Iterator<K> iterator() {
        return this.keySet().iterator();
    }

    /**
     * Rehash the table if hash map size exceeds the load factor
     */
    private void rehash() {
        if (this.size >= this.loadFactor * this.table.length) {
            Entry<K, V>[] oldTable = this.table;
            this.size = 0;
            this.table = (Entry<K, V>[]) new Entry[this.table.length * MyHashMap.DEFAULT_INCREASE_FACTOR];
            for (Entry<K, V> element : oldTable) {
                while (element != null) {
                    this.put(element.key, element.value);
                    element = element.next;
                }
            }
        }
    }

    /**
     * Get the mapping index of the given key in the hashmap
     * @param key key to get index from
     * @return the mapping index in the hash map
     */
    private int getIndex(K key) {
        return Math.floorMod(key.hashCode(), this.table.length);
    }

    /**
     * Helper function to remove element from hash map
     * @param key key to check
     * @param value value to check
     * @param checkVal whether value should be checked
     * @return deleted entry's value if exist
     */
    private V removeHelper(K key, V value, boolean checkVal) {
        if (this.table == null || this.table.length < 1) {
            return null;
        }

        int index = this.getIndex(key);
        Entry<K, V> entry = this.table[index];
        Entry<K, V> prev = null;
        while (entry != null) {
            if (entry.key.equals(key) && (!checkVal || entry.value.equals(value))) {
                if (prev == null) {
                    this.table[index] = entry.next;
                    this.size--;
                    return entry.value;
                }
            }
            prev = entry;
            entry = entry.next;
        }
        return null;
    }

    private class Entry<K, V> {
        Entry<K, V> next;
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
