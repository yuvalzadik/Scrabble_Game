package scrabble_game;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;

public class BloomFilter {
    private final BitSet b;
    //algs present the hash functions
    private final ArrayList<String> algs = new ArrayList<>() ;
    /**
     * The BloomFilter function takes in a string and hashes it using the algorithms specified
     * when the BloomFilter was created. It then sets each of those bits to 1.

     *
     * @param size int  Set the size of the bitset b
     * @param algo String...algo  in an array of strings. array of bits that grow as needed until size
     */
    public BloomFilter(int size, String...algo) {
        Collections.addAll(algs,algo);
        b= new BitSet(size);
    }

    // This function get a word and hash method and return according them the location we need to set on the BitSet b
    /**
     * The hash_value function takes a string and a hash function name as input,
     * and returns an integer value in the range [0, b.size()- 1] that is computed
     * by applying the specified hash function to the input string.  The returned
     * value is used as an index into BloomFilter's bitset (b).  This method uses
     *
     * @param s String  Determine the hash function to be used
     * @param word String  Get the word to be added into the bloom filter
    public void add(string word) {

     *
     * @return An int number according the hash function
     *
     * @docauthor Trelent
     */
    private int hash_value(String s, String word) {
        // initialize hash function according its name
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(s);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        // return an array with bytes of the word input
        byte[] bts = md.digest(word.getBytes());
        //From given a set of bytes creates a number
        BigInteger bi = new BigInteger(bts);
        // this method convert the bi number to int positive number
        int number = Math.abs(bi.intValue());
        // get single index according the hash function and the bitset size
        return number % (b.size());
    }
    /**
     * The add function takes a string and hashes it using the hash_value function.
     * It then sets the bit at that location to 1 in the bitset.

     *
     * @param word Get the hash value of a word
     *
     */
    public void add(String word)  {
        for(String s: algs){
           int value = hash_value(s,word);
            // change the bitset on the value location from 0 to 1
            b.set(value);
        }
        }
    /**
     * The contains function checks if the given word is in the Bloom Filter.
     *
     * @param word String Generate the hash value
     *
     * @return A boolean value
     *
     */
    public Boolean contains(String word)  {
        for(String s: algs) {
            int value = hash_value(s, word);
            // Check if the bit at index value is set
            if (!(b.get(value)))
                return false ;
        }
        return true;
    }
    /**
     * The toString function is used to print the BitSet in a readable format.
     *
     * @return a string of {0,1} according to the bits that have been set on the BitSet b
     *
     */
    public String toString() {
        StringBuilder bits = new StringBuilder();
        // b.length is the number of the actual members it has, size it's the maxsize definition we defined
        for(int i = 0 ; i < b.length();i++){
            if(b.get(i))
                bits.append("1");
            else
                bits.append("0");
        }
        return bits.toString();
    }
}
