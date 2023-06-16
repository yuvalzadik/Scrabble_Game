package model;

import scrabble_game.Board;
import scrabble_game.DictionaryManager;
import scrabble_game.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GameManager {
    static public int MAX_PLAYERS = 4;
    public Board board = Board.getBoard();
    public Tile.Bag bag = Tile.Bag.getBag();
    public boolean gameStarted;
    HashMap<Integer, Player> players;
    private static GameManager _instance = null;

    public GameManager(){
        this.gameStarted = false;
        this.players = new HashMap<>();
    }

    public static GameManager get_instance() {
        if (_instance == null) {
            _instance = new GameManager();
        }
        return _instance;
    }


    public int addPlayer(Player player){
        if (!gameStarted && players.size() < MAX_PLAYERS) {
            int newPlayerId = players.size()+1;
            this.players.put(newPlayerId, player);
            return newPlayerId;
        }
        return -1;
    }

    public void startGame(){
        this.gameStarted = true;
    }

    public void addScore(int playerId, int score){
        players.get(playerId).addScore(score);
    }

    public HashMap<Integer, Player> getPlayers() {
        return players;
    }


}
