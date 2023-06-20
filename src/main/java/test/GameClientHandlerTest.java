package test;

import model.*;
import scrabble_game.Board;
import scrabble_game.BookScrabbleHandler;
import scrabble_game.MyServer;
import scrabble_game.Tile;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class GameClientHandlerTest {
    public static void main(String[] args){

        /*
         * we check directly handleClient function this way - we write on the terminal the string and get the right
         * result - for example we write the string "0,D,yuv.txt, dictionary.txt,endDictionaries, AND"
         * and got the right answer from the server:
         * Server received command:SetGameDictionaries from player:0, true
         * THE CODE:
         * GameClientHandler GCHandler = new GameClientHandler();
         * InputStream inputStream = System.in;
         * OutputStream outputStream = System.out;
         * GCHandler.handleClient(inputStream, outputStream);
         * */

        //check connect as a host & Start Game Client Server from the model
        String ip = "localhost";
        int port = 1234;
        Model scrabbleModel = new Model(GameMode.Host, ip, port, "Tamar");
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
        String checkString = "alice_in_wonderland.txt,Frank Herbert - Dune.txt";
        scrabbleModel.setGameDictionaries("alice_in_wonderland.txt","Frank Herbert - Dune.txt");
        BookScrabbleCommunication BSCommunication = BookScrabbleCommunication.get_instance();
        String dictionariesBSC = BSCommunication.getDictionaries();
        if(dictionariesBSC.compareTo(checkString) != 0)
            System.out.println("problem with set dictionaries");


        //check trying place word on the board
//        if(!scrabbleModel.tryPlaceWord("IN", 7,7,false))
//            System.out.println("problem with try place word");


        //check challenge the server
        if(!scrabbleModel.challenge("INUN", 7,7,true))
            System.out.println("problem with challenge");


        //check get random letter
        char letter = scrabbleModel.getRand().letter;
        if (90 < letter || letter < 65)
            System.out.println("problem with get random letter");

    }


}
