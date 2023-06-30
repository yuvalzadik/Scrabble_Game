package view;


import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

    /**    
     * The MainWindowController function is the constructor for the MainWindowController class.
     * It initializes all the properties that are used in this class, including:
     * lastWord, playerAction, placedTiles and startGame. It also calls two other functions to initialize more properties:
     * initializeTileProperties() and initializeNameProperties(). These functions are explained below.
     *
     * @docauthor Trelent
     */
    public MainWindowController(){
        lastWord = new SimpleStringProperty();
        playerAction = new SimpleStringProperty();
        placedTiles = new HashMap<>();
        startGame = new Button();

        initializeTileProperties();
        initializeNameProperties();
        initializeScoreProperties();
    }

    /**    
     * The initializeScoreProperties function initializes the score properties of each player.
    
     * <p>
     *
     * @docauthor Trelent
     */
    private void initializeScoreProperties() {
        firstPlayerScore = new Label();
        secondPlayerScore = new Label();
        thirdPlayerScore = new Label();
        fourthPlayerScore = new Label();
    }

    /**
     * The initializeNameProperties function initializes the name properties of each player.

     * <p>
     *
     * @docauthor Trelent
     */
    private void initializeNameProperties() {
        firstPlayerName = new Label();
        secondPlayerName = new Label();
        thirdPlayerName= new Label();
        fourthPlayerName = new Label();
    }

    /**
     * The initializeTileProperties function initializes the tile properties for each of the seven tiles.
     * It sets their text to be blank and their score to be 0.

     * <p>
     *
     * @docauthor Trelent
     */
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

    /**
     * The initializeBoardAction function is responsible for initializing the board action.
     * It does this by creating a new SimpleStringProperty and binding it to the cellLabel property of the viewModel.
     * The function then adds a listener to that property, which updates the boardCellLabel whenever there is an update in that property.

     * <p>
     *
     * @docauthor Trelent
     */
    public void initializeBoardAction(){
        cellLabel = new SimpleStringProperty();
        ObjectProperty<String> newCellLabel = viewShareData.getViewModel().getCellLabel();
        cellLabel.bind(newCellLabel);
        newCellLabel.addListener(((observable, oldLabel, newLabel) -> updateBoardCellLabel(newLabel)));
    }

    /**
     * The initializeViewModelUpdates function is responsible for initializing the viewModelUpdates property.
     * The viewModelUpdates property is a StringProperty that holds the current state of the View Model's updates.
     * This function binds this StringProperty to a new SimpleStringProperty, which in turn is bound to another
     * SimpleStringProperty held by the View Share Data object. This second SimpleStringProperty holds all       * the updates from our View Model and will be updated whenever there are changes made to it by our Controller class.

     * <p>
     *
     * @docauthor Trelent
     */
    public void initializeViewModelUpdates(){
        viewModelUpdates = new SimpleStringProperty();
        StringProperty newViewModelUpdates = viewShareData.getViewModel().getViewModelUpdates();
        viewModelUpdates.bind(newViewModelUpdates);
        newViewModelUpdates.addListener(((observable, oldAction, newAction) -> {
            handleViewModelUpdates(newAction);
        }));
    }

    /**
     * The handleViewModelUpdates function is a helper function that handles the updates from the view model.
     * It takes in a string, which represents an action to be performed by this function.
     * The switch statement then performs different actions based on what string was passed into it.

     *
     * @param  newAction Determine which action to take
     *
     * @docauthor Trelent
     */
    private void handleViewModelUpdates(String newAction) {
        switch(newAction){
            case "dictionaryNotLegal" -> Platform.runLater(this::showChallengeWindow);
            case "bindButtons" -> Platform.runLater(() -> {
                bindButtonsProperties();
                viewShareData.getViewModel().updateView();
            });
        }
    }

    /**
     * The showChallengeWindow function is called when the user clicks on the &quot;Challenge&quot; button.
     * It displays a confirmation window asking if they would like to challenge the dictionary.
     * If yes, it sets playerAction to &quot;Challenge&quot; and resets word parameters. Otherwise, it updates view model data in ViewShareData class.

     * <p>
     *
     * @docauthor Trelent
     */
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

    /**
     * The updateBoardCellLabel function is used to update the label of a cell on the board.
     * <p>
     *
     * @param  newLabel Update the board cell label
     *
     * @docauthor Trelent
     */
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

    /**
     * The bindButtonsProperties function binds the visibility of the buttons to their respective properties in
     * viewShareData.getViewModel().submit, viewShareData.getViewModel().resign, and viewShareData.getViewModel().skipTurn

     * <p>
     *
     * @docauthor Trelent
     */
    public void bindButtonsProperties(){
        submit.visibleProperty().bind(viewShareData.getViewModel().submit.get().visibleProperty());
        resign.visibleProperty().bind(viewShareData.getViewModel().resign.get().visibleProperty());
        skipTurn.visibleProperty().bind(viewShareData.getViewModel().skipTurn.get().visibleProperty());
        startGame.visibleProperty().bind(viewShareData.getViewModel().startGame.get().visibleProperty());
    }

    /**
     * The bindPlayerProperties function binds the player names and scores to their respective text fields.
     * This allows for the view to be updated when a player's name or score changes.

     * <p>
     *
     * @docauthor Trelent
     */
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

    /**
     * The bindTilesProperties function binds the text properties of each tile to the corresponding
     * property in the view model. This allows for changes made to these properties in the view model
     * to be reflected on screen.

     * <p>
     *
     * @docauthor Trelent
     */
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

    /**
     * The initializeHostAction function is called when the user clicks on the &quot;Host&quot; button.
     * It sets up a new game, and then calls initializeGameAction to start it.

     * <p>
     *
     * @docauthor Trelent
     */
    public void initializeHostAction(){
        viewShareData.getViewModel().initializeHostAction();
    }

    /**
     * The initializePlayerAction function is used to bind the playerAction and lastWord properties of the viewShareData's ViewModel
     * to their respective properties in this class. This allows for changes made in either property to be reflected on both sides.

     * <p>
     *
     * @docauthor Trelent
     */
    public void initializePlayerAction(){
        viewShareData.getViewModel().getPlayerAction().bind(playerAction);
        viewShareData.getViewModel().getLastWord().bind(lastWord);
    }

    /**
     * The Submit function is called when the user clicks on the Submit button.
     * It calls buildWord() to create a string of all letters in the word, then
     * sets playerAction to &quot;Submit,&quot; + lastWord. This will be read by GameController,
     * which will check if it's a valid word and update scores accordingly.

     *
     * @param  event Get the source of the event
     *
     * @docauthor Trelent
     */
    @FXML
    public void Submit(ActionEvent event) throws IOException {
        buildWord();
        System.out.println("From Submit(View)" + lastWord);
        playerAction.set("Submit," + lastWord);
    }

    /**
     * The buildWord function is used to build the word that the player has placed on the board.
     * It does this by first finding where in the grid of tiles it should start building from, and then
     * iterating through each tile until it reaches a null value (which means there are no more letters).
     * The function also determines whether or not a word is vertical or horizontal based on how many rows/columns away from each other they are.

     * <p>
     *
     * @docauthor Trelent
     */
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

    /**
     * The resetWordParameters function clears the placedTiles ArrayList and sets playerWord to an empty string.
     * This function is called after a word has been successfully played, so that the next word can be played.

     * <p>
     *
     * @docauthor Trelent
     */
    public void resetWordParameters(){
        placedTiles.clear();
        playerWord = "";
    }

    /**
     * The ReloadTiles function is called when the user clicks on the &quot;Reload Tiles&quot; button.
     * It sets playerAction to &quot;reset&quot;, which resets all of the tiles in play, and then it sets
     * playerAction to &quot;SwapTiles&quot;, which swaps out any tiles that are not currently in play.

     *
     * @param  event Get the source of the event
     *
     * @docauthor Trelent
     */
    @FXML
    public void ReloadTiles(ActionEvent event) throws IOException {
        playerAction.setValue("reset");
        playerAction.setValue("SwapTiles");
    }

    /**
     * The ShuffleTiles function is used to shuffle the tiles in a random order.
     * It does this by collecting all the VBox elements from the HBox, shuffling them, and then adding them back to the HBox.

     *
     * @param  event Get the source of the event
     *
     * @docauthor Trelent
     */
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




    /**
     * The SkipTurn function is called when the user clicks on the Skip Turn button.
     * It sets playerAction to &quot;reset&quot; and then to &quot;SkipTurn&quot;. This allows for a
     * reset of any previous action that was set, and then it sets playerAction to
     * &quot;SkipTurn&quot;, which will be read by the game engine in order for it to skip
     * over this players turn. The function also closes out of this window after
     * setting playerAction so that we can return into our main game window.


     *
     * @param  event Get the source of the event
     *
     * @docauthor Trelent
     */
    @FXML
    public void SkipTurn(ActionEvent event) throws IOException {
        playerAction.setValue("reset");
        playerAction.setValue("SkipTurn");
    }

    /**
     * The Resign function is called when the user clicks on the Resign button.
     * It displays a confirmation alert to make sure that the user wants to resign,
     * and if they do, it exits out of the game.

     *
     * @param  event Get the source of the action
     *
     * @docauthor Trelent
     */
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


    /**
     * The StartTutorial function is called when the user clicks on the &quot;Tutorial&quot; button.
     * It loads a new scene, which displays a tutorial for how to play the game.

     *
     * @param  event Get the source of the event
     *
     * @docauthor Trelent
     */
    @FXML
    public void StartTutorial(ActionEvent event) throws IOException {
        loadScene(event, "Tutorial");
    }

    /**
     * The StartAsHost function is called when the user clicks on the &quot;Start as Host&quot; button.
     * It checks if all the fields are valid, and if they are, it sets up a connection to
     * localhost (the host's own computer) and launches BookScrabbleCommunication. If any of
     * the fields aren't valid, an error message appears next to that field.

     *
     * @param  event Get the source of the event
     *
     * @docauthor Trelent
     */
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

    /**
     * The toggleStartButton function is used to toggle the visibility of the startGame button.
     * This function is called when a game has ended, and it allows for another game to be started.

     * <p>
     *
     * @docauthor Trelent
     */
    public void toggleStartButton(){
        startGame.setVisible(true);
    }

    /**
     * The connectOrLaunchBookScrabbleCommunication function is used to connect the BookScrabble game to a server.
     * If there is no server, it will launch one.

     * <p>
     *
     * @docauthor Trelent
     */
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

    /**
     * The StartAsGuest function is called when the user clicks on the &quot;Start as Guest&quot; button.
     * It checks if all the fields are valid, and if they are, it connects to a server with
     * GameMode.Guest (which means that this client will be a guest). If any of the fields aren't valid,
     * then an error message appears next to each invalid field. The function also loads up BoardViewController's FXML file and sets viewShareData's host variable to false (since this client is not hosting).

     *
     * @param  event Get the source of the event
     *
     * @docauthor Trelent
     */
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

    /**
     * The connectToServer function is responsible for creating a new model and setting it to the viewModel.
     * It also creates a new GameManagerReceiver, which will be used by the client to receive messages from the server.
     * The function then initializes both hostAction and playerAction, which are used in order to send messages from client to server.

     *
     * @param  gameMode Determine whether the client is a host or not
     *
     * @docauthor Trelent
     */
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
    /**
     * The startGame function is called when the host presses the start game button.
     * It sends a message to all clients that they should begin playing.

     * <p>
     *
     * @docauthor Trelent
     */
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

    /**
     * The loadHostForm function is called when the user clicks on the &quot;Host&quot; button.
     * It loads a new scene, which is the HostPage.fxml file, and displays it to the user.

     *
     * @param  event Get the source of the event
     *
     * @docauthor Trelent
     */
    @FXML
    public void loadHostForm(ActionEvent event) throws IOException {
        loadScene(event, "HostPage");
    }

    /**
     * The loadHomePage function is used to load the HomePage scene when the user clicks on
     * the &quot;Home&quot; button. It takes an ActionEvent as a parameter, and returns nothing.

     *
     * @param  event Get the source of the event
     *
     * @docauthor Trelent
     */
    @FXML
    public void loadHomePage(ActionEvent event) throws IOException {
        loadScene(event, "HomePage");
    }

    /**
     * The loadGuestForm function is called when the user clicks on the &quot;Guest&quot; button.
     * It loads a new scene, which is the GuestPage.fxml file, and displays it to the user.

     *
     * @param  event Get the source of the action
     *
     * @docauthor Trelent
     */
    @FXML
    public void loadGuestForm(ActionEvent event) throws IOException {
        loadScene(event, "GuestPage");
    }

    /**
     * The loadBoard function is called when the user clicks on the &quot;Board&quot; button.
     * It loads a new scene, which displays all of the boards that are currently
     * available to be viewed by this user. The BoardPageController class handles
     * all of these actions.

     *
     * @param  event Get the source of the action
        private void loadscene(actionevent event, string scenename) throws ioexception {
            parent root = fxmlloader
     *
     * @docauthor Trelent
     */
    @FXML
    public void loadBoard(ActionEvent event) throws IOException {
        loadScene(event, "BoardPage");
    }

    /**
     * The Exit function is called when the user clicks on the Exit button.
     * It displays a confirmation alert to make sure that the user wants to exit,
     * and if they do, it exits out of the game.

     *
     * @param  event Get the source of the action
     *
     * @docauthor Trelent
     */
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


    /**
     * The BackToHomePage function is a function that allows the user to go back to the home page.
     *
     *
     * @param  event Handle the event when the button is clicked
     *
     * @docauthor Trelent
     */
    @FXML
    public void BackToHomePage(ActionEvent event) throws IOException {
        loadHomePage(event);
    }

    /**
     * The loadScene function is used to load a new scene into the window.
     * <p>
     *
     * @param  event Get the source of the event
     * @param  sceneName Load the correct scene
        public void loadscene(string scenename) throws ioexception {
            parent root = fxmlloader
     *
     * @docauthor Trelent
     */
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
    /**
     * The validPort function checks to see if the port number is valid.
     * <p>
     *
     * @param  port Check if the port number is valid
     *
     * @return True if the port is a valid port number, and false otherwise
     *
     * @docauthor Trelent
     */
    public boolean validPort(String port) {
        return port.matches("(1000[1-9]|100[1-9]\\d|10[1-9]\\d{2}|1[1-9]\\d{3}|19999)");
    }

    /**
     * The validIp function takes a String as an argument and returns true if the string is a valid IP address.
     * <p>
     *
     * @param  ip Check if the ip address is valid
     *
     * @return True if the string is a valid ip address
     *
     * @docauthor Trelent
     */
    public boolean validIp(String ip) {
        return ip.matches("(\\b25[0-5]|\\b2[0-4][0-9]|\\b[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}") || ip.matches("localhost");
    }

    /**
     * The validName function checks to see if the name entered by the user is valid.
     * <p>
     *
     * @param  name Check if the string contains only letters
     *
     * @return True if the name is valid
     *
     * @docauthor Trelent
     */
    public boolean validName(String name) {
        return name.matches("^[A-Za-z]+$");
    }
    /**
     * The squareClickHandler function is used to make the tiles draggable.
     * It also removes a tile from the board if it is clicked on.

     * <p>
     *
     * @docauthor Trelent
     */
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
                    int index = boardGridPane.getChildren().indexOf(square);
                    int rowIndex = GridPane.getRowIndex(boardGridPane.getChildren().get(index));
                    int columnIndex = GridPane.getColumnIndex(boardGridPane.getChildren().get(index));
                    placedTiles.remove("" + rowIndex + "," + columnIndex);
                });
            }
        }
    }
    /**
     * The handleDragDetected function is called when the user clicks on a tile and drags it.
     * It creates a Dragboard object, which contains information about the drag-and-drop gesture.
     * The function then sets up the content of this Dragboard to be an empty string, and calls setDragView() to specify that no image should be used for dragging (the default).

     *
     * @param  event Get the source of the event
     *
     * @docauthor Trelent
     */
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

    /**
     * The handleDragOver function is called when the user drags a piece over the board.
     * It checks to see if the source of the drag event is not from another cell in this grid pane, and that there is a string on
     * 	the dragboard (which will be true if it's coming from one of our cells). If both are true, then we accept transfer modes for moving.

     *
     * @param  event Determine the source of the drag event
     *
     * @docauthor Trelent
     */
    @FXML
    private void handleDragOver(DragEvent event) {
        if (event.getGestureSource() != boardGridPane && event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.MOVE);
        }

        event.consume();
    }

    /**
     * The handleDragDropped function is called when a drag and drop operation has been completed.
     * It sets the success variable to true, which indicates that the drag and drop operation was successful.
     * The event is consumed so that it does not propagate further up in the scene graph hierarchy.

     *
     * @param  event Get the source of the drag event
     *
     * @docauthor Trelent
     */
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
                StackPane stackPane = (StackPane) node;
                int index = boardGridPane.getChildren().indexOf(stackPane);
                int rowIndex = GridPane.getRowIndex(boardGridPane.getChildren().get(index));
                int columnIndex = GridPane.getColumnIndex(boardGridPane.getChildren().get(index));
                placedTiles.put("" + rowIndex + "," + columnIndex, tileLabel.getText());
            });
        }
        success = true;
        event.setDropCompleted(success);
        event.consume();
    }


    /**
     * The handleDragDone function is called when the user drags a card from the deck to
     * either of the two piles. It checks if there are any cards in either pile, and if not,
     * it adds a card to that pile. If there are cards in both piles, then it checks which
     * one was dragged onto and adds a card to that pile. If neither of these conditions
     * apply (i.e., both piles have cards), then nothing happens because you cannot add more than one
     * card at once from the deck into each of those two piles (this is how solitaire works).


     *
     * @param  event Get the dragboard of the source
     *
     * @docauthor Trelent
     */
    @FXML
    private void handleDragDone(DragEvent event) {

    }

    /**
     * The setViewShareData function is used to set the viewShareData variable in this class.
     * <p>
     *
     * @param  viewShareData Pass data from the viewsharedata class to this class
     *
     * @docauthor Trelent
     */
    public void setViewShareData(ViewShareData viewShareData){
        this.viewShareData = viewShareData;
    }

}





