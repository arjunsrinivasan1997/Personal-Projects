/**
 * Created by Arjun Srinivasan on 4/19/17.
 */
import org.junit.*;

import java.util.LinkedList;

import static org.junit.Assert.*;
public class TrieTesting {
   @Test
   public  void basicTrieTest() {
       Trie t = new Trie();
       t.insert("sam");
       t.insert("sad");
       t.insert("same");
       t.insert("sake");
       t.insert("save");
       assertTrue(t.search("sam"));
       assertTrue(t.search("sad"));
       assertFalse(t.search("sak"));
       assertFalse(t.search("sav"));

   }

   @Test
   public  void trieChildrenTest(){
       Trie t = new Trie();
       t.insert("sam");
       t.insert("sad");
       t.insert("same");
       t.insert("sake");
       t.insert("save");
       t.insert("a");
       t.insert("awls");
       t.insert("anvil");
       t.insert("ants");
       t.insert("anna");
       t.insert("annope");
       t.insert("anxe");
       testResults(t.getChildern("sa"), new String[]{"sam", "sad", "same", "sake", "save"});
       testResults(t.getChildern("aw"), new String[]{"awls"});
       testResults(t.getChildern("an"), new String[]{"anvil","anna","annope","anxe"});
       testResults(t.getChildern("ann"), new String[]{"anna","annope"});
       testResults(t.getChildern("anx"), new String[]{"anxe"});
   }

   public void testResults (LinkedList<String> expected, String[] actual) {
       for (String s: actual) {
           assertTrue("ERROR: Expected " + s + " but is was not found in LinkedList", expected.contains(s));
       }
   }
}

