package view.data;

import viewModel.ViewModel;

import java.util.Observable;
import java.util.Observer;

public class ViewShareData implements Observer {
    ViewModel viewModel;
    ModelReceiver modelReceiver;
    String hostIp;
    String playerName;
    int hostPort;
    boolean isHost;

    public ViewShareData(ViewModel viewModel) {

        this.viewModel = viewModel;
        this.modelReceiver = null;
        this.hostIp = "localhost";
        this.isHost = false;
    }

    public ViewModel getViewModel() {
        return viewModel;
    }

    public void setModelReceiver(ModelReceiver gameModelReceiver) {
        this.modelReceiver = gameModelReceiver;
        this.modelReceiver.addObserver(this);
    }

    public ModelReceiver getModelReceiver() {
        return modelReceiver;
    }

    @Override
    public void update(Observable o, Object arg) {
        this.getViewModel().setModel(this.modelReceiver.getUpdatedModel());
    }
}




