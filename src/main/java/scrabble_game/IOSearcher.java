package scrabble_game;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class IOSearcher {
    /**
     * The search function takes a word and an array of file names as input.
     * It then searches each file for the given word, returning true if it is found in any of them.
     * <p>
     *
     * @param word Store the word that is being searched for
     * @param FileNames Pass a variable number of arguments to the search function
     *
     * @return True if the word is found in any of the files, false otherwise
     *
     * @docauthor Trelent
     */
    public static boolean search (String word, String...FileNames)  {
        Scanner myScaner= null;
        for ( String file: FileNames) {
            try {
                myScaner = new Scanner(new BufferedReader(new FileReader(file)));
            }
            catch (FileNotFoundException e){
                return false;
            }
            while (myScaner.hasNext()) {
                if (myScaner.next().equals(word)) {
                    myScaner.close();
                    return true;
                }
            }
            myScaner.close();
        }
        return false ;
    }
}
