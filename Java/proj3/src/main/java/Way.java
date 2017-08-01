import java.util.ArrayList;

/**
 * Created by Arjun Srinivasan on 4/16/17.
 */

public class Way {
    private String name;
    private ArrayList<Long> potentialConnections;
    private boolean isValid;
    private String maxSpeed;
    public Way(){
        potentialConnections= new ArrayList<>();
    }
    public void addConnection(long nodeID){
        potentialConnections.add(nodeID);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Long> getPotentialConnections() {
        return potentialConnections;
    }



    public boolean isValid() {
        return isValid;
    }

    public void setIsValid(boolean valid) {
        isValid = valid;
    }

    public String getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(String maxSpeed) {
        this.maxSpeed = maxSpeed;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Way{");
        sb.append("name='").append(name).append('\'');
        sb.append(", potentialConnections=").append(potentialConnections);
        sb.append(", isValid=").append(isValid);
        sb.append(", maxSpeed=").append(maxSpeed);
        sb.append('}');
        return sb.toString();
    }
}
