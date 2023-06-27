package model;

import scrabble_game.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GameClientHandler implements ClientHandler {
    PrintWriter out;
    BufferedReader in;
    GameManager gameManager;

    BookScrabbleCommunication BScommunication = BookScrabbleCommunication.get_instance();
    boolean stillPlaying;



    public GameClientHandler() {
        this.gameManager = GameManager.get_instance();
        this.stillPlaying = true;
    }

    @Override
    public void handleClient(InputStream inFromclient, OutputStream outToClient) {
        /*
        init inFromClient and outToClient
        loop with two break points - when the turn is not ours or when the player succeeded task:
                         playedId locally - currentPlayer
                         boolean stillPlaying
         */
        in = new BufferedReader(new InputStreamReader(inFromclient));
        out = new PrintWriter(new OutputStreamWriter(outToClient), true);
        out.println("playTurn");
        int playerId = gameManager.turnManager.getCurrentTurn();
        stillPlaying = true;
        System.out.println("Current player -> " + gameManager.turnManager.getCurrentTurn() + " playerID -> " + playerId);
        while(playerId == gameManager.turnManager.getCurrentTurn() && stillPlaying){
            try {
                if(in.ready()){
                    System.out.println("after in.ready");
                    String line = in.readLine();
                    String res = handleInput(line);
                    out.println(res); //reach to listen to host
                    if(res.equals("true")) stillPlaying = false;
                }} catch (IOException ignored){}
        }
    }

    private String handleInput(String input) {
        int playerId = Integer.parseInt(input.split(",")[0]);
        GameCommand command = GameCommandsFactory.getCommandEnumFromChar(input.split(",")[1].charAt(0));
        System.out.println("Server received command:" + command.name() + " from player:" + playerId);
        switch (command) {
            case StartGame: // host start game
                if (playerId == 1) {
                    this.gameManager.startGame();
                    return "true";
                }
                break;
            case TryPlaceWord: // try place word
                int score = this.gameManager.board.tryPlaceWord(buildWordFromPlayer(input));
                if(score == 0){ //board not legal
                    return "boardNotLegal";
                }
                else if(score == -1){ //dictionary not legal
                    return "dictionaryNotLegal";
                }
                else if(score > 0) {//succeeded

                    gameManager.addScore(playerId, score);
                    stillPlaying = false;
                    return "wordInsertSuccessfully";
                }
                break;

            case Challenge:
                String inString = (input.split(",")[1]).toString() + "," + BScommunication.getDictionaries() +  "," + (input.split(",")[2]).toString();
                String resBSH = BScommunication.runChallengeOrQuery(inString);

                if (resBSH.equals("true")){
                    int score1 = this.gameManager.board.tryPlaceWord(buildWordFromPlayer(input));
                    if (score1 > 0) {
                        gameManager.addScore(playerId, score1);
                        stillPlaying = false;
                        return "challengeSucceeded";
                    }
                }

                //if not succeeded
                gameManager.addScore(playerId, -10);
                stillPlaying = false;
                return "challengeFailed";

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
                BScommunication.setGameDictionaries(dictionaries);
                return "true";
        }
        return "false";
    }

    /**
     * TODO: chnge this function that will remove from the player tile array and not from the game bag
     * @param input
     * @return
     */
    private Word buildWordFromPlayer(String input) {
        String[] splittedStr = input.split(",");
        String word = splittedStr[2];
        int col = Integer.parseInt(splittedStr[3]);
        int row = Integer.parseInt(splittedStr[4]);
        boolean vertical = Boolean.parseBoolean(splittedStr[5]);

        Tile[] wordTiles = new Tile[word.length()];
        Player player = gameManager.getPlayers().get(Integer.parseInt(splittedStr[0]));
        for (int i = 0; i < wordTiles.length; i++) {
            if(word.charAt(i) == '_') wordTiles[i] = null;
            else wordTiles[i] = player.getTile(word.charAt(i));
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
