package view.data;

import model.GameManager;

import java.io.*;
import java.net.Socket;
import java.util.Observable;

public class GameManagerReceiver extends Observable implements Serializable {
    MySocket server;
    private final BufferedInputStream inFromServer;
    GameManager updatedGameManager;
    /**
     * The GameManagerReceiver function is a constructor that creates a new GameManagerReceiver object.
     * It takes in the ip address and port number of the server, and uses them to create a socket connection with it.
     * The function then initializes an input stream from which it will receive data from the server, as well as an updatedGameManager variable that will be used to store any updates received from the server.
     * Finally, it calls listenForGameManagerUpdates() so that this GameManagerReceiver can begin listening for updates on its own thread.

     *
     * @param  ip Specify the ip address of the server
     * @param port Specify which port the socket will be listening on
     *
     */
    public GameManagerReceiver(String ip, int port){
        try {
            server = new MySocket(new Socket(ip, port));
            inFromServer = new BufferedInputStream(server.getPlayerSocket().getInputStream());
            updatedGameManager = null;
            listenForGameManagerUpdates();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The setUpdatedGameManager function is used to update the GameManager object that is stored in this class.
     * This function will be called by the controller when it receives a new GameManager object from the server.
     * The updatedGameManager variable will then be set to equal this new model, and all observers of this class
     * (the view) will be notified that an update has occurred so they can refresh their display accordingly.

     *
     * @param newModel Set the updatedgamemanager variable
     */
    public void setUpdatedGameManager(GameManager newModel){
        this.updatedGameManager = newModel;
        setChanged();
        notifyObservers();
    }

    /**
     * The getServer function returns the server socket.
     *
     * @return The server socket
     *
     */
    public MySocket getServer(){
        return server;
    }

    /**
     * The getUpdatedGameManager function returns the updatedGameManager variable.
     *
     * @return The updatedgamemanager variable
     */
    public GameManager getUpdatedGameManager() {
        return updatedGameManager;
    }

    /**
     * The listenForGameManagerUpdates function is a function that listens for updates from the server.
     * It does this by creating a new thread and then running it, which will listen for any incoming messages from the server.
     * If there are any incoming messages, it will update the game manager with those changes.

     * <p>
     *
     * @docauthor Trelent
     */
    private void listenForGameManagerUpdates(){
        new Thread(() -> {
            while (!server.getPlayerSocket().isClosed()) {
                try {
                    GameManager newModel = (GameManager) new ObjectInputStream(inFromServer).readObject();
                    System.out.println("Received model update");
                    setUpdatedGameManager(newModel);
                } catch (IOException | ClassNotFoundException ignored) {}
            }
        }).start();
    }

    public static class MySocket implements Serializable {
        private transient Socket playerSocket;

        /**
         * The MySocket function is a constructor for the MySocket class.
         * It takes in a Socket object as an argument and assigns it to the playerSocket variable.

         *
         * @param  playerSocket Socket  Create a new socket for the client
         *
         */
        public MySocket(Socket playerSocket) {
            this.playerSocket = playerSocket;
        }

        /**
         * The writeObject function is used to write the object's state to a stream.
         * This function is called by the serialization runtime before it attempts
         * to serialize the object. The default implementation of this method, which
         * is found in ObjectOutputStream, will check to make sure that the class of
         * this object defines its own writeObject method (which includes a call back
         * into ObjectOutputStream) and then execute that method. If no such method
         * exists for this class, then it will attempt to save all non-transient and

         *
         * @param  out ObjectOutputStream  Write the object to a file
         *
         */
        @Serial
        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
        }

        /**
         * The readObject function is used to read the object from a stream.
         * <p>
         *
         * @param in ObjectInputStream  Read the object from the stream
         */
        @Serial
        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            this.playerSocket = new Socket();
        }

        /**
         * The getPlayerSocket function returns the playerSocket variable.
         *
         * @return The playersocket variable
         */
        public Socket getPlayerSocket() {
            return playerSocket;
        }
    }
}
