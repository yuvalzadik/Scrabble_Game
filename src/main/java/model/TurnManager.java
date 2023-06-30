package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TurnManager implements Serializable {

    int currentTurnIndex;
    List<Integer> turns;

    public TurnManager(){
        turns = new ArrayList<>();
    }

    public void setTurns(Map<Integer, Player> players){
        turns = players.keySet().stream().sorted(
                (p1,p2)->players.get(p2).getTiles().get(0).score - players.get(p1).getTiles().get(0).score)
                .collect(Collectors.toCollection(ArrayList::new));
        currentTurnIndex = -1;
    }

    public int getCurrentTurn() {
        return turns.get(currentTurnIndex);
    }

    public void nextTurn() {
        currentTurnIndex = (currentTurnIndex+1) % turns.size();
    }

    public List<Integer> getTurns() {
        return turns;
    }

    public int getCurrentTurnIndex() {
        return currentTurnIndex;
    }
}
