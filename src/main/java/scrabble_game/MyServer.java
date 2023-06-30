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

    public MyServer(int port, ClientHandler ch) {
        this.port = port;
        this.ch = ch;
    }

    public void start() {
        stop = false;
        new Thread(this::startServer).start();
    }

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

    public void close(){
        stop = true;
    }
}
