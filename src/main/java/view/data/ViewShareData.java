package view.data;

import viewModel.ViewModel;

public class ViewShareData {

    ViewModel viewModel;

    public ViewShareData(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public ViewModel getViewModel() {
        return viewModel;
    }
}
