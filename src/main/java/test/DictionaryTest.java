package test;

import scrabble_game.CacheReplacementPolicy;
import scrabble_game.Dictionary;
import scrabble_game.LRU;

public class DictionaryTest {


    public static void testDictionary() {
        Dictionary d = new Dictionary("text1.txt", "text2.txt");
        if (!d.query("is"))
            System.out.println("problem with dictionarry in query ");
        if (!d.challenge("lazy"))
            System.out.println("problem with dictionarry in query");
            System.out.println("wrong implementation for LRU");
    }

    public static void main(String[] args) {
        testDictionary();
        System.out.println("done");
    }
}
