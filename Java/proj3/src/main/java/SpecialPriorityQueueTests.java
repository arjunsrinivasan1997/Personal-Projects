/**
 * Created by Arjun Srinivasan on 4/17/17.
 */

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SpecialPriorityQueueTests {
    @Test
    public void testBasicMethods() {
        SpecialPriorityQueue<NodeDistancePair> p = new SpecialPriorityQueue<>();
        for (int i = 0; i < 100; i++) {
            NodeDistancePair n = new NodeDistancePair(i, i + 10);
            p.add(n);

        }
        for (int i = 0; i < 100; i++) {
            System.out.println(p.peek());
            assertEquals(p.getSize(), 100 - i);
            assertEquals(p.pop(), new NodeDistancePair(i, i + 10));
        }
    }

    @Test
    public void testChangePriority() {
        SpecialPriorityQueue<NodeDistancePair> p = new SpecialPriorityQueue<>();
        p.add(new NodeDistancePair(1, 10));
        p.add(new NodeDistancePair(2, 20));
        p.add(new NodeDistancePair(-1, 0));
        assertEquals(p.peek(), new NodeDistancePair(-1, 0));
        p.changePriority(-1, 30);
        assertEquals(p.peek(), new NodeDistancePair(1, 10));
        p.changePriority(1, 500);
        assertEquals(p.peek(), new NodeDistancePair(2, 20));
    }

}
