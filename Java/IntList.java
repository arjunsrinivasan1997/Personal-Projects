public class IntList {
	public int first;
	public IntList rest;

	public IntList(int f, IntList r) {
		first = f;
		rest = r;
	}

	/** Return the size of the list using... recursion! */
	public int size() {
		if (rest == null) {
			return 1;
		}
		return 1 + this.rest.size();
	}

	/** Return the size of the list using no recursion! */
	public int iterativeSize() {
		IntList p = this;
		int totalSize = 0;
		while (p != null) {
			totalSize += 1;
			p = p.rest;
		}
		return totalSize;
	}
    public int get(int i) {
        if (i < 0 || i> size()) {
            throw new IndexOutOfBoundsException("Index " + i + " is out of bounds!");
        }
        int counter = 0;
        IntList p = this;
        while (p != null) {
            if (counter == i) {
                return p.first;
            } else {
                counter++;
                p = p.rest;
            }
        return 0;
        }
    }
    public static void main(String[] args) {
		IntList L = new IntList(15, null);
		L = new IntList(10, L);
		L = new IntList(5, L);

		System.out.println(L.get(2));
	}
}
