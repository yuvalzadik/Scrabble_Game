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

    private GameManager(){
        this.gameStarted = false;
        this.players = new HashMap<>();
        this.turnManager = new TurnManager();
    }

    public static GameManager get_instance() {
        if (_instance == null) {
            _instance = new GameManager();
        }
        return _instance;
    }


    public void addPlayer(Player player){
        int newPlayerId = players.size()+1;
        this.players.put(newPlayerId, player);
    }

    public void startGame(){
        this.gameStarted = true;
        for(Integer playerId : players.keySet()){
            fillHand(playerId);
        }
        turnManager.setTurns(players);
    }

    public void addScore(int playerId, int score){
        players.get(playerId).addScore(score);
    }

    public HashMap<Integer, Player> getPlayers() {
        return players;
    }

    public void addTile(int id){
        if(players.get(id).getTiles().size() < 7)
            players.get(id).getTiles().add(bag.getRand());
    }

    public void fillHand(int id){
        while(players.get(id).getTiles().size() < 7) addTile(id);
    }

    public void clearHand(int playerId) {
        players.get(playerId).setTiles(new ArrayList<>());
    }
}
