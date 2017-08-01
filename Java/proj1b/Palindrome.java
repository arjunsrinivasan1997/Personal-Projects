/**
 * Tests the methods of the Palindrome class
 * Created by arjunsrinivasan on 2/6/17.
 */
public class Palindrome {
    // Takes a string as input, and returns a Deque that has each character of the string//
    public static Deque<Character> wordToDeque(String word) {
        LinkedListDeque<Character> wordDeque = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i++) {
            wordDeque.addLast(word.charAt(i));
        }
        return wordDeque;
    }
    // Returns true if a word is a plaindrome, false otherwise//
    public static boolean isPalindrome(String word) {
        Deque<Character> wordDeque = wordToDeque(word);
        if (wordDeque.size() <= 1) {
            return true;
        } else {
            for (int i = 0; i < wordDeque.size() / 2; i++) {
                if (wordDeque.get(i) != wordDeque.get(wordDeque.size() - 1 - i)) {
                    return false;
                }
            }
            return true;

        }
    }

    // Returns true if a word is palindrome based on the condition specified by cc//
    public static boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> wordDeque = wordToDeque(word);
        for (int i = 0; i < wordDeque.size() / 2; i++) {
            if (!(cc.equalChars(wordDeque.get(i), wordDeque.get(wordDeque.size() - 1 - i)))) {
                return false;
            }
        }
        return true;
    }
}
