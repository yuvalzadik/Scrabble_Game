package test;

import model.BookScrabbleCommunication;
import model.GameClientHandler;
import model.GameMode;
import model.Model;
import scrabble_game.BookScrabbleHandler;
import scrabble_game.MyServer;

import java.util.ArrayList;

public class HostMain {
    public static void main(String[] args) {
        System.out.println("Scrabble Game!");

        String ip = "localhost";
        int port = 1234;
        Model scrabbleModel = new Model(GameMode.Host, ip, port);
        MyServer bookScrabbleServer = new MyServer(5842, new BookScrabbleHandler());
        bookScrabbleServer.start();
        scrabbleModel.setGameDictionaries("yuv.txt","s2.txt");
        scrabbleModel.joinGame("Nofar");

        scrabbleModel.startGame();

        //scrabbleModel.getBoard();
        scrabbleModel.tryPlaceWord("AND", 7,7,true);



       scrabbleModel.challenge("andy", 2,3,true);

        System.out.println(scrabbleModel.getRand().letter);
    }
}
