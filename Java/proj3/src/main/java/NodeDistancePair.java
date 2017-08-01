import edu.princeton.cs.algs4.Graph;

import java.util.ArrayList;
import java.util.Comparator;
/**
 * Created by Arjun Srinivasan on 4/17/17.
 */
public class NodeDistancePair implements Comparable<NodeDistancePair> {
    private long node;
    private double distance;
    private GraphDB g;
    private long endNode;


    public long getNode() {
        return node;
    }


    public double getDistance() {
        return distance;
    }

    public NodeDistancePair(long nodeID, double distanceSoFar) {
        this.node = nodeID;
        this.distance = distanceSoFar;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeDistancePair that = (NodeDistancePair) o;

        if (node != that.node) return false;
        return Double.compare(that.distance, distance) == 0;
    }


    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (node ^ (node >>> 32));
        temp = Double.doubleToLongBits(distance);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }


    @Override
    public int compareTo(NodeDistancePair n){
        if (n.getDistance() == distance){
            return 0;
        } else if (n.getDistance() == -1 || getDistance() > n.getDistance()){
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("NodeDistancePair{");
        sb.append("node=").append(node);
        sb.append(", distance=").append(distance);
        sb.append('}');
        return sb.toString();
    }

    public static Comparator<NodeDistancePair> getComparator(){
        return new nodeIdDistancePairsComparator();
    }


}
