package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TurnManager implements Serializable {

    int currentTurnIndex;
    List<Integer> turns;

    /**
     * The TurnManager function is used to keep track of the turns in a game.
     * It keeps track of the current turn, and also allows for adding new turns.

     * <p>
     *
     * @docauthor Trelent
     */
    public TurnManager(){
        turns = new ArrayList<>();
    }

    /**
     * The setTurns function takes in a map of players and their player numbers,
     * then sorts the players by their highest tile score. The sorted list is stored
     * as an ArrayList called turns. The currentTurnIndex is set to - 1 so that when
     * getNextPlayer() is called for the first time, it will return the first player in
     * turns (the one with the highest tile score). This function should be called at
     * least once before calling getNextPlayer(). If this function isn't used, then there's no way to know who goes next!
     *
     * @param players Sort the players in order of the highest score to lowest
     *
     * @docauthor Trelent
     */
    public void setTurns(Map<Integer, Player> players){
        turns = players.keySet().stream().sorted(
                (p1,p2)->players.get(p2).getTiles().get(0).score - players.get(p1).getTiles().get(0).score)
                .collect(Collectors.toCollection(ArrayList::new));
        currentTurnIndex = -1;
    }

    /**
     * The getCurrentTurn function returns the current turn of the game.
     * <p>
     *
     *
     * @return The current turn
     *
     * @docauthor Trelent
     */
    public int getCurrentTurn() {
        return turns.get(currentTurnIndex);
    }

    /**
     * The nextTurn function increments the currentTurnIndex by 1, and then uses the modulus operator to ensure that it is within bounds.
     * <p>
     *
     *
     * @docauthor Trelent
     */
    public void nextTurn() {
        currentTurnIndex = (currentTurnIndex+1) % turns.size();
    }

    /**
     * The getTurns function returns the list of turns that have been made in the game.
     * <p>
     *
     *
     * @return A list of integers that represents the turns taken by the player
     *
     * @docauthor Trelent
     */
    public List<Integer> getTurns() {
        return turns;
    }

    /**
     * The getCurrentTurnIndex function returns the currentTurnIndex variable.
     * <p>
     *
     *
     * @return The index of the current turn
     *
     * @docauthor Trelent
     */
    public int getCurrentTurnIndex() {
        return currentTurnIndex;
    }
}
