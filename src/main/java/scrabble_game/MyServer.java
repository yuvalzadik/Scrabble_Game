package scrabble_game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class MyServer {
    protected int port;
    boolean stop;
    protected ClientHandler ch;

    /**
     * The MyServer function is a constructor for the MyServer class.
     * It initializes the port and client handler of this server.

     *
     * @param port Set the port number that the server will listen on
     * @param ch Pass the clienthandler object to the myserver constructor
     *
     */
    public MyServer(int port, ClientHandler ch) {
        this.port = port;
        this.ch = ch;
    }

    /**
     * The start function starts the server.
     */
    public void start() {
        stop = false;
        new Thread(this::startServer).start();
    }

    /**
     * The startServer function is responsible for creating a new server socket, and then listening to it.
     * If the server receives a client connection, it will handle the client using the ClientHandler object.

     * <p>
     *
     * @docauthor Trelent
     */
    protected void startServer() {
        ServerSocket serversocket = null;
        try {
            serversocket = new ServerSocket(port);
            serversocket.setSoTimeout(1000);
            while (!stop) {
                try {
                    Socket client = serversocket.accept();
                    ch.handleClient(client.getInputStream(), client.getOutputStream());
                    ch.close();
                    client.close();
                } catch (SocketTimeoutException ignored) {
                } finally {
                    ch.close();
                }
            }
            serversocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert serversocket != null;
                serversocket.close();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

    /**
     * The close function stops the thread from running.
     */
    public void close(){
        stop = true;
    }
}
