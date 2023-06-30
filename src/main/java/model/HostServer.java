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
            connectingClients();
            playingGame();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void playingGame() {
        if(!gameIsRunning) gameIsRunning = true;
        while(gameIsRunning){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(timerTask == null || timer == null){
                System.out.println("Starting new turn");
                this.timerTask = new ManageTurn();
                this.timer = new Timer();
                GameManager.get_instance().turnManager.nextTurn();
                GameManager.get_instance().fillHand(GameManager.get_instance().turnManager.getCurrentTurn());
                timer.schedule(timerTask,1000,60000); //after the test will be 5000 and 60,000
            }
        }
    }

    private void connectingClients() {
        while(!gameIsRunning){
            try{
                Socket newSocket = serverSocket.accept();
                Scanner fromPlayer = new Scanner(newSocket.getInputStream());
                PrintWriter outToPlayer = new PrintWriter(newSocket.getOutputStream(), true);
                String playerName = fromPlayer.next();
                if(playerName.equals("startGame")){
                    startGame();
                    newSocket.close();
                }
                else if(socketMap.size() < 4) {
                    socketMap.put(socketMap.size() + 1, newSocket);
                    outToPlayer.println(socketMap.size());
                    System.out.println(playerName + " Connected");
                    GameManager.get_instance().addPlayer(new Player(playerName));
                    Socket clientModelReceiver = serverSocket.accept();
                    modelReceivers.put(socketMap.size(), clientModelReceiver);
                    updateGuestsModel();
                }
            }
            catch (IOException ignored) {}
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

    private void broadcastUpdate(String message) {
        try{
            for(Socket s : socketMap.values()){
                PrintWriter printWriter = new PrintWriter(s.getOutputStream(), true);
                printWriter.println("bindButtons");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean getServerStatus(){
        return this.gameIsRunning;
    }

    public class ManageTurn extends TimerTask{
        @Override
        public void run() {
            new Thread (()-> {
                broadcastUpdate("bindButtons");
                updateGuestsModel();
                GameManager gameManager = GameManager.get_instance();
                int turn = gameManager.turnManager.getCurrentTurn();
                System.out.println("Player number " + turn + " is playing!");
                try {
                    ch.handleClient(socketMap.get(turn).getInputStream(), socketMap.get(turn).getOutputStream());
                    this.cancel();
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
