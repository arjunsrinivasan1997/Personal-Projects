public class LinkedListDeque<Item> implements Deque<Item> {
    /** Node class helps to make Doubly Linked Lists.
     * Each node has a value, and two pointers that points to the previous node and the next node
     */
    private class Node {
        private Item value;
        private Node prev;
        private Node next;

        Node(Node p, Item v, Node n) {
            value = v;
            prev = p;
            next = n;
        }
    }
    private Node sentinel;
    private int size;
    // Creates an empty Doubly linked lists//
    public LinkedListDeque() {
        size = 0;
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }
    @Override
    // Adds element to the beginning of a doubly linked list//
    public void addFirst(Item element) {
        if (size == 0) {
            sentinel.next = new Node(sentinel, element, sentinel);
            sentinel.prev = sentinel.next;
        } else {
            Node a = new Node(sentinel, element, sentinel.next);
            sentinel.next = a;
            sentinel.next.next.prev = a;
        }
        size++;
    }
    @Override
    // Adds element to the end of the linked list//
    public void addLast(Item element) {
        if (size == 0) {
            addFirst(element);
        } else {
            Node a = new Node(sentinel.prev, element, sentinel);
            sentinel.prev = a;
            sentinel.prev.prev.next = a;
            size++;
        }
    }
    @Override
    // Returns true if linked list is empty, false otherwise//
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }
    @Override
    // Returns current size of the linked list
    public int size() {
        return size;
    }
    @Override
    // Removes first item of the linked list
    public Item removeFirst() {
        size--;
        Item value = sentinel.next.value;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        return value;
    }
    @Override
    // Removes last item of the linked list
    public Item removeLast() {
        size--;
        Item value = sentinel.prev.value;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        return value;
    }
    @Override
    // Returns value that exists at index. If no such index exists, returns null
    public Item get(int index) {
        if (index > size || index < 0) {
            return null;
        }
        Node a = this.sentinel;
        for (int i = 0; i <= index; i++) {
            a = a.next;
        }
        return a.value;
    }
    @Override
    // Same as get, but uses recursion
    public Item getRecursive(int index) {
        Node a = this.sentinel.next;
        if (index > size || index < 0) {
            return null;
        } else {
            return getRecursiveHelper(index, 0, a);
        }
    }
    private Item getRecursiveHelper(int index, int counter, Node n) {
        if (index == counter) {
            return n.value;
        } else {
            counter = counter + 1;
            return getRecursiveHelper(index, counter, n.next);
        }
    }
    @Override
    // Prints the elements of a linked list on a single line separated by a space
    public void printDeque() {
        Node a = this.sentinel;
        a = a.next;
        while (a.value != null) {
            System.out.print(a.value);
            a = a.next;
        }
    }
}






