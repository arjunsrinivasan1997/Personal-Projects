/**
 * Created by arjunsrinivasan on 2/6/17.
 */
public interface Deque<Item> {
    void addFirst(Item i);

    void addLast(Item i);

    boolean isEmpty();

    int size();

    void printDeque();

    Item removeFirst();

    Item removeLast();

    Item get(int index);

    Item getRecursive(int index);
}
