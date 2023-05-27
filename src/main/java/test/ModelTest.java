package test;

import model.BookScrabbleCommunication;
import model.GameManager;
import model.GameMode;
import model.Model;
import scrabble_game.Board;
import scrabble_game.BookScrabbleHandler;
import scrabble_game.MyServer;

public class ModelTest {
    public static void main (String[] args){

        //check connect as a host & Start Game Client Server from the model
        String ip = "localhost";
        int port = 1234;
        Model scrabbleModel = new Model(GameMode.Host, ip, port);
        if (scrabbleModel == null)
            System.out.println("problem with creating the model");


        //check connecting as a guest
        try{
            scrabbleModel.joinGame("Nofar"); //connect as a guest}
        }catch (NullPointerException e){
            System.out.println("problem with connect as a guest");
        }


        //check start game
        scrabbleModel.startGame();
        GameManager gameManager = GameManager.get_instance();
        if(!gameManager.gameStarted)
            System.out.println("problem with start game");


        //check getting stage board
        Board resGetBoard = scrabbleModel.getBoard();
        if (!(resGetBoard == gameManager.board))
            System.out.println("problem with getting the board");


        //Start Book Scrabble Server
        MyServer bookScrabbleServer = new MyServer(5842, new BookScrabbleHandler());
        bookScrabbleServer.start();


        //check place dictionaries
        String checkString = "yuv.txt,s2.txt";
        scrabbleModel.setGameDictionaries("yuv.txt","s2.txt");
        BookScrabbleCommunication BSCommunication = BookScrabbleCommunication.get_instance();
        String dictionariesBSC = BSCommunication.getDictionaries();
        if(dictionariesBSC.compareTo(checkString) != 0)
            System.out.println("problem with set dictionaries");


        //check trying place word on the board
        if(!scrabbleModel.tryPlaceWord("AND", 7,7,true))
            System.out.println("problem with try place word");


        //check challenge the server
        if(!scrabbleModel.challenge("NAL", 8,7,false))
            System.out.println("problem with challenge");


        //check get random letter
        char letter = scrabbleModel.getRand().letter;
        if (90 < letter || letter < 65)
            System.out.println("problem with get random letter");

    }
}
