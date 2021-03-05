package bearmaps.proj2c;

import bearmaps.hw4.streetmap.Node;
import bearmaps.hw4.streetmap.StreetMapGraph;
import bearmaps.lab9.MyTrieSet;
import bearmaps.lab9.TrieSet61B;
import bearmaps.proj2ab.KdTree;
import bearmaps.proj2ab.Point;

import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {

    private Map<Point, Node> pointToNodeMap;
    private TrieSet61B trie;
    private KdTree kdTree;
    private Map<String, List<Node>> locationNameIdx;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        this.pointToNodeMap = new HashMap<>();
        this.trie = new MyTrieSet();
        this.locationNameIdx = new HashMap<>();

        List<Node> nodes = this.getNodes();
        List<Point> points = new ArrayList<>();

        for (Node node : nodes) {
            String name = node.name();
            if (name != null && !name.isEmpty()) {
                String cleanedName = AugmentedStreetMapGraph.cleanString(name);
                this.trie.add(cleanedName);
                if (this.locationNameIdx.containsKey(cleanedName)) {
                    this.locationNameIdx.get(cleanedName).add(node);
                } else {
                    List<Node> nodeList = new ArrayList<>();
                    nodeList.add(node);
                    this.locationNameIdx.put(cleanedName, nodeList);
                }
            }

            double lon = node.lon();
            double lat = node.lat();
            Point newPoint = new Point(lon, lat);
            this.pointToNodeMap.put(newPoint, node);
            if (!this.neighbors(node.id()).isEmpty()) {
                points.add(newPoint);
            }
        }
        this.kdTree = new KdTree(points);
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        Point nearestPoint = this.kdTree.nearest(lon, lat);
        Node node = this.pointToNodeMap.get(nearestPoint);
        return node.id();
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        List<String> locationNames = new ArrayList<>();
        List<String> cleanedNames = this.trie.keysWithPrefix(AugmentedStreetMapGraph.cleanString(prefix));
        for (String s : cleanedNames) {
            List<Node> nodeList = this.locationNameIdx.get(s);
            for (Node node : nodeList) {
                locationNames.add(node.name());
            }
        }
        return locationNames;
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        List<Map<String, Object>> result = new LinkedList<>();
        List<Node> nodeList = this.locationNameIdx.get(AugmentedStreetMapGraph.cleanString(locationName));
        if (nodeList != null) {
            for (Node node : nodeList) {
                Map<String, Object> loc = new HashMap<>();
                loc.put("lat", node.lat());
                loc.put("lon", node.lon());
                loc.put("name", node.name());
                loc.put("id", node.id());
                result.add(loc);
            }
        }
        return result;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
