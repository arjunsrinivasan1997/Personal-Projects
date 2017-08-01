import static org.junit.Assert.*;
import org.junit.Test;
public class TestArrayDeque1B {
    @Test
    public void testArrayDeque() {
        // from student array deque launcher//
        StudentArrayDeque<Integer> sad = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ads = new ArrayDequeSolution<>();
        // from operation sequence demo//
        OperationSequence fs = new OperationSequence();
        for (int i = 0; i < 1000; i++) {
            int c = StdRandom.uniform(0, 7);
            int a = StdRandom.uniform(0, 100);
            if (0 <= c && c < 2) {
                sad.addFirst(a);
                ads.addFirst(a);
                DequeOperation dequeOp = new DequeOperation("addFirst", a);
                fs.addOperation(dequeOp);
            } else if (4 <= c && c < 6) {
                sad.addLast(a);
                ads.addLast(a);
                DequeOperation dequeOp = new DequeOperation("addLast", a);
                fs.addOperation(dequeOp);
            } else if (4 <= c && c < 6 && sad.size() != 0 && ads.size() != 0) {
                Integer x = sad.removeFirst();
                Integer y = ads.removeFirst();
                DequeOperation dequeOp = new DequeOperation("removeFirst");
                fs.addOperation(dequeOp);
                assertEquals(fs.toString(), x, y);
            } else if (6 <= c && c < 8 && sad.size() != 0 && ads.size() != 0) {
                Integer x = sad.removeLast();
                Integer y = ads.removeLast();
                DequeOperation dequeOp = new DequeOperation("removeLast");
                fs.addOperation(dequeOp);
                assertEquals(fs.toString(), x, y);
            }
        }
    }
}
