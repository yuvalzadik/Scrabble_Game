package model;

import scrabble_game.*;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class GameClientHandler implements ClientHandler {
    PrintWriter out;
    BufferedReader in;
    GameManager gameManager;

    BookScrabbleCommunication BScommunication = BookScrabbleCommunication.get_instance();


    public GameClientHandler() {
        this.gameManager = GameManager.get_instance();
    }

    @Override
    public void handleClient(InputStream inFromclient, OutputStream outToClient) {
        try {
            in = new BufferedReader(new InputStreamReader(inFromclient));
            String line = in.readLine();
            String res = handleInput(line);
            out = new PrintWriter(outToClient, true);
            out.println(res + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String handleInput(String input) {
        int playerId = Integer.parseInt(input.split(",")[0]);
        GameCommand command = GameCommandsFactory.getCommandEnumFromChar(input.split(",")[1].charAt(0));
        System.out.println("Server received command:" + command.name() + " from player:" + playerId);
        switch (command) {
            case JoinGame: // add new player
                Player newPlayer = new Player(input.split(",")[2]);
                int newPlayerId = this.gameManager.addPlayer(newPlayer);
                return String.valueOf(newPlayerId);
            case StartGame: // host start game
                if (playerId == 0) {
                    this.gameManager.startGame();
                    return "true";
                }
                break;
            case TryPlaceWord: // try place word
                int score = this.gameManager.board.tryPlaceWord(buildWordFromInput(input));
                gameManager.addScore(playerId, score);
                if (score > 0)
                    return "true";
                break;
            case Challenge:
                String inString = (input.split(",")[1]).toString() + "," + gameManager.getDictionaries() +  "," + (input.split(",")[2]).toString();
                String resBSH = BScommunication.runChallengeOrQuery(inString);

                if (resBSH.equals("true")){
                    int score1 = this.gameManager.board.tryPlaceWord(buildWordFromInput(input));
                    gameManager.addScore(playerId, score1);
                    if (score1 > 0)
                        return "true";
                }
                else{
                    gameManager.addScore(playerId, -10);
                    return "false";
                }

//                break;
            case GetRandTile: // return rand tile
                byte[] bagBytes = Tile.serialize(Tile.Bag.getBag().getRand());
                return Arrays.toString(bagBytes);
            case GetBoard: // return board
                byte[] boardBytes = Board.serialize(Board.getBoard());
                return Arrays.toString(boardBytes);
            case SetGameDictionaries: //set dictionaries at game manager
                ArrayList<String> dictionaries = new ArrayList<>();
                String[] inputArr = input.split(",");
                int i=2; //skip id and command
                while(!inputArr[i].equals("endDictionaries")){
                    dictionaries.add(inputArr[i]);
                    i++;
                }
                gameManager.setGameDictionaries(dictionaries);
                return "true";
        }
        return "false";
    }

    private Word buildWordFromInput(String input) {
        String word = input.split(",")[2];
        int col = Integer.parseInt(input.split(",")[3]);
        int row = Integer.parseInt(input.split(",")[4]);
        boolean vertical = Boolean.parseBoolean(input.split(",")[5]);

        Tile[] wordTiles = new Tile[word.length()];
        for (int i = 0; i < wordTiles.length; i++) {
            wordTiles[i] = Tile.Bag.getBag().getTile(word.charAt(i));
        }

        return new Word(wordTiles, row, col, vertical);
    }

    @Override
    public void close() {
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
