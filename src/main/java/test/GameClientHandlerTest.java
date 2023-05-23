package test;

import model.GameClientHandler;
import model.GameManager;

import java.net.Socket;

public class GameClientHandlerTest {
    public static void main(String[] args){
        GameClientHandler gch = new GameClientHandler();
        try{
            Socket playerTest = new Socket();
        } catch (RuntimeException ignored){}


//        gch.handleClient();
    }


}
