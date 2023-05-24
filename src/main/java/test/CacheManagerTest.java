package test;

import scrabble_game.CacheManager;
import scrabble_game.CacheReplacementPolicy;
import scrabble_game.LFU;
import scrabble_game.LRU;

public class CacheManagerTest {
    public static void testCacheManager() {
        CacheManager exists=new CacheManager(3, new LRU());
        boolean b = exists.query("a");
        b|=exists.query("b");
        b|=exists.query("c");

        if(b)
            System.out.println("wrong result for CacheManager first queries");

        exists.add("a");
        exists.add("b");
        exists.add("c");

        b=exists.query("a");
        b&=exists.query("b");
        b&=exists.query("c");

        if(!b)
            System.out.println("wrong result for CacheManager second queries ");

        boolean bf = exists.query("d"); // false, LRU is "a"
        exists.add("d");
        boolean bt = exists.query("d"); // true
        bf|= exists.query("a"); // false
        exists.add("a");
        bt &= exists.query("a"); // true, LRU is "b"

        if(bf || ! bt)
            System.out.println("wrong result for CacheManager last queries ");

    }


    public static void main(String[] args) {
        testCacheManager();
        System.out.println("done");
    }
}
