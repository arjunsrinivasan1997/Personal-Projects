import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /**
     * Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc.
     */
    public HashMap<Long, GraphNode> allNodeswithNames = new HashMap<>();
    public HashMap<String,ArrayList<Long>> namesToIDs = new HashMap<>();
    private LinkedHashMap<String, GraphNode> nodes = new LinkedHashMap<>();
    private int edges;
    private ArrayList<Long> nodeIDs = new ArrayList<>();
    private Trie names = new Trie();
    private HashMap<String, String> cleanedNamestoRealNames = new HashMap<>();

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     *
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputFile, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     *
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }


    /**
     * Remove nodes with no connections from the graph.
     * While this does not guarantee that any two nodes in the remaining graph are connected,
     * we can reasonably assume this since typically roads are connected.
     */

    private void clean() {
        //System.out.println("Nodes before clean");
        //System.out.println(countIterableItems(vertices()));
        ArrayList<Long> nodeIdsClone = (ArrayList<Long>) nodeIDs.clone();
        for (Long i : nodeIDs) {
            GraphNode temp = nodes.get(Long.toString(i));
            if (!temp.isConnected()) {
                nodes.remove(Long.toString(i));
                nodeIdsClone.remove(i);
                //System.out.println("removed node");
            }
        }
        this.nodeIDs = nodeIdsClone;

        //System.out.println("Nodes after clean");
        //System.out.println(countIterableItems(vertices()));
    }

    private <Item> int countIterableItems(Iterable<Item> it) {
        int N = 0;
        for (Item x : it) {
            N += 1;
        }
        return N;
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        return nodeIDs;
    }

    /**
     * Returns ids of all vertices adjacent to v.
     */
    Iterable<Long> adjacent(long v) {
        GraphNode temp = nodes.get(Long.toString(v));
        return nodes.get(Long.toString(v)).getConnections();
    }

    public HashMap<String, String> getCleanedNamestoRealNames() {
        return cleanedNamestoRealNames;
    }

    /**
     * Returns the Euclidean distance between vertices v and w, where Euclidean distance
     * is defined as sqrt( (lonV - lonV)^2 + (latV - latV)^2 ).
     */
    double distance(long v, long w) {
        GraphNode nodeV = nodes.get(Long.toString(v));
        GraphNode nodeW = nodes.get(Long.toString(w));

        return Math.sqrt(Math.pow(nodeV.getX() - nodeW.getX(), 2)
                + Math.pow(nodeV.getY() - nodeW.getY(), 2));
    }

    double distance(long v, double lon, double lat) {
        GraphNode nodeV = nodes.get(Long.toString(v));

        return Math.sqrt(Math.pow(nodeV.getX() - lon, 2) + Math.pow(nodeV.getY() - lat, 2));
    }

    /**
     * Returns the vertex id closest to the given longitude and latitude.
     */
    long closest(double lon, double lat) {
        double tempDistance = Double.MAX_VALUE;
        long tempNode = Long.MAX_VALUE;
        for (String n : nodes.keySet()) {
            double distance = distance(Long.parseLong(n), lon, lat);
            if (distance < tempDistance) {
                tempNode = Long.parseLong(n);
                tempDistance = distance;
            }

        }
        return tempNode;
    }

    /**
     * Longitude of vertex v.
     */
    double lon(long v) {
        return nodes.get(Long.toString(v)).getX();
    }

    /**
     * Latitude of vertex v.
     */
    double lat(long v) {
        return nodes.get(Long.toString(v)).getY();
    }

    void addVertex(GraphNode n) {
        nodes.put(Long.toString(n.getId()), n);
        nodeIDs.add(n.getId());
    }

    public LinkedHashMap<String, GraphNode> getNodes() {
        return nodes;
    }

    public int getVerticies() {
        return nodes.size();
    }

    public int getEdges() {
        return edges;
    }

    public Trie getNames() {
        return names;
    }

    public void addEdge(long vertex1, long vertex2) {
        edges += 1;
        nodes.get(Long.toString(vertex1)).addConnection(vertex2);
        nodes.get(Long.toString(vertex2)).addConnection(vertex1);
        GraphNode temp = nodes.get(Long.toString(vertex1));
        GraphNode temp2 = nodes.get(Long.toString(vertex2));
    }
}