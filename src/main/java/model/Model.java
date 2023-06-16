package model;

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

    static int currentId = 0;
    int playerId;

    public Model(GameMode mode, String ip, int port, String name) {
        //this.ip = ip;
        //this.port = port;
        if (mode.equals(GameMode.Host)) {
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
                        case "hey" -> System.out.println("Received message from host!" + this.playerId);
//                      TODO: handle end game- log out succeeded, handle try place word- boardLegal/ wordLegal,
//                       handle challenge- word not found, , its your turn
                        //TODO: each time broadcast message


                    }
                }
            }
        }).start();
    }


    // TODO: Change below code after adding thread, also remove prints
    private String runCommand(String commandString) {
//        try {
//            fg = new Socket(ip, port);
//            out2fg = new PrintWriter(fg.getOutputStream());
//            fgin = new Scanner(fg.getInputStream());

            out2fg.println(commandString);
            String res = fgin.next();
            System.out.println("Recieved from server: \n" + res);
//            fgin.close();
//            out2fg.close();
//            fg.close();

            return res;
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

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

    public void startGame(){
        gameServer.startGame();
        GameManager.get_instance().startGame();
    }

    public boolean tryPlaceWord(String word, int row, int col, boolean vertical) {
        String tryPlaceWordQuery = GameCommandsFactory.getTryPlaceWordCommandString(playerId, word, row, col, vertical);
        String res = runCommand(tryPlaceWordQuery);
        return Boolean.parseBoolean(res);
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


}