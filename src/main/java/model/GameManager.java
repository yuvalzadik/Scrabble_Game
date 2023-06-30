package model;

import scrabble_game.Board;
import scrabble_game.Tile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class GameManager implements Serializable {
    public Board board = Board.getBoard();
    public Tile.Bag bag = Tile.Bag.getBag();
    public boolean gameStarted;
    HashMap<Integer, Player> players;
    private static GameManager _instance = null;
    public TurnManager turnManager;

    /**
     * The GameManager function is a singleton class that manages the game.
     * It keeps track of all players, and their respective scores.
     * It also keeps track of whose turn it is, and what the current round number is.
     *
     * @docauthor Trelent
     */
    private GameManager(){
        this.gameStarted = false;
        this.players = new HashMap<>();
        this.turnManager = new TurnManager();
    }

    /**
     * The get_instance function is a static function that returns the singleton instance of GameManager.
     * If no instance exists, it creates one and returns it.
     *
     * @return The singleton instance of the gamemanager class
     *
     */
    public static GameManager get_instance() {
        if (_instance == null) {
            _instance = new GameManager();
        }
        return _instance;
    }


    /**
     * The addPlayer function adds a player to the game.
     *
     * @param  player Add a new player to the game
     *
     */
    public void addPlayer(Player player){
        int newPlayerId = players.size()+1;
        this.players.put(newPlayerId, player);
    }

    /**
     * The startGame function is called when the game is ready to begin.
     * It sets the gameStarted boolean to true, and then fills each player's hand with 7 cards.
     */
    public void startGame(){
        this.gameStarted = true;
        for(Integer playerId : players.keySet()){
            fillHand(playerId);
        }
        turnManager.setTurns(players);
    }

    /**
     * The addScore function adds the score to the player's current score.
     *
     * @param playerId int  Identify which player to add the score to
     * @param score int  Add a score to the player's total score
     *
     */
    public void addScore(int playerId, int score){
        players.get(playerId).addScore(score);
    }

    /**
     * The getPlayers function returns a HashMap of all the players in the game.
     * @return A hashmap of type integer and player
     *
     */
    public HashMap<Integer, Player> getPlayers() {
        return players;
    }

    /**
     * The addTile function adds a tile to the player's hand.
     *
     * @param id int  Identify the player who is adding a tile to their hand
     */
    public void addTile(int id){
        if(players.get(id).getTiles().size() < 7)
            players.get(id).getTiles().add(bag.getRand());
    }

    /**
     * The fillHand function is used to fill the hand of a player when they have less than 7 tiles.
     *
     * @param id int  Identify which player's hand is being filled
     *
     */
    public void fillHand(int id){
        while(players.get(id).getTiles().size() < 7) addTile(id);
    }

    /**
     * The clearHand function is used to clear the hand of a player.
     *
     * @param  playerId int Identify which player's hand is being cleared
     *
     */
    public void clearHand(int playerId) {
        players.get(playerId).setTiles(new ArrayList<>());
    }
}
