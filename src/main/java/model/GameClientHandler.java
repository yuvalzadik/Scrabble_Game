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



    /**
     * The GameClientHandler function is responsible for handling the client's requests.
     * It receives a request from the client, and sends back an appropriate response.
     * @docauthor Trelent
     */
    public GameClientHandler() {
        this.gameManager = GameManager.get_instance();
        this.stillPlaying = true;
    }

    /**
     * The handleClient function is the main function of the ClientHandler class.
     * It handles all communication between a client and server, and it also calls
     * other functions in order to handle specific tasks. The function takes two parameters:
     * an InputStream from which it reads data sent by the client, and an OutputStream to which it writes data that will be sent back to the client.
     * @param  inFromclient - InputStream  Read the input from the client
     * @param outToClient - OutputStream  Send data to the client
     */
    @Override
    public void handleClient(InputStream inFromclient, OutputStream outToClient) {
        in = new BufferedReader(new InputStreamReader(inFromclient));
        out = new PrintWriter(new OutputStreamWriter(outToClient), true);
        out.println("playTurn");
        int playerId = gameManager.turnManager.getCurrentTurn();
        stillPlaying = true;
        System.out.println("Current player -> " + gameManager.turnManager.getCurrentTurn() + " playerID -> " + playerId);
        while(playerId == gameManager.turnManager.getCurrentTurn() && stillPlaying){
            try {
                if(in.ready()){
                    String line = in.readLine();
                    String res = handleInput(line);
                    out.println(res); //reach to listen to host
                    if(res.equals("true")) stillPlaying = false;
                }} catch (IOException ignored){}
        }
    }

    /**
     * The handleInput function is responsible for handling the input from the client.
     * It receives a string, which represents an action that was performed by one of the players.
     * The function then parses this string and performs all necessary actions in order to update
     * both clients' boards accordingly. After performing these actions, it returns a response to be sent back to the client who sent this request.

     *
     * @param input String  Get the player id and command from the client
     *
     * @return  a string value according the specific command  switch case result;

     */
    private String handleInput(String input) {
        int playerId = Integer.parseInt(input.split(",")[0]);
        Board.printBoard(gameManager.board);
        System.out.println("tile in [7][7] -> " + gameManager.board.getTiles()[7][7]);
        System.out.println("tile in [7][8] -> " + gameManager.board.getTiles()[7][8]);
        System.out.println("tile in [8][7] -> " + gameManager.board.getTiles()[8][7]);
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

            case SkipTurn: return "true";
            case SwapTiles:
                gameManager.clearHand(playerId);
                gameManager.fillHand(playerId);
                return "true";
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
     * The buildWordFromPlayer function takes in a string input from the client and parses it into a Word object.
     * The function first splits the input string by commas, which separates each piece of information about the word.
     * It then assigns each piece of information to its respective variable (word, row, col, vertical).
     * Next it creates an array of Tiles that will be used to create our Word object. This is done by looping through
     * every character in our word String and creating a Tile for each one using player's getTile method (which returns
     * null if there is no tile at that index).
     *
     * @param input String  Get the word, row, column and vertical orientation of the word
     *
     * @return A word object
     *
     * @docauthor Trelent
     */
    private Word buildWordFromPlayer(String input) {
        String[] splittedStr = input.split(",");
        String word = splittedStr[2];
        int row = Integer.parseInt(splittedStr[3]);
        int col = Integer.parseInt(splittedStr[4]);
        boolean vertical = Boolean.parseBoolean(splittedStr[5]);
        System.out.println("Vertical? -> " + vertical);

        Tile[] wordTiles = new Tile[word.length()];
        Player player = gameManager.getPlayers().get(Integer.parseInt(splittedStr[0]));
        for (int i = 0; i < wordTiles.length; i++) {
            if(word.charAt(i) == '_') wordTiles[i] = null;
            else wordTiles[i] = player.getTile(word.charAt(i));
        }

        return new Word(wordTiles, row, col, vertical);
    }

    /**
     * The close function closes the input and output streams.
     */
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
