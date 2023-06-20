package view.data;

import model.Model;
import java.io.*;
import java.net.Socket;
import java.util.Observable;

public class ModelReceiver extends Observable implements Serializable {
    MySocket server;
    private final BufferedInputStream inFromServer;
    Model updatedModel;
    public ModelReceiver(String ip, int port){
        try {
            server = new MySocket(new Socket(ip, port));
            inFromServer = new BufferedInputStream(server.getPlayerSocket().getInputStream());
            updatedModel = null;
            listenForModelUpdates();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUpdatedModel(Model newModel){
        this.updatedModel = newModel;
        setChanged();
        notifyObservers();
    }

    public MySocket getServer(){
        return server;
    }

    public Model getUpdatedModel() {
        return updatedModel;
    }

    private void listenForModelUpdates(){
        new Thread(() -> {
            while (!server.getPlayerSocket().isClosed()) {
                try {
                    Model newModel = (Model) new ObjectInputStream(inFromServer).readObject();
                    System.out.println("Received model update");
                    setUpdatedModel(newModel);
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
