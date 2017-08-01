import java.util.ArrayList;
import java.util.Map;
/**
 * Created by Arjun Srinivasan on 4/15/17.
 */
public class GraphNode {
    private long id;
    private double x;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GraphNode{");
        sb.append(", name='").append(name).append('\'');
        sb.append("x=").append(x);
        sb.append(", y=").append(y);
        sb.append('}');
        return sb.toString();
    }

    private double y;
    private String name;
    private boolean isConnected;

    private Map<String, String> attributes;
    private ArrayList<Long> connections;
    public GraphNode(long id, double longitude, double latitude) {
        this.id = id;
        x = longitude;
        y = latitude;
        connections = new ArrayList<>();
        isConnected = false;
    }
    public void addConnection(long nodeId){
        connections.add(nodeId);
        isConnected = true;

    }

    public void setName(String name) {
        this.name = name;
    }

    public double getX() {
        return x;
    }
    public String getName() {
        return name;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnections(ArrayList<Long> connections) {
        this.connections = connections;
        if (!(connections.size() == 0)){
            isConnected = true;
        }
    }

    public long getId(){
        return  id;
    }

    public double getY() {
        return y;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public ArrayList<Long> getConnections() {
        return connections;
    }

}
