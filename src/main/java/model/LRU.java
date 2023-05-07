package model;
import java.util.LinkedHashSet;

public class LRU implements CacheReplacementPolicy {
    // Hash set to store the cache words requests
    private final LinkedHashSet<String> wordcache = new LinkedHashSet<String>();

    public void add(String word) {
        if (wordcache.contains(word))
            wordcache.remove(word);
        wordcache.add(word);
    }
    // Return and remove the last word of the hashset
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
