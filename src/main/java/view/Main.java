package view;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.data.ViewShareData;
import viewModel.ViewModel;

import java.io.IOException;
public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
        Parent root = loader.load();

        MainWindowController mainWindowController = loader.getController();
        ViewShareData viewShareData = new ViewShareData(new ViewModel());
        mainWindowController.setViewShareData(viewShareData);

        Scene scene = new Scene(root, 1000, 550);
        scene.getStylesheets().add(getClass().getResource("HomePage.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

