package test;

import model.GameMode;
import model.Model;

public class HostMain {
    public static void main(String[] args) {
        System.out.println("Scrabble Game!");

        String ip = "localhost";
        int port = 1234;
        Model scrabbleModel = new Model(GameMode.Host, ip, port);
        scrabbleModel.joinGame("Nofar");

        scrabbleModel.startGame();
        scrabbleModel.tryPlaceWord("Bla", 1,2,true);
        scrabbleModel.getBoard();
        scrabbleModel.setGameDictionaries("dictionary1", "dictionary2");
        System.out.println(scrabbleModel.getRand().letter);
    }
}
