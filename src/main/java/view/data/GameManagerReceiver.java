package view.data;

import model.GameManager;

import java.io.*;
import java.net.Socket;
import java.util.Observable;

public class GameManagerReceiver extends Observable implements Serializable {
    MySocket server;
    private final BufferedInputStream inFromServer;
    GameManager updatedGameManager;
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

    public void setUpdatedGameManager(GameManager newModel){
        this.updatedGameManager = newModel;
        setChanged();
        notifyObservers();
    }

    public MySocket getServer(){
        return server;
    }

    public GameManager getUpdatedGameManager() {
        return updatedGameManager;
    }

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

    public static class MySocket implements Serializable { //changed to static
        private transient Socket playerSocket;

        public MySocket(Socket playerSocket) {
            this.playerSocket = playerSocket;
        }

        @Serial
        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
        }

        @Serial
        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            this.playerSocket = new Socket();
        }

        public Socket getPlayerSocket() {
            return playerSocket;
        }
    }
}
