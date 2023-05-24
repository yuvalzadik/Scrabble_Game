package test;

import scrabble_game.CacheReplacementPolicy;
import scrabble_game.LRU;

public class LRUTest {
    public static void testLRU() {
        CacheReplacementPolicy lru=new LRU();
        lru.add("a");
        lru.add("b");
        lru.add("c");
        lru.add("a");

        if(!lru.remove().equals("b"))
            System.out.println("wrong implementation for LRU");
    }

    public static void main(String[] args) {
        testLRU();
        System.out.println("done");
    }
}
