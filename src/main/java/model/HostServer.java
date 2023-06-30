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

    /**
     * The HostServer function is the constructor for the HostServer class.
     * It takes in a port number and a ClientHandler object, and initializes
     * its serverSocket to null, as well as creating new HashMaps for socketMap
     * and modelReceivers. The gameIsRunning boolean is set to false by default,
     * while timerTask is initialized to null.

     *
     * @param port int Set the port number for the server
     * @param ch ClientHandler  Create a new thread for each client that connects to the server
     *
     * @docauthor Trelent
     */
    public HostServer(int port, ClientHandler ch) {
        super(port, ch);
        this.serverSocket = null;
        socketMap = new HashMap<>();
        modelReceivers = new HashMap<>();
        gameIsRunning = false;
        timerTask = null;
    }

    /**
     * The startServer function creates a new server socket and sets the timeout to 1000 milliseconds.
     * It then calls the connectingClients function, which waits for two clients to connect before calling playingGame.

     *
     *
     * @docauthor Trelent
     */
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

    /**
     * The playingGame function is the main game loop. It runs until the gameIsRunning boolean is set to false, which happens when a player wins or loses.
     * The function starts by checking if the timerTask and timer are null, and if they are it creates new instances of them (this only happens on turn 1).
     * Then it calls nextTurn() in TurnManager to advance turns, fills each players hand with cards from their deck using fillHand(), then schedules a task for every minute that will check for win conditions using ManageTurn().

     * <p>
     *
     * @docauthor Trelent
     */
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

    /**
     * The connectingClients function is responsible for accepting new connections from clients.
     * It will accept a connection, and then check if the client has sent a &quot;startGame&quot; message.
     * If so, it will start the game by calling the startGame function. Otherwise, it will add
     * that player to its list of players and send them their player number (which is just their index in this list).

     * <p>
     *
     * @docauthor Trelent
     */
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

    /**
     * The updateGuestsModel function is called whenever the game model changes.
     * It sends a serialized version of the GameManager to all connected clients,
     * so that they can update their views accordingly.

     * <p>
     *
     * @docauthor Trelent
     */
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

    /**
     * The startGame function is responsible for starting the game.
     * It does this by calling the startGame function in GameManager, which sets up all of the necessary variables and starts a new thread to run the game loop.

     *
     *
     * @docauthor Trelent
     */
    public void startGame(){
        GameManager gameManager = GameManager.get_instance();
        gameManager.startGame();
        this.gameIsRunning = true;
    }

    /**
     * The broadcastUpdate function is used to send a message to all connected clients.
     * <p>
     *
     * @param message String  Send a message to all the connected clients
     *
     * @docauthor Trelent
     */
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

    /**
     * The getServerStatus function returns the boolean value of gameIsRunning.
     *
     * @return A boolean value
     *
     * @docauthor Trelent
     */
    public boolean getServerStatus(){
        return this.gameIsRunning;
    }

    public class ManageTurn extends TimerTask{
        /**
         * The run function is the main function of this class. It creates a new thread that handles the client's turn,
         * and then updates the GUI to reflect that it is now their turn.
         *
         * @docauthor Trelent
         */
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

    /**
     * The resetCurrentTask function sets the timerTask variable to null.
     *
     * @docauthor Trelent
     */
    private void resetCurrentTask(){
        timerTask = null;
    }

    /**
     * The close function is called when the user closes the window.
     * It cancels any running timers, resets the current task to null, and then calls super.close()

     *
     * @docauthor Trelent
     */
    @Override
    public void close() {
        timer.cancel();
        resetCurrentTask();
        //TODO - close everything.
        super.close();
    }
}
