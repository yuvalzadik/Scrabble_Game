package model;

import scrabble_game.Tile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    private int score;
    private final String name;

    private List<Tile> playerTiles ;

    public Player(String name){
        this.name = name;
        this.score = 0;
        playerTiles = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getScore(){
        return this.score;
    }
    public void addScore(int score){ this.score += score;}

    public List<Tile> getTiles() {
        return playerTiles;
    }

    public Tile getTile(char letter){
        ArrayList<Tile> newTiles = new ArrayList<>(playerTiles);
        for(Tile t : newTiles){
            if(t.letter == letter){
                playerTiles.remove(t);
                return t;
            }
        }
        return null;
    }

    public void setTiles(List<Tile> tiles){
        this.playerTiles = tiles;
    }

}
