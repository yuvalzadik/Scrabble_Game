package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import scrabble_game.Board;
import scrabble_game.MyServer;
import scrabble_game.Tile;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Observable;
import java.util.Scanner;

public class Model extends Observable {
    HashMap<String, String> properties;
    String ip;
    int port;
    Socket fg;
    PrintWriter out2fg;
    Scanner fgin;
    HostServer gameServer;

    GameManager gameManager;

    static int currentId = 0;
    int playerId;
    private StringProperty messageFromHost = new SimpleStringProperty();;


    public Model(GameMode mode, String ip, int port, String name) {
        //this.ip = ip;
        //this.port = port;
        gameManager = null;
        if (mode.equals(GameMode.Host)) {
            gameManager = GameManager.get_instance();
            gameServer = new HostServer(port, new GameClientHandler()); //TODO - Change to hostServer
            gameServer.start();
        }
        try {
            fg = new Socket(ip, port);
            out2fg = new PrintWriter(fg.getOutputStream(), true);
            fgin = new Scanner(fg.getInputStream());
            out2fg.println(name);
            currentId++;
            this.playerId = currentId;
            listenToHost();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void listenToHost(){
        new Thread(() -> {
            while(fg.isConnected()){
                if(fgin.hasNext()){
                    String messageFromHost = fgin.next();
                    switch(messageFromHost){ //action according to server response
                        case "playTurn"-> playTurn();
                        //challenge
                        case "challengeSucceeded"-> System.out.println("trueMeyuhad");
                        case "challengeFailed"-> System.out.println("trueMeyuhad");

                        //tryPlaceWord
                        case "wordInsertSuccessfully"-> wordInsertSuccessfully();
                        case "boardNotLegal"-> System.out.println("boardNotLegal");
                        case "dictionaryNotLegal"-> System.out.println("dictionaryNotLegal");

//                      TODO: handle end game- log out succeeded, handle try place word- boardLegal/ wordLegal,
//                       handle challenge- word not found, , its your turn
                        //TODO: each time broadcast message
                    }
                }
            }
        }).start();
    }

    private void playTurn() {
        /*
        Show buttons for current player.
         */
    }

    private  void wordInsertSuccessfully(){
        messageFromHost.setValue("wordInsertSuccessfully");
        System.out.println("work insert successfully");
    }


    // TODO: Change below code after adding thread, also remove prints
    private String runCommand(String commandString) {
//        try {
//            fg = new Socket(ip, port);
//            out2fg = new PrintWriter(fg.getOutputStream());
//            fgin = new Scanner(fg.getInputStream());
        System.out.println("runCommand");
        out2fg.println(commandString);
//            String res = fgin.next();
//            System.out.println("Recieved from server: " + res);
//            fgin.close();
//            out2fg.close();
//            fg.close();

            //return res;
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        return "";
    }

    public boolean joinGame(String name) {
        String joinString = GameCommandsFactory.getJoinGameCommandString(name);
        String res = runCommand(joinString);
        if (Integer.parseInt(res) >= 0) {
            playerId = Integer.parseInt(res);
            return true;
        }
        return false;
    }

//    public boolean startGame() {
//        String startGameString = GameCommandsFactory.getStartGameCommandString(playerId);
//        String res = runCommand(startGameString);
//        return Boolean.parseBoolean(res);
//    }

    public void tryPlaceWord(String word, int row, int col, boolean vertical) {
        System.out.println("Model -> tryPlaceWord");
        String tryPlaceWordQuery = GameCommandsFactory.getTryPlaceWordCommandString(playerId, word, row, col, vertical);
        runCommand(tryPlaceWordQuery);
    }

    public boolean challenge(String word, int row, int col, boolean vertical) {
        String challengeWordQuery =  GameCommandsFactory.getChallengeCommandString(playerId, word, row, col, vertical);
        String res = runCommand(challengeWordQuery);
        return Boolean.parseBoolean(res);
    }

    public Board getBoard() {
        String getBoardString = GameCommandsFactory.getGetBoardCommandString(playerId);
        String res = runCommand(getBoardString);
        String[] byteValues = res.substring(1, res.length() - 1).split(",");
        byte[] bytes = new byte[byteValues.length];
        for (int i = 0, len = bytes.length; i < len; i++) {
            bytes[i] = Byte.parseByte(byteValues[i].trim());
        }
        return Board.deserialize(bytes);
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public Tile getRand() {
        String getBagString = GameCommandsFactory.getGetRandTileString(playerId);
        String res = runCommand(getBagString);
        String[] byteValues = res.substring(1, res.length() - 1).split(",");
        byte[] bytes = new byte[byteValues.length];
        for (int i = 0, len = bytes.length; i < len; i++) {
            bytes[i] = Byte.parseByte(byteValues[i].trim());
        }
        return Tile.deserialize(bytes);
    }

    public void setGameDictionaries(String... dictionaries){
        String getSetGameDictionariesString = GameCommandsFactory.getSetGameDictionariesString(playerId, dictionaries);
        runCommand(getSetGameDictionariesString);
    }




    public StringProperty getMessageFromHost() {
        return messageFromHost;
    }
}