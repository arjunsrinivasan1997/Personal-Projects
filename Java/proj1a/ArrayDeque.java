public class ArrayDeque<Item> {
    private Item[] items;
    private int size;
    private int nextFirst;
    private int nextLast;
    private double resizingFactor = 2;

    public ArrayDeque() {
        items = (Item[]) new Object[8];
        size = 0;
        nextFirst = 4;
        nextLast = 5;
    }

    private void increaseSize(int capacity) {
        Item[] a = (Item[]) new Object[capacity];
        System.arraycopy(items, 0, a, 0, nextLast);
        int first = nextFirst + 1;
        if (first > items.length - 1) {
            first = 0;
        }
        System.arraycopy(items, first, a, a.length - (size() - first), size() - first);
        nextFirst = a.length - 1 - (size() - nextLast);
        items = a;
    }
    public void addLast(Item element) {
        if (size == items.length) {
            increaseSize((int) (items.length * resizingFactor));
        }
        size++;
        items[nextLast] = element;
        if (nextLast + 1 > items.length - 1) {
            nextLast = 0;
        } else {
            nextLast++;
        }
    }

    public void addFirst(Item element) {
        if (size == items.length) {
            increaseSize((int) (items.length * resizingFactor));
        }
        size++;
        items[nextFirst] = element;
        if (nextFirst - 1 < 0) {
            nextFirst = items.length - 1;
        } else {
            nextFirst--;
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void printDeque() {
        int counter = nextFirst + 1;
        if (counter > items.length - 1) {
            counter = 0;
        }
        for (int i = 0; i < size; i++) {
            System.out.print(items[counter] + " ");
            counter++;
            if (counter > items.length - 1) {
                counter = 0;
            }
        }

    }

    public Item get(int index) {
        if (index > size || index < 0) {
            return null;
        }
        int counter = nextFirst + 1;
        if (counter > items.length - 1) {
            counter = 0;
        }
        for (int i = 0; i < index; i++) {
            counter++;
            if (counter > items.length - 1) {
                counter = 0;
            }
        }
        return items[counter];
    }

    private void decreaseSize(int capacity) {
        Item[] a = (Item[]) new Object[capacity];
        if (nextFirst + size > items.length - 1) {
            int length = items.length - 1 - nextFirst;
            System.arraycopy(items, nextFirst + 1, a, 0, length);
            System.arraycopy(items, 0, a, length, nextLast);
        } else {
            System.arraycopy(items, nextFirst + 1, a, 0, size);
        }
        nextFirst = capacity - 1;
        nextLast = size;
        items = a;
    }

    public Item removeFirst() {
        if (isEmpty()) {
            return null;
        }
        if (((double) size / (double) items.length) < .25 && items.length >= 16) {
            decreaseSize((int) (items.length / resizingFactor));
        }
        if (nextFirst + 1 > items.length - 1) {
            nextFirst = -1;
        }
        Item a = items[nextFirst + 1];
        items[nextFirst + 1] = null;
        size--;
        nextFirst++;
        return a;
    }

    public Item removeLast() {
        if (isEmpty()) {
            return null;
        }
        if (((double) size / (double) items.length) < .25 && items.length >= 16) {
            decreaseSize((int) (items.length / resizingFactor));
        }
        if (nextLast - 1 < 0) {
            nextLast = items.length;
        }
        Item a = items[nextLast - 1];
        items[nextLast - 1] = null;
        size--;
        nextLast--;
        return a;
    }
}

