package view;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class MainWindowController {
    private VBox draggedTile; // Currently dragged tile
    private StackPane targetSquare; // Target square for dropping the tile
    public static String[] players = {"Guest", "Guest", "Guest", "Guest"};
    @FXML
    private Label player0Label;
    @FXML
    private Label player1Label;
    @FXML
    private Label player2Label;

    @FXML
    private Label player3Label;

    @FXML
    private Label nameLabelError;
    @FXML
    private TextField nameField;
    @FXML
    private Label portLabelError;
    @FXML
    private TextField portField;
    @FXML
    private Label ipLabelError;
    @FXML
    private TextField ipField;
    @FXML
    private GridPane boardGridPane;

    @FXML
    private HBox tileContainerHBox;

    @FXML
    public void Submit(ActionEvent event) throws IOException {
        System.out.println("Submit");
        squareClickHandler();
    }

    @FXML
    public void Challenge(ActionEvent event) throws IOException {
        System.out.println("Challenge");
    }

    @FXML
    public void ReloadTiles(ActionEvent event) throws IOException {
        System.out.println("SwapTiles");
    }

    @FXML
    public void ShuffleTiles(ActionEvent event) throws IOException {
        List<VBox> tileContainers = new ArrayList<>();

        // Collect the TileContainer elements from the HBox
        for (var node : tileContainerHBox.getChildren()) {
            if (node instanceof VBox) {
                tileContainers.add((VBox) node);
            }
        }

        // Shuffle the tileContainers list in a random order
        Collections.shuffle(tileContainers, new Random());

        // Clear the HBox and add the shuffled VBox elements back to it
        tileContainerHBox.getChildren().setAll(tileContainers);

    }




    @FXML
    public void SkipTurn(ActionEvent event) throws IOException {
        System.out.println("SkipTurn");
    }

    @FXML
    public void Resign(ActionEvent event) throws IOException {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Are you sure you want to resign the game?");
        confirmationAlert.setContentText("Click OK to confirm.");

        // Show the alert and wait for the user's response
        confirmationAlert.showAndWait()
                .ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        // User clicked OK, perform the submit action
                        Platform.exit();
                    }
                });
        System.out.println("Resign");
    }


    @FXML
    public void StartTutorial(ActionEvent event) throws IOException {
        loadScene(event, "Tutorial");
    }

    @FXML
    public void StartGame(ActionEvent event) throws IOException {
        loadScene(event, "HomePage");
    }

    @FXML
    public void StartAsHost(ActionEvent event) throws IOException {
        boolean allValid = true;
        if (!validName(nameField.getText())) {
            nameLabelError.setVisible(true);
            allValid = false;
        } else {
            nameLabelError.setVisible(false);
        }
        if (!validPort(portField.getText())) {
            portLabelError.setVisible(true);
            allValid = false;
        } else {
            portLabelError.setVisible(false);
        }

        if (allValid) {
            players[0]=nameField.getText();
            loadBoard(event);
        }
    }

    @FXML
    public void StartAsGuest(ActionEvent event) throws IOException {
        boolean allValid = true;
        if (!validName(nameField.getText())) {
            nameLabelError.setVisible(true);
            allValid = false;
        } else {
            nameLabelError.setVisible(false);
        }
        if (!validPort(portField.getText())) {
            portLabelError.setVisible(true);
            allValid = false;
        } else {
            portLabelError.setVisible(false);
        }
        if (!validIp(ipField.getText())) {
            ipLabelError.setVisible(true);
            allValid = false;
        } else {
            ipLabelError.setVisible(false);
        }

        if (allValid) {
            for(int i=0;i<4;i++)
                if(players[i] == "Guest"){
                    players[i]=nameField.getText();
                    break;
                }
            loadBoard(event);
        }
    }

    @FXML
    public void loadHostForm(ActionEvent event) throws IOException {
        loadScene(event, "HostPage");
    }

    @FXML
    public void loadHomePage(ActionEvent event) throws IOException {
        loadScene(event, "HomePage");
    }

    @FXML
    public void loadGuestForm(ActionEvent event) throws IOException {
        loadScene(event, "GuestPage");
    }

    @FXML
    public void loadBoard(ActionEvent event) throws IOException {
        loadScene(event, "BoardPage");
    }

    @FXML
    public void Exit(ActionEvent event) throws IOException {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Are you sure you want to exit the game?");
        confirmationAlert.setContentText("Click OK to confirm.");

        // Show the alert and wait for the user's response
        confirmationAlert.showAndWait()
                .ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        // User clicked OK, perform the submit action
                        Platform.exit();
                    }
                });
    }


    @FXML
    public void BackToHomePage(ActionEvent event) throws IOException {
        loadHomePage(event);
    }

    @FXML
    public void loadScene(ActionEvent event, String sceneName) throws IOException {
        /*Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(sceneName + ".fxml")));*/
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneName + ".fxml"));
        Parent root = loader.load();
        MainWindowController controller = loader.getController();

        Scene scene = null;
        if (Objects.equals(sceneName, "BoardPage")) {
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setFullScreen(true);
        } else {
            scene = new Scene(root);
            stage.setScene(scene);
        }
        try {
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(sceneName + ".css")).toExternalForm());
        } catch (NullPointerException ignored) {
        }

        stage.show();
        if(Objects.equals(sceneName, "BoardPage"))
            controller.updatePlayerNames(players);
    }
    @FXML
    public void updatePlayerNames(String[] playerNames){
        player0Label.setText(playerNames[0]);
        player1Label.setText(playerNames[1]);
        player2Label.setText(playerNames[2]);
        player3Label.setText(playerNames[3]);
    }
    public boolean validPort(String port) {
        return port.matches("(1000[1-9]|100[1-9]\\d|10[1-9]\\d{2}|1[1-9]\\d{3}|19999)");
    }

    public boolean validIp(String ip) {
        return ip.matches("(\\b25[0-5]|\\b2[0-4][0-9]|\\b[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}");
    }

    public boolean validName(String name) {
        return name.matches("^[A-Za-z]+$");
    }
    @FXML
    /*make the tile draggable*/
    public void squareClickHandler() {
        for (Node node : boardGridPane.getChildren()) {
            if (node instanceof StackPane) {
                StackPane square = (StackPane) node;
                Label squareLabel = (Label) square.getChildren().get(0);

                square.setOnMouseClicked(event -> {
                    // Check if the square's background is pink
                    // If a tile is already present in the square, remove it

                    if (square.getStyleClass().contains("TripleWord-cell")) {
                        squareLabel.setText("TP");
                    }
                    if (square.getStyleClass().contains("DoubleWord-cell")) {
                        squareLabel.setText("DW");
                    }
                    if (square.getStyleClass().contains("DoubleLetter-cell")) {
                        squareLabel.setText("DL");
                    }
                    if (square.getStyleClass().contains("TripleLetter-cell")) {
                        squareLabel.setText("TL");
                    }
                    if (square.getStyleClass().contains("Normal-cell")) {
                        squareLabel.setText("");
                    }
                    if (square.getStyleClass().contains("center-cell")) {
                        squareLabel.setText("SP");
                    }
                });
            }
        }
    }
    @FXML
    private void handleDragDetected(MouseEvent event) {

        VBox sourceTileContainer = (VBox) event.getSource();

        Dragboard db = sourceTileContainer.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putString(sourceTileContainer.getStyleClass().toString());
        db.setContent(content);

        // Create a snapshot of the dragged content
        WritableImage snapshot = sourceTileContainer.snapshot(null, null);
        db.setDragView(snapshot, event.getX(), event.getY());

        event.consume();
    }

    @FXML
    private void handleDragOver(DragEvent event) {
        if (event.getGestureSource() != boardGridPane && event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.MOVE);
        }

        event.consume();
    }

    @FXML
    private void handleDragDropped(DragEvent event) {
        boolean success = false;
        for (Node node : boardGridPane.getChildren()) {
            StackPane targetSquare = (StackPane) node;
            Label squareLabel = (Label) targetSquare.getChildren().get(0);

            targetSquare.setOnDragDropped(event1 -> {
                VBox draggedTileContainer = (VBox) event1.getGestureSource();
                Label tileLabel = (Label) draggedTileContainer.getChildren().get(0);
                squareLabel.setText(tileLabel.getText());

            });
        }
        success = true;

        event.setDropCompleted(success);
        event.consume();
    }


    @FXML
    private void handleDragDone(DragEvent event) {
        if (event.getTransferMode() == TransferMode.MOVE) {

        }

        event.consume();
    }

}





