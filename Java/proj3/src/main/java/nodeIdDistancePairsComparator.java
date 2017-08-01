/**
 * Created by Arjun Srinivasan on 4/17/17.
 */

import java.util.Comparator;
public class nodeIdDistancePairsComparator implements Comparator<NodeDistancePair> {

    @Override
    public int compare(NodeDistancePair a, NodeDistancePair b){
        return a.compareTo(b);
    }
}