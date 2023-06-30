package model;

import scrabble_game.Tile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    private int score;
    private final String name;

    private List<Tile> playerTiles ;

    /**
     * The Player function is a constructor for the Player class. It takes in a String name and sets it as the player's name,
     * sets their score to 0, and creates an ArrayList of Tiles called playerTiles.

     *
     * @param name String  Set the name of the player
     */
    public Player(String name){
        this.name = name;
        this.score = 0;
        playerTiles = new ArrayList<>();
    }

    /**
     * The getName function returns the name of the person.
     * <p>
     *
     *
     * @return The name of the student
     */
    public String getName() {
        return name;
    }

    /**
     * The getScore function returns the score of the player.
     * <p>
     *
     *
     * @return The score of the player
     */
    public int getScore(){
        return this.score;
    }
    /**
     * The addScore function adds the score parameter to the current score.
     * <p>
     *
     * @param score int  Add the score to the current score
     */
    public void addScore(int score){ this.score += score;}

    /**
     * The getTiles function returns a list of the tiles that are currently in the player's hand.
     *
     *
     * @return A list of the player's tiles
     */
    public List<Tile> getTiles() {
        return playerTiles;
    }

    /**
     * The getTile function takes in a character and returns the tile with that letter.
     *
     *
     * @param  letter char Find the tile with that letter
     *

     */
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

    /**
     * The setTiles function is used to set the tiles of a player.
     * <p>
     *
     * @param tiles - Set the playertiles variable to the tiles parameter

     */
    public void setTiles(List<Tile> tiles){
        this.playerTiles = tiles;
    }

}
