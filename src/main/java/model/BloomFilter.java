package model;

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
    public BloomFilter(int size, String...algo) {
        Collections.addAll(algs,algo);
        // array of bits that grow as needed until size
        b= new BitSet(size);
    }

    // This function get a word and hash method and return according them the location we need to set on the BitSet b
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
    public void add(String word)  {
        for(String s: algs){
           int value = hash_value(s,word);
            // change the bitset on the value location from 0 to 1
            b.set(value);
        }
        }
    public Boolean contains(String word)  {
        for(String s: algs) {
            int value = hash_value(s, word);
            // Check if the bit at index value is set
            if (!(b.get(value)))
                return false ;
        }
        return true;
    }

    // return a string of {0,1} according to the bits that have been set on the BitSet b
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
