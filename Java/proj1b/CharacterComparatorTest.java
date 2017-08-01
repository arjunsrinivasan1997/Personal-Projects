/** Tests any class that implements Character Comparator
 * Created by arjunsrinivasan on 2/7/17.
 */
import static org.junit.Assert.*;
import org.junit.Test;
public class CharacterComparatorTest {
    @Test
    public void OffByOneTest(){
        OffByOne a = new OffByOne();
        assertEquals(a.equalChars('a','b'),true);
        assertEquals(a.equalChars('h','i'),true);
        assertEquals(a.equalChars('n','m'),true);
        assertEquals(a.equalChars('z','y'),true);
        assertEquals(a.equalChars('a','d'),false);
        assertEquals(a.equalChars('a','e'),false);
        assertEquals(a.equalChars('x','b'),false);
    }
    @Test
    public void OffByNTest(){
        OffByN two = new OffByN(2);
        assertEquals(two.equalChars('a','c'),true);
        assertEquals(two.equalChars('l','j'),true);
        assertEquals(two.equalChars('f','c'),false);
        OffByN five = new OffByN(5);
        assertEquals(five.equalChars('a','f'), true);
        assertEquals(five.equalChars('l','g'), true);
        assertEquals(five.equalChars('a','c'),false);
    }
}
