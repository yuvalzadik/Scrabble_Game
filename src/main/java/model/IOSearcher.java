package model;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class IOSearcher {
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
