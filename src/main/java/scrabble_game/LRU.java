package scrabble_game;
import java.util.LinkedHashSet;

public class LRU implements CacheReplacementPolicy {
    // Hash set to store the cache words requests
    private final LinkedHashSet<String> wordcache = new LinkedHashSet<String>();

    /**
     * The add function adds a word to the cache.
     * <p>
     *
     * @param word Add a word to the cache
     *
     * @docauthor Trelent
     */
    public void add(String word) {
        if (wordcache.contains(word))
            wordcache.remove(word);
        wordcache.add(word);
    }

    /**
     * The remove function removes the first word in the cache.
     * <p>
     *
     *
     * @return and remove the last word of the hashset
     *
     * @docauthor Trelent
     */
    public String remove() {
        if (wordcache.isEmpty())
            return null;
        else {
            String first = wordcache.iterator().next();
            wordcache.remove(first);
            return first;
        }
    }
}
