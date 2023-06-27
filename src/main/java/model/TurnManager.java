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
        currentTurnIndex = -1;
    }

    public void setTurns(Map<Integer, Player> playes){
        turns = playes.keySet().stream().sorted(
                (p1,p2)->playes.get(p2).getTiles().get(0).score - playes.get(p1).getTiles().get(0).score)
                .collect(Collectors.toCollection(ArrayList::new));
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
}
