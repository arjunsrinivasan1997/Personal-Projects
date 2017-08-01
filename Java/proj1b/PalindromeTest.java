/** Tester class for the palindrome class
 * Created by arjunsrinivasan on 2/6/17.
 */
import static org.junit.Assert.*;
import org.junit.Test;
public class PalindromeTest {

    @Test
    public void TestIsPalindrome() {
        assertEquals(Palindrome.isPalindrome("Hello"),false);
        assertEquals(Palindrome.isPalindrome("horse"),false);
        assertEquals(Palindrome.isPalindrome("abduction"),false);
        assertEquals(Palindrome.isPalindrome("abear"),false);
        assertEquals(Palindrome.isPalindrome("a"),true);
        assertEquals(Palindrome.isPalindrome("racecar"),true);
        assertEquals(Palindrome.isPalindrome("noon"),true);
        assertEquals(Palindrome.isPalindrome("mom"),true);
        assertEquals(Palindrome.isPalindrome("dad"),true);
        assertEquals(Palindrome.isPalindrome("repaper"),true);
        assertEquals(Palindrome.isPalindrome("reviver"),true);
        assertEquals(Palindrome.isPalindrome("rotator"),true);
    }
    @Test
    public void TestIsPalindromeGeneral(){
        assertEquals(Palindrome.isPalindrome("flake",new OffByOne()),true);
        assertEquals(Palindrome.isPalindrome("unhot",new OffByOne()),true);
        assertEquals(Palindrome.isPalindrome("stout",new OffByOne()),true);
        assertEquals(Palindrome.isPalindrome("test",new OffByOne()),false);
        assertEquals(Palindrome.isPalindrome("batman",new OffByOne()),false);
        assertEquals(Palindrome.isPalindrome("ululu",new OffByOne()),false);
        assertEquals(Palindrome.isPalindrome("hello",new OffByOne()),false);
        assertEquals(Palindrome.isPalindrome("wormy",new OffByN(2)),true);
        assertEquals(Palindrome.isPalindrome("bland",new OffByN(2)),true);
        assertEquals(Palindrome.isPalindrome("blend",new OffByN(2)),true);
        assertEquals(Palindrome.isPalindrome("maneri",new OffByN(2)),false);
        assertEquals(Palindrome.isPalindrome("testing",new OffByN(2)),false);
        assertEquals(Palindrome.isPalindrome("viper",new OffByN(2)),false);
        assertEquals(Palindrome.isPalindrome("vowel",new OffByN(10)),true);
        assertEquals(Palindrome.isPalindrome("sri",new OffByN(10)),true);
        assertEquals(Palindrome.isPalindrome("ovule",new OffByN(10)),true);
        assertEquals(Palindrome.isPalindrome("nosed",new OffByN(10)),true);
        assertEquals(Palindrome.isPalindrome("viper",new OffByN(10)),false);
        assertEquals(Palindrome.isPalindrome("hello",new OffByN(10)),false);


    }
}




