import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/** Priority Queue that supports changing of priorities in the queue
 * Created by Arjun Srinivasan on 4/17/17.
 */
public class SpecialPriorityQueue<Item> {
    PriorityQueue<Item> p;
    HashMap<Long, Double> distances = new HashMap<>();
    int size;

    public SpecialPriorityQueue(){
        p = new PriorityQueue<Item>((Comparator<Item>) new nodeIdDistancePairsComparator());
        distances = new HashMap<>();
        size = 0;
    }

    public boolean contains(long nodeId){
        return p.contains(nodeId);
    }

    public void changePriority(long nodeId, double newPriority) {
        double distance = distances.get(nodeId);
        if (!p.remove(new NodeDistancePair(nodeId,distance))){
            throw new IllegalArgumentException("ERROR: Element does noe exist in priority queue");
        }
        p.add((Item) new NodeDistancePair(nodeId, newPriority));
    }

    /**
     * Returns the first item in the queue, without removing it
     * @return Item that is first in the qeue
     */
    public Item peek(){
        return p.peek();
    }

    /**
     * Adds item a to the queue
     * @param a Type Item
     */
    public void add(Item a){
        if (p.contains(a)){
            NodeDistancePair temp = (NodeDistancePair) a;
            changePriority(temp.getNode(),temp.getDistance());
        } else {
            p.add(a);
        }
        size++;
        NodeDistancePair temp = (NodeDistancePair) a;
        distances.put(temp.getNode(),temp.getDistance());
    }

    public int getSize() {
        return size;
    }

    /**

     * Returns and removes the first item in the queue
     * @return Item that is first in the queue
     */
    public Item pop(){
        size --;
        NodeDistancePair temp = (NodeDistancePair) p.peek();
        distances.remove(temp.getNode(),temp.getDistance());
        return p.poll();
    }

    public static void main(String[] args){
        SpecialPriorityQueue<NodeDistancePair> a = new SpecialPriorityQueue<>();
        a.p.add(new NodeDistancePair(1234,12));
        a.p.add(new NodeDistancePair(12345,-1));
        a.p.add(new NodeDistancePair(12345,1));
        System.out.println(a.p.poll());
        System.out.println(a.p.poll());
    }
}
