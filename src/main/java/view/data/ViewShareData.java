package view.data;

import model.GameManager;
import model.Model;
import scrabble_game.MyServer;
import viewmodel.ViewModel;

import java.util.Observable;
import java.util.Observer;

public class ViewShareData implements Observer {
    ViewModel viewModel;
    GameManagerReceiver gameManagerReceiver;
    MyServer bookScrabbleServer;
    String hostIp;
    String playerName;
    int hostPort;
    boolean isHost;

    /**
     * The ViewShareData function is a constructor for the ViewShareData class.
     * It takes in a viewModel and sets it to this.viewModel, then sets gameManagerReceiver to null, hostIp to localhost, isHost to false and bookScrabbleServer also set as null.

     *
     * @param  viewModel Get the data from the viewmodel
     */
    public ViewShareData(ViewModel viewModel) {

        this.viewModel = viewModel;
        this.gameManagerReceiver = null;
        this.hostIp = "localhost";
        this.isHost = false;
        this.bookScrabbleServer = null;

    }

    /**
     * The getViewModel function returns the viewModel object.
     * <p>
     *
     *
     * @return The viewmodel object
     */
    public ViewModel getViewModel() {
        return viewModel;
    }

    /**
     * The setGameManagerReceiver function is used to set the gameManagerReceiver variable.
     * <p>
     *
     * @param  gameModelReceiver Set the gamemanagerreceiver variable
     */
    public void setGameManagerReceiver(GameManagerReceiver gameModelReceiver) {
        this.gameManagerReceiver = gameModelReceiver;
        this.gameManagerReceiver.addObserver(this);
    }

    /**
     * The setHost function sets the isHost variable to true or false.
     * <p>
     *
     * @param  host Set the ishost variable to true or false
     */
    public void setHost(boolean host) {
        isHost = host;
    }

    /**
     * The getHost function returns the boolean value of isHost.
     * <p>
     *
     *
     * @return The value of the ishost variable
     */
    public boolean getHost(){
        return isHost;
    }

    /**
     * The setBookScrabbleServer function sets the bookScrabbleServer variable to a new MyServer object.
     * <p>
     *
     * @param  bookScrabbleServer Set the bookscrabbleserver variable to the value of bookscrabbleserver
     */
    public void setBookScrabbleServer(MyServer bookScrabbleServer) {
        this.bookScrabbleServer = bookScrabbleServer;
    }

    /**
     * The setHostIp function sets the hostIp variable to a new value.
     * <p>
     *
     * @param  hostIp Set the host ip address
     *
     */
    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    /**
     * The getHostIp function returns the hostIp variable.
     * <p>
     *
     *
     * @return The hostip variable
     */
    public String getHostIp() {
        return hostIp;
    }

    /**
     * The setHostPort function sets the hostPort variable to the value of its parameter.
     * <p>
     *
     * @param  hostPort Set the hostport variable
     */
    public void setHostPort(int hostPort) {
        this.hostPort = hostPort;
    }

    /**
     * The getHostPort function returns the port number of the host.
     * <p>
     *
     *
     * @return The hostport variable
     */
    public int getHostPort() {
        return hostPort;
    }

    /**
     * The update function is a function that updates the view model with the updated game manager.
     * <p>
     *
     * @param o Observable  Identify the observable object that has changed
     * @param arg Object  Pass data to the observer
     *
     */
    @Override
    public void update(Observable o, Object arg) {
        this.getViewModel().setGameManager(this.gameManagerReceiver.getUpdatedGameManager());
    }

}




