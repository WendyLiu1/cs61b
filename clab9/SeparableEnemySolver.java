import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class SeparableEnemySolver {

    Graph g;

    /**
     * Creates a SeparableEnemySolver for a file with name filename. Enemy
     * relationships are biderectional (if A is an enemy of B, B is an enemy of A).
     */
    SeparableEnemySolver(String filename) throws java.io.FileNotFoundException {
        this.g = graphFromFile(filename);
    }

    /** Alterntive constructor that requires a Graph object. */
    SeparableEnemySolver(Graph g) {
        this.g = g;
    }

    /**
     * Returns true if input is separable, false otherwise.
     */
    public boolean isSeparable() {
        // This is a bipartite issue
        if (this.g == null) {
            throw new IllegalArgumentException("input is null");
        }
        // 0 means uncolored/visited
        // 1 means marked as red
        // 2 means marked as blue
        Map<String, Integer> partitionMap = new HashMap<>();
        Queue<String> bfsQueue = new ArrayDeque<>();

        for (String label : this.g.labels()) {
            partitionMap.put(label, 0);
        }

        for (String label : this.g.labels()) {
            if (partitionMap.get(label) == 0) {
                bfsQueue.add(label);
                // if unvisited, put any color option
                partitionMap.put(label, 1);
                while (!bfsQueue.isEmpty()) {
                    String curNode = bfsQueue.poll();
                    int currentColor = partitionMap.get(curNode);
                    for (String neighbor : this.g.neighbors(curNode)) {
                        if (partitionMap.get(neighbor) == 0) {
                            bfsQueue.add(neighbor);
                            partitionMap.put(neighbor, this.getDifferentColor(currentColor));
                        } else if (currentColor == partitionMap.get(neighbor)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private int getDifferentColor(int i) {
        if (i == 1) {
            return 2;
        } else {
            return 1;
        }
    }

    /* HELPERS FOR READING IN CSV FILES. */

    /**
     * Creates graph from filename. File should be comma-separated. The first line
     * contains comma-separated names of all people. Subsequent lines each have two
     * comma-separated names of enemy pairs.
     */
    private Graph graphFromFile(String filename) throws FileNotFoundException {
        List<List<String>> lines = readCSV(filename);
        Graph input = new Graph();
        for (int i = 0; i < lines.size(); i++) {
            if (i == 0) {
                for (String name : lines.get(i)) {
                    input.addNode(name);
                }
                continue;
            }
            assert(lines.get(i).size() == 2);
            input.connect(lines.get(i).get(0), lines.get(i).get(1));
        }
        return input;
    }

    /**
     * Reads an entire CSV and returns a List of Lists. Each inner
     * List represents a line of the CSV with each comma-seperated
     * value as an entry. Assumes CSV file does not contain commas
     * except as separators.
     * Returns null if invalid filename.
     *
     * @source https://www.baeldung.com/java-csv-file-array
     */
    private List<List<String>> readCSV(String filename) throws java.io.FileNotFoundException {
        List<List<String>> records = new ArrayList<>();
        Scanner scanner = new Scanner(new File(filename));
        while (scanner.hasNextLine()) {
            records.add(getRecordFromLine(scanner.nextLine()));
        }
        return records;
    }

    /**
     * Reads one line of a CSV.
     *
     * @source https://www.baeldung.com/java-csv-file-array
     */
    private List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<String>();
        Scanner rowScanner = new Scanner(line);
        rowScanner.useDelimiter(",");
        while (rowScanner.hasNext()) {
            values.add(rowScanner.next().trim());
        }
        return values;
    }

    /* END HELPERS  FOR READING IN CSV FILES. */

}
