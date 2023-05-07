package model;

import java.util.LinkedHashMap;
import java.util.Map;

public class LFU implements CacheReplacementPolicy {
    // key for the word , value for the nuber of times it asked.
    private final LinkedHashMap<String,Integer> LFUwords = new LinkedHashMap<>();

    public void add(String word) {
        String key = null;
        // Checking if thw word we want to add already in the cache
        for (Map.Entry<String, Integer> e : LFUwords.entrySet()) {
            if (e.getKey().equals(word)) {
                key = e.getKey();
                LFUwords.put(word, e.getValue() + 1);
            }
        }
        // if word is new
        if (key == null)
            LFUwords.put(word, 1);
    }
    public String remove() {
        int least_used_word =1000000 ;
        String word_key_remove = null ;
        for ( Map.Entry<String, Integer> e : LFUwords.entrySet()){
            if (e.getValue() < least_used_word)
                word_key_remove = e.getKey();
                least_used_word = e.getValue();
        }
        LFUwords.remove(word_key_remove);
        return (word_key_remove);
    }
}
