package scrabble_game;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Dictionary {
    private final CacheManager exist_words ;
    private final CacheManager not_exist_words ;
    private final BloomFilter bloomFilter;
    private final String[] files;

    /**
     * The Dictionary function checks if the word is in the dictionary.
     * <p>
     *
     * @param FileNames Pass an array of strings to the function
     *
     * @docauthor Trelent
     */
    public Dictionary(String...FileNames)  {
        this.exist_words = new CacheManager(400, new LRU());
        this.not_exist_words = new CacheManager(100, new LFU());;
        this.bloomFilter = new BloomFilter(16384,"MD5","SHA1","SHA256","SHA512","MD2");
        this.files = FileNames ;
        Scanner myScaner= null;
        for ( String file: FileNames) {
            try {
                myScaner = new Scanner(new BufferedReader(new FileReader(file)));
            }
            catch (FileNotFoundException e){
                throw new RuntimeException(e);
            }
            while (myScaner.hasNext()) {
                bloomFilter.add(myScaner.next());
            }
            myScaner.close();
        }
    }

    /**
     * The query function checks if the word is in the bloom filter.
     * If it is, then we check if it's in exist_words. If so, return true;
     * else add to exist_words and return true.
     * Else check if it's in not_exist_words. If so, return false;
     * else add to not_exist words and return false;

     *
     * @param word Query the bloom filter
     *
     * @return True if the word is in the data structure
     *
     * @docauthor Trelent
     */
    public boolean query(String word){
        // This method check for a word input if it exist on the exist_words CacheManager
        // or in the not_exist_words CacheManager or in the bloomFilter and
        // return true if the word probably exist or false if it is not exist.
        // if we found the word on the bloomfilter we will update the suitable CacheManager.
        if (exist_words.query(word))
            return true;
        else if (not_exist_words.query(word))
            return false;
        else if (bloomFilter.contains(word)){
            exist_words.add(word);
            return true;
        }
        else{
            not_exist_words.add(word);
            return false;
        }
    }

    /**
     * The challenge function takes a word as an argument and checks if it exists in the files.
     * If the word is found, it adds to exist_words list and returns true.
     * If not, it adds to not_exist_words list and returns false.

     *
     * @param word Search for the word in the files
     *
     * @return True if the word is found in the files and false otherwise
     *
     * @docauthor Trelent
     */
    public boolean challenge(String word) {
       boolean findword= IOSearcher.search(word,files);
       if (findword){
            exist_words.add(word);
            return true;
        }
        else{
            not_exist_words.add(word);
            return false;
        }
    }
}
