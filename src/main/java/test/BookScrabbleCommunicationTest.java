package test;

import model.BookScrabbleCommunication;
import scrabble_game.BookScrabbleHandler;
import scrabble_game.MyServer;

import java.util.ArrayList;

public class BookScrabbleCommunicationTest {

    public static void main(String[] args){
        //Start the  Server
        MyServer bookScrabbleServer = new MyServer(6789, new BookScrabbleHandler());
        bookScrabbleServer.start();

        BookScrabbleCommunication BSCommunication = BookScrabbleCommunication.get_instance();


        //set the dictionaries using function setGameDictionaries
        String checkString = "alice_in_wonderland.txt,Frank Herbert - Dune.txt";
        ArrayList<String> dictionaries = new ArrayList<>();
        dictionaries.add("alice_in_wonderland.txt");
        dictionaries.add("Frank Herbert - Dune.txt");
        BSCommunication.setGameDictionaries(dictionaries);
        String dictionariesBSC = BSCommunication.getDictionaries(); //check get dictionaries function
        if(dictionariesBSC.compareTo(checkString) != 0)
            System.out.println("problem with set dictionaries");


        //check function runChallengeOrQuery
        //challenge
        if(!Boolean.parseBoolean(BSCommunication.runChallengeOrQuery("C,alice_in_wonderland.txt,Frank Herbert - Dune.txt,NAL")))
            System.out.println("problem with function runChallengeOrQuery");

        //query
        if(!Boolean.parseBoolean(BSCommunication.runChallengeOrQuery("Q,alice_in_wonderland.txt,Frank Herbert - Dune.txt,NAL")))
            System.out.println("problem with function runChallengeOrQuery");

        System.out.println("success!");
    }





}
