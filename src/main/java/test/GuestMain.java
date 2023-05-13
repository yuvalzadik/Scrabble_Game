package test;

import model.GameMode;
import model.Model;

public class GuestMain {
    public static void main(String[] args) {
        System.out.println("Scrabble Game!");

        String propertiesPath = "./properties.txt";
        String ip = "localhost";
        int port = 1234;
        Model scrabbleModel = new Model(GameMode.Guest, ip, port);

        scrabbleModel.joinGame("Afek");
    }
}
