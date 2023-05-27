package test;

import model.GameMode;
import model.GameManager;
import model.Player;

public class GameManagerTest {
    public static void main(String[] args) {
        Player player1 = new Player("Yuval");
        Player player2 = new Player("Gil");
        Player player3 = new Player("Or");
        GameManager gameManager = new GameManager();
       int playerID = gameManager.addPlayer(player1);
       if (playerID!= 0){
           System.out.println("problem with addPlayer1 Function");
       }
       playerID = gameManager.addPlayer(player2);
        if (playerID!= 1){
            System.out.println("problem with addPlayer2 Function");
        }
        gameManager.startGame();
        playerID = gameManager.addPlayer(player3);
        if (playerID!= -1){
            System.out.println("problem with addPlayer3 Function");
       }

        gameManager.addScore(1,10);
        if (player2.getScore()!= 10){
            System.out.println("problem with addScore1 Function");
        }
        gameManager.addScore(1,-5);
        if (player2.getScore()!= 5){
            System.out.println("problem with addScore2 Function");
        }
        gameManager.addScore(0,3);
        if (player1.getScore()!= 3) {
            System.out.println("problem with addScore3 Function");
        }
        System.out.println("DONE");

    }
}
