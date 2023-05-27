package test;

import model.GameClientHandler;
import model.GameManager;
import model.GameMode;
import model.Model;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class GameClientHandlerTest {
    public static void main(String[] args){
        GameClientHandler gch = new GameClientHandler();
        String inputString = "0,D,yuv.txt, dictionary.txt, AND";
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        System.out.println("0,D,yuv.txt,dictionary.txt,endDictionaries,AND");
        // Start debugging
        // Add breakpoints in the handleClient method where you want to pause execution and inspect variables

        // Trigger the handleClient method with the System.in and System.out streams
        gch.handleClient(inputStream, outputStream);
//        System.out.println(GameManager.get_instance().getDictionaries());

//        System.out.println(GameManager.get_instance().getDictionaries());

    }


}
