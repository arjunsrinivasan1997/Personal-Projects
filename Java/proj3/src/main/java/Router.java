import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a LinkedList of <code>Long</code>s representing the shortest path from st to dest,
     * where the longs are node IDs.
     */

    private static HashMap<Long, Double> distanceTo;
    private static HashMap<Long, Long> edgeTo;
    private static SpecialPriorityQueue<NodeDistancePair> fringe;

    public static LinkedList<Long> shortestPath(GraphDB g, double stlon,
                                                double stlat, double destlon, double destlat) {
        long startNodeId = g.closest(stlon, stlat);
        long endNodeId = g.closest(destlon, destlat);
        distanceTo = new HashMap<>();
        edgeTo = new HashMap<>();
        for (Long l : g.vertices()) {
            distanceTo.put(l, 0.0);
            edgeTo.put(l, null);
        }

        fringe = new SpecialPriorityQueue<>();
        LinkedList<Long> path = new LinkedList<>();
        fringe.add(new NodeDistancePair(startNodeId, 0));
        while (fringe.size != 0) {
            NodeDistancePair n = fringe.pop();
            if (n.getNode() == endNodeId) {
                break;
            } else {
                long parent = n.getNode();
                for (Long adjacent : g.adjacent(parent)) {
                    double tempDistance;
                    tempDistance = distanceTo.get(parent) + g.distance(parent, adjacent);
                    if (tempDistance < distanceTo.get(adjacent) || distanceTo.get(adjacent) == 0.0) {
                        distanceTo.put(adjacent, tempDistance);
                        edgeTo.put(adjacent, parent);
                        fringe.add(new NodeDistancePair(adjacent, tempDistance + g.distance(adjacent, endNodeId)));
                    }
                }
            }
        }

        Long tempPathId = edgeTo.get(endNodeId);
        path.add(endNodeId);
        path.add(tempPathId);
        while (tempPathId != startNodeId) {
            tempPathId = edgeTo.get(tempPathId);
            path.add(tempPathId);
        }
        //path.add(startNodeId);
        Collections.reverse(path);
        return path;
    }
    @Deprecated
    public static double findHeuristic(long nodeId1, long endNode, GraphDB g) {
        return g.distance(nodeId1, endNode);
    }


}
