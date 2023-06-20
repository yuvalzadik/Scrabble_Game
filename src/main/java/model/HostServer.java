package model;

import scrabble_game.ClientHandler;
import scrabble_game.MyServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.*;

public class HostServer extends MyServer {

    Map<Integer, Socket> socketMap;
    ServerSocket serverSocket;
    boolean gameIsRunning;
    Timer timer;

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
                    if(socketMap.size() < 4){
                        Scanner fromPlayer = new Scanner(newSocket.getInputStream());
                        String playerName = fromPlayer.next();
                        socketMap.put(socketMap.size()+1, newSocket);
                        System.out.println(playerName + " Connected");
                        GameManager.get_instance().addPlayer(new Player(playerName));
                    }
                }
                catch (SocketTimeoutException ignored) {}
            }

            if(gameIsRunning){
                this.timer = new Timer();
                timer.scheduleAtFixedRate(new ManageTurn(),5000,15000); //after the test will be 5000 and 60,000
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

    public class ManageTurn extends TimerTask{

        @Override
        public void run() {
            new Thread (()-> {
                GameManager gameManager = GameManager.get_instance();
                gameManager.turnManager.nextTurn();
                int turn = gameManager.turnManager.getCurrentTurn();
                System.out.println("Player number " + turn + " is playing!");

                try {
                    ch.handleClient(socketMap.get(turn).getInputStream(), socketMap.get(turn).getOutputStream());
                    //the client finish its turn before timer is over
                    this.cancel();
                    timer.scheduleAtFixedRate(new ManageTurn(), 5000, 15000); //after the test will be 5000 and 60,000
                } catch (IOException ignored) {}
            }).start();
            }
    }
}
