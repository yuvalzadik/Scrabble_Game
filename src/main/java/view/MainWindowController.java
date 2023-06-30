package view;


import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.BookScrabbleCommunication;
import model.GameManager;
import model.GameMode;
import model.Model;
import scrabble_game.Board;
import scrabble_game.BookScrabbleHandler;
import scrabble_game.MyServer;
import view.data.GameManagerReceiver;
import view.data.ViewShareData;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;


public class MainWindowController {
    ViewShareData viewShareData;

    String playerWord;
    int startRow;
    int startCol;
    boolean vertical;

    private VBox draggedTile; // Currently dragged tile
    private StackPane targetSquare; // Target square for dropping the tile
    //public static String[] players = {"Guest", "Guest", "Guest", "Guest"};

    @FXML
    private Label firstPlayerName;
    @FXML
    private Label secondPlayerName;
    @FXML
    private Label thirdPlayerName;
    @FXML
    private Label fourthPlayerName;

    @FXML
    private Label firstPlayerScore;
    @FXML
    private Label secondPlayerScore;
    @FXML
    private Label thirdPlayerScore;
    @FXML
    private Label fourthPlayerScore;

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
    private Label firstTileLetter;
    @FXML
    private Label secondTileLetter;
    @FXML
    private Label thirdTileLetter;
    @FXML
    private Label fourthTileLetter;
    @FXML
    private Label fifthTileLetter;
    @FXML
    private Label sixTileLetter;
    @FXML
    private Label sevenTileLetter;

    @FXML
    private Label firstTileScore;
    @FXML
    private Label secondTileScore;
    @FXML
    private Label thirdTileScore;
    @FXML
    private Label fourthTileScore;
    @FXML
    private Label fifthTileScore;
    @FXML
    private Label sixTileScore;
    @FXML
    private Label sevenTileScore;

    @FXML
    private Button submit;
    @FXML
    private Button resign;
    @FXML
    private Button skipTurn;
    @FXML
    private Button startGame;

    StringProperty viewModelUpdates;
    StringProperty cellLabel;
    StringProperty playerAction;
    StringProperty lastWord;
    Map<String, String> placedTiles;
    private boolean[] tileDragged = new boolean[7];
    public MainWindowController(){
        lastWord = new SimpleStringProperty();
        playerAction = new SimpleStringProperty();
        placedTiles = new HashMap<>();
        startGame = new Button();

        initializeTileProperties();
        initializeNameProperties();
        initializeScoreProperties();
    }

    private void initializeScoreProperties() {
        firstPlayerScore = new Label();
        secondPlayerScore = new Label();
        thirdPlayerScore = new Label();
        fourthPlayerScore = new Label();
    }

    private void initializeNameProperties() {
        firstPlayerName = new Label();
        secondPlayerName = new Label();
        thirdPlayerName= new Label();
        fourthPlayerName = new Label();
    }

    public void initializeTileProperties(){
        firstTileLetter = new Label();
        secondTileLetter = new Label();
        thirdTileLetter = new Label();
        fourthTileLetter = new Label();
        fifthTileLetter = new Label();
        sixTileLetter = new Label();
        sevenTileLetter = new Label();

        firstTileScore = new Label();
        secondTileScore = new Label();
        thirdTileScore = new Label();
        fourthTileScore = new Label();
        fifthTileScore = new Label();
        sixTileScore = new Label();
        sevenTileScore = new Label();
    }

    public void initializeBoardAction(){
        cellLabel = new SimpleStringProperty();
        ObjectProperty<String> newCellLabel = viewShareData.getViewModel().getCellLabel();
        cellLabel.bind(newCellLabel);
        newCellLabel.addListener(((observable, oldLabel, newLabel) -> updateBoardCellLabel(newLabel)));
    }

    public void initializeViewModelUpdates(){
        viewModelUpdates = new SimpleStringProperty();
        StringProperty newViewModelUpdates = viewShareData.getViewModel().getViewModelUpdates();
        viewModelUpdates.bind(newViewModelUpdates);
        newViewModelUpdates.addListener(((observable, oldAction, newAction) -> {
            handleViewModelUpdates(newAction);
        }));
    }

    private void handleViewModelUpdates(String newAction) {
        switch(newAction){
            case "dictionaryNotLegal" -> Platform.runLater(this::showChallengeWindow);
            case "bindButtons" -> Platform.runLater(() -> {
                bindButtonsProperties();
                viewShareData.getViewModel().updateView();
            });
            case "boardNotLegal" -> {
                System.out.println("board Not Legal");
                initializeBoardAction();
                // Reset tileDragged array
                Arrays.fill(tileDragged, false);

                // Enable all TileContainers
                for (Node node : tileContainerHBox.getChildren()) {
                    if (node instanceof VBox) {
                        VBox tileContainer = (VBox) node;
                        tileContainer.setDisable(false);
                    }
                }
            }
        }
    }

