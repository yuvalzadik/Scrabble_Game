package scrabble_game;

import java.util.HashSet;


public class CacheManager {
    private final int maxsize;
    private final HashSet<String> cachedwords ;
    private final CacheReplacementPolicy crp;

    /**
     * The CacheManager function is responsible for managing the cache.
     * It will add a word to the cache if it is not already in there, and update its frequency.
     * If the word is already in there, it will only update its frequency.
     * If adding a new word would exceed maxsize of the cache, then remove one or more words from
     *   	the cache according to crp (Cache Replacement Policy).  The CacheManager function returns nothing.


     *
     * @param maxsize Set the maximum number of words that can be stored in the cache
     * @param crptype Determine which replacement policy to use
     */
    public CacheManager(int maxsize, CacheReplacementPolicy crptype) {
        this.maxsize = maxsize;
        this.cachedwords = new  HashSet<String>();
        this.crp = crptype;
    }

    /**
     * The add function adds a word to the cache. If the cache is full, it removes
     * the least recently used word from both data structures before adding in
     * new word. It also updates crp and cachedwords accordingly.

     *
     * @param word Add a word to the cache
     *
     */
    public void add(String word) {
        if (cachedwords.size() == maxsize){
            String removed_word = crp.remove();
            cachedwords.remove(removed_word);
        }
        crp.add(word);
        cachedwords.add(word);

    }
    /**
     * The query function takes a string as input and returns true if the word is in the dictionary, false otherwise.
     *
     * @param word Check if the word is in the cachedwords arraylist
     *
     * @return True if the word is in the dictionary, and false otherwise
     *
     */
    public  boolean query ( String word){
        return cachedwords.contains(word);
    }


}

