package model;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class BookScrabbleCommunication {

    final String BookScrabbleIP= "localhost";

    final int BookScrabblePort= 6789;

    private static BookScrabbleCommunication _instance = null;

    ArrayList<String> dictionaries;


    /**
     * The BookScrabbleCommunication function is a static private function .
     *
     */
    private BookScrabbleCommunication() {}

    /**
     * The get_instance function is a singleton function that returns the instance of the BookScrabbleCommunication class.
     * <p>
     *
     *
     * @return The instance of the class
     *
     * @docauthor Trelent
     */
    public static BookScrabbleCommunication get_instance() {
        if (_instance == null) {
            _instance = new BookScrabbleCommunication();
        }
        return _instance;
    }

    /**
     * The runChallengeOrQuery function takes a string as input and sends it to the BookScrabble server.
     * It then returns the response from the BookScrabble server.
     *
     *
     * @param inString inString Send the challenge to bookscrabblehandler
     *
     * @return A string
     *
     * @docauthor Trelent
     */
    public String runChallengeOrQuery(String inString){
        try{
            Socket server=new Socket(this.BookScrabbleIP,this.BookScrabblePort);
            PrintWriter out=new PrintWriter(server.getOutputStream());
            Scanner in=new Scanner(server.getInputStream());
            out.println(inString);
            out.flush();
            String resBSH=in.next();
            in.close();
            out.close();
            server.close();
            return resBSH;

        } catch (IOException e){
            throw new RuntimeException(e);
        }

    }

    /**
     * The setGameDictionaries function takes in an ArrayList of Strings and sets the dictionaries variable to that ArrayList.
     * <p>
     *
     * @param dictionaries<String> - Set the dictionaries arraylist in the class
     *
     */
    public void setGameDictionaries(ArrayList<String> dictionaries) {
        this.dictionaries = new ArrayList<>(dictionaries);
    }

    /**
     * The getDictionaries function returns a string of all the dictionaries that are currently being used by the program.
     *
     * @return A string that contains all the dictionaries in the array
     *
     * @docauthor Trelent
     */
    public String getDictionaries() {
        return String.join(",", dictionaries);
    }
}

