package view.data;

import model.GameManager;
import model.Model;
import scrabble_game.MyServer;
import viewModel.ViewModel;

import java.util.Observable;
import java.util.Observer;

public class ViewShareData implements Observer {
    ViewModel viewModel;
    GameManagerReceiver gameManagerReceiver;

    MyServer bookScrabbleServer;
    GameManager gameManager;
    String hostIp;
    String playerName;
    int hostPort;
    boolean isHost;

    public ViewShareData(ViewModel viewModel) {

        this.viewModel = viewModel;
        this.gameManagerReceiver = null;
        this.hostIp = "localhost";
        this.isHost = false;
        this.bookScrabbleServer = null;

    }

    public ViewModel getViewModel() {
        return viewModel;
    }

    public void setGameManagerReceiver(GameManagerReceiver gameModelReceiver) {
        this.gameManagerReceiver = gameModelReceiver;
        this.gameManagerReceiver.addObserver(this);
    }

    public void setHost(boolean host) {
        isHost = host;
    }

    public void setBookScrabbleServer(MyServer bookScrabbleServer) {
        this.bookScrabbleServer = bookScrabbleServer;
    }

    public MyServer getBookScrabbleServer() {
        return bookScrabbleServer;
    }

    public GameManagerReceiver getGameManagerReceiver() {
        return gameManagerReceiver;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostPort(int hostPort) {
        this.hostPort = hostPort;
    }

    public int getHostPort() {
        return hostPort;
    }

    @Override
    public void update(Observable o, Object arg) {
        this.getViewModel().setGameManager(this.gameManagerReceiver.getUpdatedGameManager());
    }
}




