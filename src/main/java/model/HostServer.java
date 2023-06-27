package model;

import scrabble_game.Board;
import scrabble_game.ClientHandler;
import scrabble_game.MyServer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.*;

public class HostServer extends MyServer {

    Map<Integer, Socket> socketMap;
    Map<Integer, Socket> modelReceivers;
    ServerSocket serverSocket;
    boolean gameIsRunning;
    Timer timer;
    TimerTask timerTask;

    public HostServer(int port, ClientHandler ch) {
        super(port, ch);
        this.serverSocket = null;
        socketMap = new HashMap<>();
        modelReceivers = new HashMap<>();
        gameIsRunning = false;
        timerTask = null;
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
                    if(playerName.equals("startGame")){
                        startGame();
                        newSocket.close();
                    }
                    else if(socketMap.size() < 4) {
                        socketMap.put(socketMap.size() + 1, newSocket);
                        System.out.println(playerName + " Connected");
                        GameManager.get_instance().addPlayer(new Player(playerName));
                        System.out.println("before accept");
                        Socket clientModelReceiver = serverSocket.accept();
                        System.out.println("clientModelReceiver (hostServer)");
                        modelReceivers.put(socketMap.size(), clientModelReceiver);
                        updateGuestsModel();
                    }
                }
                catch (SocketTimeoutException ignored) {}
            }

            while(gameIsRunning){
                if(timerTask == null || timer == null){
                    System.out.println("Starting new task");
                    this.timer = new Timer();
                    this.timerTask = new ManageTurn();
                    GameManager.get_instance().turnManager.nextTurn();
                    timer.schedule(timerTask,1000,60000); //after the test will be 5000 and 60,000
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void updateGuestsModel() {
        new Thread(() -> {
            for(Socket s : modelReceivers.values()){
                try{
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(s.getOutputStream());
                    objectOutputStream.writeObject(GameManager.get_instance());
                } catch (IOException e) {
                    System.out.println("Model is probably not serializable!");
                }
            }
        }).start();
    }

    public void startGame(){
        GameManager gameManager = GameManager.get_instance();
        gameManager.startGame();
        this.gameIsRunning = true;
    }

    public boolean getServerStatus(){
        return this.gameIsRunning;
    }

    public class ManageTurn extends TimerTask{
        @Override
        public void run() {
            new Thread (()-> {
                updateGuestsModel();
                GameManager gameManager = GameManager.get_instance();
                int turn = gameManager.turnManager.getCurrentTurn();
                System.out.println("Player number " + turn + " is playing!");
                try {
                    ch.handleClient(socketMap.get(turn).getInputStream(), socketMap.get(turn).getOutputStream());
                    this.cancel();
                    timer = null;
                    resetCurrentTask();
                } catch (IOException ignored) {}
            }).start();
        }
    }

    private void resetCurrentTask(){
        timerTask = null;
    }

    @Override
    public void close() {
        timer.cancel();
        resetCurrentTask();
        //TODO - close everything.
        super.close();
    }
}