    private void showChallengeWindow() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Dictionary Challenge");
        alert.setHeaderText(null);
        alert.setContentText("Would you like to challenge the dictionary?");

        Button yesButton = (Button) alert.getDialogPane().lookupButton(javafx.scene.control.ButtonType.OK);
        yesButton.setText("Yes");

        Button noButton = (Button) alert.getDialogPane().lookupButton(javafx.scene.control.ButtonType.CANCEL);
        noButton.setText("No");

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);

        alert.getDialogPane().setExpandableContent(vbox);
        alert.getDialogPane().setExpanded(true);

        alert.showAndWait().ifPresent(response -> {
            if(response == ButtonType.OK){
                playerAction.set("Challenge" + "," + lastWord);
                resetWordParameters();
            }
            else{
                viewShareData.getViewModel().updateView();
            }
            alert.close();
        });
    }

    private void updateBoardCellLabel(String newLabel) {
        String[] splitted = newLabel.split(",");
        StackPane square = (StackPane) boardGridPane.getChildren().get(Integer.parseInt(splitted[0]));
        Label squareLabel = (Label) square.getChildren().get(0);

        if(splitted[1].equals("default")){
            if (square.getStyleClass().contains("TripleWord-cell")) {
                squareLabel.setText("TP");
            }
            else if (square.getStyleClass().contains("DoubleWord-cell")) {
                squareLabel.setText("DW");
            }
            else if (square.getStyleClass().contains("DoubleLetter-cell")) {
                squareLabel.setText("DL");
            }
            else if (square.getStyleClass().contains("TripleLetter-cell")) {
                squareLabel.setText("TL");
            }
            else if (square.getStyleClass().contains("Normal-cell")) {
                squareLabel.setText("");
            }
            else if (square.getStyleClass().contains("center-cell")) {
                squareLabel.setText("SP");
            }
            else squareLabel.setText("");
        }
        else squareLabel.setText(splitted[1]);
    }

    public void bindButtonsProperties(){
        submit.visibleProperty().bind(viewShareData.getViewModel().submit.get().visibleProperty());
        resign.visibleProperty().bind(viewShareData.getViewModel().resign.get().visibleProperty());
        skipTurn.visibleProperty().bind(viewShareData.getViewModel().skipTurn.get().visibleProperty());
        startGame.visibleProperty().bind(viewShareData.getViewModel().startGame.get().visibleProperty());
    }

    public void bindPlayerProperties(){
        firstPlayerName.textProperty().bind(viewShareData.getViewModel().firstPlayerName);
        secondPlayerName.textProperty().bind(viewShareData.getViewModel().secondPlayerName);
        thirdPlayerName.textProperty().bind(viewShareData.getViewModel().thirdPlayerName);
        fourthPlayerName.textProperty().bind(viewShareData.getViewModel().fourthPlayerName);

        firstPlayerScore.textProperty().bind(viewShareData.getViewModel().firstPlayerScore);
        secondPlayerScore.textProperty().bind(viewShareData.getViewModel().secondPlayerScore);
        thirdPlayerScore.textProperty().bind(viewShareData.getViewModel().thirdPlayerScore);
        fourthPlayerScore.textProperty().bind(viewShareData.getViewModel().fourthPlayerScore);
    }

    public void bindTilesProperties(){
        firstTileLetter.textProperty().bind(viewShareData.getViewModel().firstTileLetter);
        secondTileLetter.textProperty().bind(viewShareData.getViewModel().secondTileLetter);
        thirdTileLetter.textProperty().bind(viewShareData.getViewModel().thirdTileLetter);
        fourthTileLetter.textProperty().bind(viewShareData.getViewModel().fourthTileLetter);
        fifthTileLetter.textProperty().bind(viewShareData.getViewModel().fifthTileLetter);
        sixTileLetter.textProperty().bind(viewShareData.getViewModel().sixTileLetter);
        sevenTileLetter.textProperty().bind(viewShareData.getViewModel().sevenTileLetter);

        firstTileScore.textProperty().bind(viewShareData.getViewModel().firstTileScore);
        secondTileScore.textProperty().bind(viewShareData.getViewModel().secondTileScore);
        thirdTileScore.textProperty().bind(viewShareData.getViewModel().thirdTileScore);
        fourthTileScore.textProperty().bind(viewShareData.getViewModel().fourthTileScore);
        fifthTileScore.textProperty().bind(viewShareData.getViewModel().fifthTileScore);
        sixTileScore.textProperty().bind(viewShareData.getViewModel().sixTileScore);
        sevenTileScore.textProperty().bind(viewShareData.getViewModel().sevenTileScore);
    }

    public void initializeHostAction(){
        viewShareData.getViewModel().initializeHostAction();
    }

    public void initializePlayerAction(){
        viewShareData.getViewModel().getPlayerAction().bind(playerAction);
        viewShareData.getViewModel().getLastWord().bind(lastWord);
    }

    @FXML
    public void Submit(ActionEvent event) throws IOException {
        buildWord();
        System.out.println("From Submit(View)" + lastWord);
        playerAction.set("Submit," + lastWord);
    }

    private void buildWord() {
        StringBuilder stringBuilder = new StringBuilder();
        startRow = 14;
        startCol = 14;

        int endRow = 0;
        int endCol = 0;

        for(String str : placedTiles.keySet()){
            String[] splittedStr = str.split(",");
            if(Integer.parseInt(splittedStr[0]) < startRow) startRow = Integer.parseInt(splittedStr[0]);
            if(Integer.parseInt(splittedStr[0]) > endRow) endRow = Integer.parseInt(splittedStr[0]);

            if(Integer.parseInt(splittedStr[1]) < startCol) startCol = Integer.parseInt(splittedStr[1]);
            if(Integer.parseInt(splittedStr[1]) > endCol) endCol = Integer.parseInt(splittedStr[1]);
        }

        vertical = endRow - startRow > 0;

        int len = vertical ? endRow-startRow + 1 : endCol-startCol + 1;
        int currentRow = startRow;
        int currentCol = startCol;

        for(int i = 0; i < len; i++){
            String foundLetter = placedTiles.get("" + currentRow + "," + currentCol);
            if(foundLetter != null) stringBuilder.append(foundLetter);
            else stringBuilder.append("_");
            if(vertical) currentRow++;
            else currentCol++;
        }

        playerWord = stringBuilder.toString();
        lastWord.setValue(playerWord+","+startRow+","+startCol+","+vertical);
        resetWordParameters();
    }

    public void resetWordParameters(){
        placedTiles.clear();
        playerWord = "";
    }

    @FXML
    public void ReloadTiles(ActionEvent event) throws IOException {
        playerAction.setValue("reset");
        playerAction.setValue("SwapTiles");
        initializeBoardAction();
        // Reset tileDragged array
        Arrays.fill(tileDragged, false);

        // Enable all TileContainers
        for (Node node : tileContainerHBox.getChildren()) {
            if (node instanceof VBox) {
                VBox tileContainer = (VBox) node;
                tileContainer.setDisable(false);
            }
        }
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
        playerAction.setValue("reset");
        playerAction.setValue("SkipTurn");
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
                        //Platform.exit();
                        try {
                            loadScene(event, "FinalPage");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
        System.out.println("Resign");
    }


    @FXML
    public void StartTutorial(ActionEvent event) throws IOException {
        loadScene(event, "Tutorial");
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
            ipField = new TextField();
            ipField.setText("localhost");
            viewShareData.setHostIp("localhost");
            viewShareData.setHostPort(Integer.parseInt(portField.getText()));
            connectToServer(GameMode.Host);
            viewShareData.setHost(true);
            viewShareData.getViewModel().setGameManager(GameManager.get_instance());
            connectOrLaunchBookScrabbleCommunication();
            loadBoard(event);
        }
    }

    public void toggleStartButton(){
        startGame.setVisible(true);
    }

    private void connectOrLaunchBookScrabbleCommunication() {
        boolean connectionSucceeded = false;
        try{
            Socket server = new Socket("localhost", 6789);
            PrintWriter printWriter = new PrintWriter(server.getOutputStream(), true);
            printWriter.println("connectionCheck");
            connectionSucceeded = true;
            server.close();
        } catch (IOException ignored) {}
        if(!connectionSucceeded){
            MyServer bookScrabbleServer = new MyServer(6789, new BookScrabbleHandler());
            bookScrabbleServer.start();
            viewShareData.setBookScrabbleServer(bookScrabbleServer);
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
            connectToServer(GameMode.Guest);
            viewShareData.setHost(false);
            loadBoard(event);
        }
    }

    public void connectToServer(GameMode gameMode){
        Model newModel = new Model(gameMode, ipField.getText(), Integer.parseInt(portField.getText()), nameField.getText());
        if(gameMode == GameMode.Host){
            ArrayList<String> dictionaries = new ArrayList<>();
            dictionaries.add("alice_in_wonderland.txt");
            //dictionaries.add("Frank Herbert - Dune.txt");
            BookScrabbleCommunication.get_instance().setGameDictionaries(dictionaries);
        }
        GameManagerReceiver clientModelReceiver = new GameManagerReceiver(ipField.getText(), Integer.parseInt(portField.getText()));
        System.out.println("clientModelReceiver was created.(connectToServer)");
        viewShareData.getViewModel().setModel(newModel);
        viewShareData.setGameManagerReceiver(clientModelReceiver);
        initializeHostAction();
        initializePlayerAction();
        System.out.println("Created new model -> " + gameMode);
    }
    @FXML
    public void startGame(){
        Socket server = null;
        try{
            server = new Socket(viewShareData.getHostIp(), viewShareData.getHostPort());
            PrintWriter printWriter = new PrintWriter(server.getOutputStream(),true);
            printWriter.println("startGame");
            server.close();
        } catch (IOException ignored){}
        finally {
            if(server != null && server.isConnected()){
                try {
                    server.close();
                } catch (IOException ignored) {}
            }
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
        controller.setViewShareData(viewShareData);

        Scene scene = null;
        if (Objects.equals(sceneName, "BoardPage")) {
            scene = new Scene(root);
            controller.squareClickHandler();
            controller.initializePlayerAction();
            controller.initializeHostAction();
            controller.bindTilesProperties();
            controller.bindPlayerProperties();
            controller.initializeBoardAction();
            controller.initializeViewModelUpdates();

            if(viewShareData.getHost()) controller.toggleStartButton();
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
    }
    public boolean validPort(String port) {
        return port.matches("(1000[1-9]|100[1-9]\\d|10[1-9]\\d{2}|1[1-9]\\d{3}|19999)");
    }

    public boolean validIp(String ip) {
        return ip.matches("(\\b25[0-5]|\\b2[0-4][0-9]|\\b[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}") || ip.matches("localhost");
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

                    String deletedValue = squareLabel.getText();
                    enableTileContainer(deletedValue);

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
                    int index = boardGridPane.getChildren().indexOf(square);
                    int rowIndex = GridPane.getRowIndex(boardGridPane.getChildren().get(index));
                    int columnIndex = GridPane.getColumnIndex(boardGridPane.getChildren().get(index));
                    placedTiles.remove("" + rowIndex + "," + columnIndex);
                });
            }
        }
    }
    private void enableTileContainer(String value) {
        for (Node node : tileContainerHBox.getChildren()) {
            if (node instanceof VBox) {
                VBox tileContainer = (VBox) node;
                Label tileLabel = (Label) tileContainer.getChildren().get(0);

                if (tileLabel.getText().equals(value)) {
                    tileContainer.setDisable(false);
                    break;
                }
            }
        }
    }


    @FXML
    private void handleDragDetected(MouseEvent event) {

        VBox sourceTileContainer = (VBox) event.getSource();

        int index = getIndexFromTileContainer(sourceTileContainer);
        tileDragged[index] = true;

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

                StackPane stackPane = (StackPane) node;


                squareLabel.setText(tileLabel.getText());
                int index = getIndexFromTileContainer(draggedTileContainer);
                // Disable the dragged TileContainer
                draggedTileContainer.setDisable(true);


                index = boardGridPane.getChildren().indexOf(stackPane);
                int rowIndex = GridPane.getRowIndex(boardGridPane.getChildren().get(index));
                int columnIndex = GridPane.getColumnIndex(boardGridPane.getChildren().get(index));
                placedTiles.put("" + rowIndex + "," + columnIndex, tileLabel.getText());

            });
        }
        success = true;
        event.setDropCompleted(success);
        event.consume();
    }


    @FXML
    private void handleDragDone(DragEvent event) {
//        if (event.getTransferMode() == TransferMode.MOVE) {
//
//        }
//
//        event.consume();
    }

    public void setViewShareData(ViewShareData viewShareData){
        this.viewShareData = viewShareData;
    }
    private int getIndexFromTileContainer(VBox tileContainer) {
        ObservableList<Node> children = tileContainerHBox.getChildren();
        for (int i = 0; i < children.size(); i++) {
            Node child = children.get(i);
            if (child instanceof VBox && child.equals(tileContainer)) {
                return i;
            }
        }
        return -1; // If the tileContainer is not found
    }
}





