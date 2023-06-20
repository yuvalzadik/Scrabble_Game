package test;

import model.BookScrabbleCommunication;
import model.GameMode;
import model.Model;
import scrabble_game.BookScrabbleHandler;
import scrabble_game.MyServer;

import java.util.ArrayList;
import java.util.List;

public class ModelPlayTest {
    public static void main(String[] args) {
        MyServer bookScrabbleServer = new MyServer(6789, new BookScrabbleHandler());
        bookScrabbleServer.start();

        Model modelHost = new Model(GameMode.Host, "localhost", 12345,"Tamar");
        try{Thread.sleep(3000);}catch (InterruptedException ignored){};
        Model modelGuest = new Model(GameMode.Guest, "localhost", 12345,"Lior");
        try{Thread.sleep(3000);}catch (InterruptedException ignored){};
        ArrayList<String> dictionaries = new ArrayList<>();
        dictionaries.add("alice_in_wonderland.txt");
        dictionaries.add("Frank Herbert - Dune.txt");
        BookScrabbleCommunication.get_instance().setGameDictionaries(dictionaries);
        modelHost.startGame();
        modelHost.tryPlaceWord("NAL", 7,7,false);



    }
}
