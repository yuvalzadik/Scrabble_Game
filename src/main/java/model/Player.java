package model;

public class Player {
    private int score;
    private String name;

    public Player(String name){
        this.name = name;
        this.score = 0;
    }

    public int getScore(){
        return this.score;
    }
    public void addScore(int score){ this.score += score;}
}
