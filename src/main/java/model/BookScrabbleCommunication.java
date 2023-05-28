package model;

import scrabble_game.DictionaryManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class BookScrabbleCommunication {

    final String BookScrabbleIP= "localhost";

    final int BookScrabblePort= 5842;

    private static BookScrabbleCommunication _instance = null;

    ArrayList<String> dictionaries;


    private BookScrabbleCommunication() {}

    public static BookScrabbleCommunication get_instance() {
        if (_instance == null) {
            _instance = new BookScrabbleCommunication();
        }
        return _instance;
    }

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

    public void setGameDictionaries(ArrayList<String> dictionaries) {
        this.dictionaries = new ArrayList<>(dictionaries);
    }

    public String getDictionaries() {
        return String.join(",", dictionaries);
    }
}

