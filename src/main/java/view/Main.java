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
    /**
     * The start function is the main function of the program.
     * It loads an FXML file and sets it as a scene in a stage.
     * <p>
     *
     * @param  stage Set the scene
     *
     * @docauthor Trelent
     */
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

    /**
     * The main function of the program.
     * <p>
     *
     * @param  args Pass command line arguments to the main function
     *
     * @docauthor Trelent
     */
    public static void main(String[] args) {
        launch();
    }
}

