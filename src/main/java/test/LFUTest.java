package test;

import scrabble_game.CacheReplacementPolicy;
import scrabble_game.LFU;

public class LFUTest {
    public static void testLFU() {
        CacheReplacementPolicy lfu=new LFU();
        lfu.add("a");
        lfu.add("b");
        lfu.add("b");
        lfu.add("c");
        lfu.add("a");

        if(!lfu.remove().equals("c"))
            System.out.println("wrong implementation for LFU");
    }

    public static void main(String[] args) {
        testLFU();
        System.out.println("done");
    }
}
