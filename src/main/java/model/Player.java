package model;

import scrabble_game.Tile;

import java.util.ArrayList;
import java.util.List;

public class Player {
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

    public void setTiles(List<Tile> tiles){
        this.playerTiles = tiles;
    }

}
