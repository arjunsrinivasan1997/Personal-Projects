/** This class outputs all palindromes in the words file in the current directory. */
public class PalindromeFinder {
    public static void main(String[] args) {
        int minLength = 2 ;
        In in = new In("words.webarchive");

        while (!in.isEmpty()) {
            String word = in.readString();
            if (word.length() >= minLength && Palindrome.isPalindrome(word,new OffByN(10))){
                System.out.println(word);
            }
        }
    }
} 
