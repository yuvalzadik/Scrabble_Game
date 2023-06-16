package model;

import scrabble_game.ClientHandler;
import scrabble_game.MyServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HostServer extends MyServer {

    Map<Integer, Socket> socketMap;
    ServerSocket serverSocket;
    boolean gameIsRunning;

    public HostServer(int port, ClientHandler ch) {
        super(port, ch);
        this.serverSocket = null;
        socketMap = new HashMap<>();
        gameIsRunning = false;
    }

    @Override
    protected void startServer() {
        try {
            this.serverSocket = new ServerSocket(this.port);
            this.serverSocket.setSoTimeout(1000);
            while(!gameIsRunning){
                try{
                    Socket newSocket = serverSocket.accept();
                    Scanner fromPlayer = new Scanner(newSocket.getInputStream());
                    String playerName = fromPlayer.next();
                    socketMap.put(socketMap.size()+1, newSocket);
                    int playerId = GameManager.get_instance().addPlayer(new Player(playerName));
                    System.out.println("First loop");
                }
                catch (SocketTimeoutException ignored) {}
            }

            while(gameIsRunning){
                System.out.println("Second loop");
                for(Socket s : socketMap.values()){
                    PrintWriter test = new PrintWriter(s.getOutputStream(), true);
                    test.println("hey");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void startGame(){
        this.gameIsRunning = true;
    }

    public boolean getServerStatus(){
        return this.gameIsRunning;
    }
}
