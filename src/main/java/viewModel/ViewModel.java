package viewModel;

import model.Model;

import java.util.Observable;
import java.util.Observer;

public class ViewModel {

    Model model;

    public void setModel(Model model) {

        this.model = model;
    }

    public Model getModel() {

        return model;
    }

}
