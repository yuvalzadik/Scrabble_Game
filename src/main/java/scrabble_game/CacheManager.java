package scrabble_game;

import java.util.HashSet;


public class CacheManager {
    private final int maxsize;
    private final HashSet<String> cachedwords ;
    private final CacheReplacementPolicy crp;

    public CacheManager(int maxsize, CacheReplacementPolicy crptype) {
        this.maxsize = maxsize;
        this.cachedwords = new  HashSet<String>();
        this.crp = crptype;
    }

    public void add(String word) {
        if (cachedwords.size() == maxsize){
            String removed_word = crp.remove();
            cachedwords.remove(removed_word);
        }
        crp.add(word);
        cachedwords.add(word);

    }
    public  boolean query ( String word){
        return cachedwords.contains(word);
    }


}

