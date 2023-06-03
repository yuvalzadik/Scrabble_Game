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
        int port = 2345;
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




        //Start Book Scrabble Server
        MyServer bookScrabbleServer = new MyServer(5842, new BookScrabbleHandler());
        bookScrabbleServer.start();


        //check place dictionaries
        String checkString = "alice_in_wonderland.txt,Frank Herbert - Dune.txt";
        scrabbleModel.setGameDictionaries("alice_in_wonderland.txt","Frank Herbert - Dune.txt");
        BookScrabbleCommunication BSCommunication = BookScrabbleCommunication.get_instance();
        String dictionariesBSC = BSCommunication.getDictionaries();
        if(dictionariesBSC.compareTo(checkString) != 0) {
            System.out.println("problem with set dictionaries");
        }


        //check trying place word on the board
        if(!scrabbleModel.tryPlaceWord("IN", 7,7,false)) {
            System.out.println("problem with try place word");
        }

        //check getting stage board ->
        // print the board saved in game manager and print the board we got from client  request
        Board boardDirect = Board.getBoard();
        Board resGetBoard = scrabbleModel.getBoard();
        System.out.println("this is the board direct - ");
        Board.printBoard(boardDirect);
        System.out.println("this is the board from GetBoard command - ");
        Board.printBoard(resGetBoard);


        //check challenge the server
        if(!scrabbleModel.challenge("INUN", 7,7,true))
            System.out.println("problem with challenge");


        //check get random letter
        char letter = scrabbleModel.getRand().letter;
        if (90 < letter || letter < 65)
            System.out.println("problem with get random letter");

    }
}
