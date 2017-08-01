/** Performs some basic linked list tests. */

public class LinkedListDequeTest {

	/* Utility method for printing out empty checks. */
	public static boolean checkEmpty(boolean expected, boolean actual) {
		if (expected != actual) {
			System.out.println("isEmpty() returned " + actual + ", but expected: " + expected);
			return false;
		}
		return true;
	}

	/* Utility method for printing out empty checks. */
	public static boolean checkSize(int expected, int actual) {
		if (expected != actual) {
			System.out.println("size() returned " + actual + ", but expected: " + expected);
			return false;
		}
		return true;
	}

	/* Prints a nice message based on whether a test passed.
	 * The \n means newline. */
	public static void printTestStatus(boolean passed) {
		if (passed) {
			System.out.println("Test passed!\n");
		} else {
			System.out.println("Test failed!\n");
		}
	}

	/** Adds a few things to the list, checking isEmpty() and size() are correct,
	  * finally printing the results.
	  *
	  * && is the "and" operation. */
	public static void addIsEmptySizeTest() {
		System.out.println("Running add/isEmpty/Size test.");
		LinkedListDeque<String> lld1 = new LinkedListDeque<String>();
		boolean passed = checkEmpty(true, lld1.isEmpty());
		lld1.addFirst("front");

		// The && operator is the same as "and" in Python.
		// It's a binary operator that returns true if both arguments true, and false otherwise.
		passed = checkSize(1, lld1.size()) && passed;
		passed = checkEmpty(false, lld1.isEmpty()) && passed;
		lld1.addLast("middle");
		passed = checkSize(2, lld1.size()) && passed;
		lld1.addLast("back");
		passed = checkSize(3, lld1.size()) && passed;
		System.out.println("Printing out deque: ");
		lld1.printDeque();
		printTestStatus(passed);

	}

	/** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
	public static void addRemoveTest() {

		System.out.println("Running add/remove test.");


		LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
		// should be empty
		boolean passed = checkEmpty(true, lld1.isEmpty());
		lld1.addFirst(10);
		// should not be empty
		passed = checkEmpty(false, lld1.isEmpty()) && passed;
		lld1.removeFirst();
		// should be empty
		passed = checkEmpty(true, lld1.isEmpty()) && passed;
		printTestStatus(passed);

	}
	/** Tests all of the methods in the LinkedListDeque.java class using integers as elements for the list*/
	public static void IntListMethodsTest(){
		LinkedListDeque<Integer> lli = new LinkedListDeque<>();
		// adding elements to the list//
		System.out.println("Running Integer List Methods Test");
		for (int i = 0; i <10 ; i++) {
			if (i % 2 ==0) {
                lli.addFirst(i);
            } else {
			    lli.addLast(i);
            }
		}
		System.out.println("Printing elements in list");
        lli.printDeque();
        boolean passed = checkSize(10,lli.size()) && lli.get(0) == 8 && lli.get(9) == 9 && lli.get(12) == null;
		passed = checkEmpty(false, lli.isEmpty()) && passed;
        passed = checkSize(10, lli.size()) && passed;
        lli.removeFirst();
        lli.removeFirst();
        passed = lli.get(0) == 4 && lli.get(1) == 2 && lli.get(7) == 9 && passed && checkSize(8,lli.size()) && passed;
        lli.removeLast();
        lli.removeLast();
        passed = lli.get(5) == 5 && lli.get(4) == 3 && checkSize(6,lli.size()) && passed;
        System.out.println("");
        System.out.println("List after removing first twice and removing last twice");
        lli.printDeque();
        passed = lli.get(3) == 1 && lli.getRecursive(3) == 1 && passed;
        passed = lli.get(10) == null && lli.getRecursive(10) == null && passed;
        printTestStatus(passed);
	}
	/** Tests methods of LinkedListDeque using strings as the elements of the List*/
	public static void StringListTest(){
		LinkedListDeque<String> lls = new LinkedListDeque<>();
		lls.addFirst("why");
		lls.addLast("so");
		lls.addLast("serious");
		lls.printDeque();
	}
	public static void main(String[] args) {
		System.out.println("Running tests.\n");
		addIsEmptySizeTest();
		addRemoveTest();
		IntListMethodsTest();
		StringListTest();

	}
} 
