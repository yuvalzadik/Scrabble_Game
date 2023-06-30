package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import scrabble_game.Board;
import scrabble_game.Tile;

import java.io.*;
import java.net.Socket;
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
    int playerId;
    private StringProperty messageFromHost = new SimpleStringProperty();


    /**
     * The Model function is the constructor for the Model class.
     * It takes in a GameMode, an IP address, a port number and a name as parameters.
     * If it is in Host mode, it creates an instance of GameManager and starts up the server on that port number with
     * its own client handler. Then it connects to itself using that same ip address and port number to create its own socket connection
     * with itself (fg). It then sends out its name through this socket connection so that when another player joins later on they will know who's turn it is first.

     *
     * @param mode GameMode  Determine if the client is a host or not
     * @param ip String  Connect to the server
     * @param port int  Create a new hostserver object
     * @param name String  Identify the client to the server
    public model(gamemode mode, string ip, int port) {
            this(mode, ip, port,&quot;&quot;);
        }

        public void listentohost() {

            thread t = new thread(new runnable() {

     *
     */
    public Model(GameMode mode, String ip, int port, String name) {
        gameManager = null;
        if (mode.equals(GameMode.Host)) {
            gameManager = GameManager.get_instance();
            gameServer = new HostServer(port, new GameClientHandler());
            gameServer.start();
        }
        try {
            fg = new Socket(ip, port);
            out2fg = new PrintWriter(fg.getOutputStream(), true);
            fgin = new Scanner(fg.getInputStream());
            out2fg.println(name);
            String givenId = fgin.next();
            this.playerId = Integer.parseInt(givenId);
            listenToHost();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The listenToHost function is a thread that listens to the host and performs actions according to the server response.
     * The function uses switch case in order to perform different actions according to the server response.
     *
     * @docauthor Trelent
     */
    private void listenToHost(){
        new Thread(() -> {
            while(fg.isConnected()){
                if(fgin.hasNext()){
                    String messageFromHost = fgin.next();
                    switch(messageFromHost){ //action according to server response
                        case "playTurn"-> playTurn();
                        case "bindButtons" -> bindButtons();
                        //challenge
                        case "challengeSucceeded"-> System.out.println("Challenge Succeeded - Word is placed on board.");
                        case "challengeFailed"-> System.out.println("Challenge Failed - You lost 10 points!");

                        //tryPlaceWord
                        case "wordInsertSuccessfully"-> wordInsertSuccessfully();
                        case "boardNotLegal"-> System.out.println("boardNotLegal");
                        case "dictionaryNotLegal"-> dictionaryNotLegal();

                    }
                }
            }
        }).start();
    }

    /**
     * The bindButtons function binds the buttons to their respective functions.
     *
     * @docauthor Trelent
     */
    private void bindButtons() {
        messageFromHost.setValue("bindButtons");
    }

    /**
     * The dictionaryNotLegal function is called when the user attempts to load a dictionary that does not meet the requirements of being a legal dictionary.
     * The function sets the messageFromHost value to &quot;dictionaryNotLegal&quot; so that it can be displayed in an alert box on the client side.

     *
     * @docauthor Trelent
     */
    private void dictionaryNotLegal() {
        messageFromHost.setValue("dictionaryNotLegal");
    }

    /**
     * The playTurn function is called when the current player's turn begins.
     * It shows the buttons for current player and hides all other buttons.

     *
     * @docauthor Trelent
     */
    private void playTurn() {
        messageFromHost.setValue("playTurn");
    }

    /**
     * The wordInsertSuccessfully function is called when the user has successfully inserted a word into the database.
     * It sets the messageFromHost MutableLiveData object to &quot;wordInsertSuccessfully&quot; so that it can be observed by
     * other classes and used to display a Toast message on screen.

     * <p>
     *
     * @docauthor Trelent
     */
    private  void wordInsertSuccessfully(){
        messageFromHost.setValue("wordInsertSuccessfully");
        System.out.println("word insert successfully");
    }

    /**
     * The runCommand function is used to send a command to FlightGear.
     *
     * @param commandString String Send a command to the flightgear application
     *
     * @return A string
     *
     * @docauthor Trelent
     */
    private String runCommand(String commandString) {
        System.out.println("runCommand");
        out2fg.println(commandString);
        return "";
    }

    /**
     * The joinGame function is used to join a game.
     *
     *
     * @param name String  Identify the player
     *
     * @return A boolean value
     *
     */
    public boolean joinGame(String name) {
        String joinString = GameCommandsFactory.getJoinGameCommandString(name);
        String res = runCommand(joinString);
        if (Integer.parseInt(res) >= 0) {
            playerId = Integer.parseInt(res);
            return true;
        }
        return false;
    }

    /**
     * The tryPlaceWord function takes in a word, row, column and vertical boolean.
     * It then creates a query string using the GameCommandsFactory class to create
     * the tryPlaceWord command. The function then runs this command on the server
     * by calling runCommand with this query string as an argument.

     *
     * @param word String Specify the word to be placed
     * @param row int  Specify the row in which to place the word
     * @param col int  Determine the column where the word is to be placed
     * @param vertical boolean  Determine if the word is placed vertically or horizontally
     *
     */
    public void tryPlaceWord(String word, int row, int col, boolean vertical) {
        String tryPlaceWordQuery = GameCommandsFactory.getTryPlaceWordCommandString(playerId, word, row, col, vertical);
        runCommand(tryPlaceWordQuery);
    }

    /**
     * The challenge function is used to challenge a word that has been played by the opponent.
     * The function returns true if the challenged word is valid, and false otherwise.
     *
     *
     * @param  word Specify the word that is being challenged
     * @param row Specify the row of the first letter in a word
     * @param col Specify the column of the first letter in a word
     * @param  vertical Determine if the word is placed vertically or horizontally
     *
     * @return A boolean
     *
     */
    public boolean challenge(String word, int row, int col, boolean vertical) {
        String challengeWordQuery =  GameCommandsFactory.getChallengeCommandString(playerId, word, row, col, vertical);
        String res = runCommand(challengeWordQuery);
        return Boolean.parseBoolean(res);
    }

    /**
     * The getBoard function returns the current board state of the game.
     *
     *
     *
     * @return The board as a byte array
     *
     */
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

    /**
     * The getGameManager function returns the gameManager object.
     *
     * @return The gamemanager object
     *
     */
    public GameManager getGameManager() {
        return gameManager;
    }

    /**
     * The setGameManager function is used to set the gameManager variable.
     *
     * @param gameManager Set the gamemanager variable
     *
     */
    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    /**
     * The getRand function is used to get a random tile from the bag.
     *
     * @return A tile object
     *
     */
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

    /**
     * The setGameDictionaries function sets the dictionaries that will be used in the game.
     *
     * @param dictionaries Pass in a variable number of arguments
     *
     */
    public void setGameDictionaries(String... dictionaries){
        String getSetGameDictionariesString = GameCommandsFactory.getSetGameDictionariesString(playerId, dictionaries);
        runCommand(getSetGameDictionariesString);
    }

    /**
     * The getPlayerId function returns the playerId of the Player object.
     *
     * @return The playerid of the current player object
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * The getMessageFromHost function is a getter function that returns the messageFromHost property.
     *
     * @return A string property
     */
    public StringProperty getMessageFromHost() {
        messageFromHost = new SimpleStringProperty();
        return messageFromHost;
    }

    /**
     * The skipTurn function is used to skip a player's turn.
     * It takes no arguments and returns nothing.
     */
    public void skipTurn() {
        String skipTurnString =  GameCommandsFactory.getSkipTurnString(playerId);
        runCommand(skipTurnString);
    }

    /**
     * The swapTiles function is used to swap the tiles in a player's hand with new tiles from the bag.
     * The function takes no parameters and returns nothing. It simply sends a command to the server,
     * which then updates all the clients' games accordingly.

     */
    public void swapTiles(){
        String swapTilesString =  GameCommandsFactory.getSwapTilesString(playerId);
        runCommand(swapTilesString);
    }
}