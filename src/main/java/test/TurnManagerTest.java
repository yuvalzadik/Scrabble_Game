package test;

import model.GameManager;
import model.Player;

public class TurnManagerTest {
    public static void main(String[] args) {
        GameManager gm = GameManager.get_instance();
        gm.addPlayer(new Player("tamar"));
        gm.addPlayer(new Player("lior"));
        gm.addPlayer(new Player("uv"));
        gm.addPlayer(new Player("nofi"));


        gm.startGame();

        for(int key: gm.getPlayers().keySet()){

            System.out.println("player "+ key +" tile is - " + gm.getPlayers().get(key).getTiles().get(0).score);
        }
        System.out.println(gm.turnManager.getTurns());
        /*System.out.println("the current turn is for player- " + gm.turnManager.getCurrentTurn());
        gm.turnManager.nextTurn();
        System.out.println("After next turn the current turn is for player- " + gm.turnManager.getCurrentTurn());*/
        for(int i=0; i<10; i++){
            gm.turnManager.nextTurn();
            System.out.println("the current turn is for player- " + gm.turnManager.getCurrentTurn());
        }

    }
}
