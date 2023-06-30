package viewmodel;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import model.GameManager;
import model.Model;
import model.Player;
import scrabble_game.Board;
import scrabble_game.Tile;

import java.util.ArrayList;
import java.util.List;

public class ViewModel {

    Model model;

    StringProperty messageFromHost;
    StringProperty playerAction;
    StringProperty lastWord;

    public SimpleStringProperty firstPlayerName;
    public SimpleStringProperty secondPlayerName;
    public SimpleStringProperty thirdPlayerName;
    public SimpleStringProperty fourthPlayerName;

    public SimpleStringProperty firstPlayerScore;
    public SimpleStringProperty secondPlayerScore;
    public SimpleStringProperty thirdPlayerScore;
    public SimpleStringProperty fourthPlayerScore;

    public SimpleStringProperty firstTileLetter;
    public SimpleStringProperty secondTileLetter;
    public SimpleStringProperty thirdTileLetter;
    public SimpleStringProperty fourthTileLetter;
    public SimpleStringProperty fifthTileLetter;
    public SimpleStringProperty sixTileLetter;
    public SimpleStringProperty sevenTileLetter;

    public StringProperty firstTileScore;
    public StringProperty secondTileScore;
    public StringProperty thirdTileScore;
    public StringProperty fourthTileScore;
    public StringProperty fifthTileScore;
    public StringProperty sixTileScore;
    public StringProperty sevenTileScore;

    public ObjectProperty<String> cellLabel;

    public ObjectProperty<Button> submit;
    public ObjectProperty<Button> resign;
    public ObjectProperty<Button> startGame;
    public ObjectProperty<Button> skipTurn;

    public StringProperty viewModelUpdates;

    List<StringProperty> tileLetters;
    List<StringProperty> tileScores;

    List<StringProperty> playersNames;
    List<StringProperty> playersScores;

    /**
     * The ViewModel function is the constructor for the ViewModel class.
     * It initializes all of the properties that are used in this class, and sets up listeners to handle changes in those properties.

     * <p>
     *
     * @docauthor Trelent
     */
    public ViewModel(){
        model = null;
        messageFromHost = new SimpleStringProperty();
        playerAction = new SimpleStringProperty();
        playerAction.addListener(((observable, oldAction, newAction) -> {
            if(newAction.equals("reset")) return;
            managePlayerAction(newAction);
        }));
        lastWord = new SimpleStringProperty();
        tileLetters = new ArrayList<>();
        tileScores = new ArrayList<>();
        playersNames = new ArrayList<>();
        playersScores = new ArrayList<>();
        initializeButtons();
        initializeTileProperty();
        initializePlayerProperties();
    }

    /**
     * The initializePlayerProperties function initializes the player properties.
     * It creates a new SimpleStringProperty for each of the four players' names and scores,
     * then adds them to their respective ArrayLists.

     * <p>
     *
     * @docauthor Trelent
     */
    private void initializePlayerProperties() {
        firstPlayerName = new SimpleStringProperty();
        secondPlayerName = new SimpleStringProperty();
        thirdPlayerName = new SimpleStringProperty();
        fourthPlayerName = new SimpleStringProperty();
        firstPlayerScore = new SimpleStringProperty();
        secondPlayerScore = new SimpleStringProperty();
        thirdPlayerScore = new SimpleStringProperty();
        fourthPlayerScore = new SimpleStringProperty();

        playersNames.add(firstPlayerName);
        playersNames.add(secondPlayerName);
        playersNames.add(thirdPlayerName);
        playersNames.add(fourthPlayerName);

        playersScores.add(firstPlayerScore);
        playersScores.add(secondPlayerScore);
        playersScores.add(thirdPlayerScore);
        playersScores.add(fourthPlayerScore);
    }

    /**
     * The initializeTileProperty function initializes the tile properties for each of the seven tiles.
     * The function also adds these properties to two ArrayLists, one for letters and one for scores.

     * <p>
     *
     * @docauthor Trelent
     */
    public void initializeTileProperty(){
        firstTileLetter = new SimpleStringProperty();
        secondTileLetter = new SimpleStringProperty();
        thirdTileLetter = new SimpleStringProperty();
        fourthTileLetter = new SimpleStringProperty();
        fifthTileLetter = new SimpleStringProperty();
        sixTileLetter = new SimpleStringProperty();
        sevenTileLetter = new SimpleStringProperty();

        firstTileScore = new SimpleStringProperty();
        secondTileScore = new SimpleStringProperty();
        thirdTileScore = new SimpleStringProperty();
        fourthTileScore = new SimpleStringProperty();
        fifthTileScore = new SimpleStringProperty();
        sixTileScore = new SimpleStringProperty();
        sevenTileScore = new SimpleStringProperty();

        tileLetters.add(firstTileLetter);
        tileLetters.add(secondTileLetter);
        tileLetters.add(thirdTileLetter);
        tileLetters.add(fourthTileLetter);
        tileLetters.add(fifthTileLetter);
        tileLetters.add(sixTileLetter);
        tileLetters.add(sevenTileLetter);

        tileScores.add(firstTileScore);
        tileScores.add(secondTileScore);
        tileScores.add(thirdTileScore);
        tileScores.add(fourthTileScore);
        tileScores.add(fifthTileScore);
        tileScores.add(sixTileScore);
        tileScores.add(sevenTileScore);
    }

    /**
     * The initializeButtons function initializes the buttons that are used in the game.
     * It sets up a new button for each of the four buttons that are used in this game.

     * <p>
     *
     * @docauthor Trelent
     */
    public void initializeButtons(){
        submit = new SimpleObjectProperty<>();
        resign = new SimpleObjectProperty<>();
        skipTurn = new SimpleObjectProperty<>();
        startGame = new SimpleObjectProperty<>();

        submit.set(new Button());
        resign.set(new Button());
        startGame.set(new Button());
        skipTurn.set(new Button());
    }

    /**
     * The getMessageFromHost function returns the messageFromHost property.
     * <p>
     *
     *
     * @return The messagefromhost property
     *
     * @docauthor Trelent
     */
    public StringProperty getMessageFromHost() {
        return messageFromHost;
    }

    /**
     * The getPlayerAction function returns the playerAction property.
     * <p>
     *
     *
     * @return A stringproperty object
     *
     * @docauthor Trelent
     */
    public StringProperty getPlayerAction() {
        return playerAction;
    }

    /**
     * The initializeHostAction function is called by the initialize function in the Controller class.
     * It sets up a listener for changes to the messageFromHost property of the model, and calls manageHostMessage when it detects a change.

     * <p>
     *
     * @docauthor Trelent
     */
    public void initializeHostAction(){
        messageFromHost = new SimpleStringProperty();
        messageFromHost.bind(model.getMessageFromHost());
        model.getMessageFromHost().addListener(((observable, oldAction, newAction) -> {
            manageHostMessage(newAction);
        }));
    }

    /**
     * The manageHostMessage function is a switch statement that takes in the action string from the server and performs
     * an action based on what it receives. The actions are as follows:
     * wordInsertSuccessfully - prints out &quot;Your turn has ended!&quot; to let the user know their turn is over.
     * dictionaryNotLegal - calls the dictionaryNotLegal function which displays an alert box telling them their word was not legal.
     * updateView - calls updateView which updates all of our views with new information from our model (the board, score, etc.)
     * playTurn - calls playTurn which allows us to make a move by clicking on
     *
     * @param  newAction Determine which action to perform
     *
     * @docauthor Trelent
     */
    public void manageHostMessage(String newAction){
        switch(newAction){
            case "wordInsertSuccessfully" -> System.out.println("Your turn has ended!");
            case "dictionaryNotLegal" -> dictionaryNotLegal();
            case "updateView" -> updateView();
            case "playTurn" -> playTurn();
            case "bindButtons" -> bindButtons();
            default -> System.out.println("default");
        }
    }

    /**
     * The bindButtons function binds the buttons to their respective functions.
     * The bindButtons function is called in the onCreate method of MainActivity.java

     * <p>
     *
     * @docauthor Trelent
     */
    private void bindButtons() {
        viewModelUpdates.setValue("bindButtons");
    }

    /**
     * The playTurn function is the main function of the game. It updates all buttons,
     * checks if a player has won, and switches turns.

     * <p>
     *
     * @docauthor Trelent
     */
    private void playTurn() {
        updateButtons();
    }

    /**
     * The dictionaryNotLegal function is called when the user attempts to load a dictionary that does not exist.
     * It sets the value of viewModelUpdates to &quot;dictionaryNotLegal&quot; so that the View can display an error message.

     * <p>
     *
     * @docauthor Trelent
     */
    private void dictionaryNotLegal(){
        viewModelUpdates.setValue("dictionaryNotLegal");
    }

    /**
     * The updateView function is called by the controller whenever a change in the model occurs.
     * It updates all the GUI elements to reflect this change.

     * <p>
     *
     * @docauthor Trelent
     */
    public void updateView() {
        Platform.runLater(() -> {
            updateTiles();
            updateButtons();
            updatePlayers();
            updateBoard();
        });
        Board.printBoard(model.getGameManager().board);
    }

    /**
     * The updateBoard function is called by the update function in order to update the board.
     * It iterates through each tile on the gameboard and updates its corresponding label with
     * either a letter or &quot;default&quot; if there is no tile on that space.

     * <p>
     *
     * @docauthor Trelent
     */
    private void updateBoard() {
        int current = 0;
        Tile[][] gameBoard = getGameManager().board.getTiles();
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if(gameBoard[i][j] != null) cellLabel.set(current + "," + gameBoard[i][j].letter);
                else cellLabel.set(current + "," + "default");
                current++;
            }
        }
    }

    /**
     * The updatePlayers function updates the players' names and scores in the game.
     * <p>
     *
     *
     * @docauthor Trelent
     */
    private void updatePlayers() {
        ArrayList<Player> playersList = new ArrayList<>(model.getGameManager().getPlayers().values());
        for(int i = 0; i < playersNames.size(); i++){
            if(i < playersList.size()){
                playersNames.get(i).setValue(playersList.get(i).getName());
                playersScores.get(i).setValue("" + playersList.get(i).getScore());
            }
            else{
                playersNames.get(i).setValue("");
                playersScores.get(i).setValue("");
            }
        }
    }

    /**
     * The updateButtons function is called whenever the game state changes.
     * It updates the visibility of buttons based on whether or not it is currently
     * this player's turn, and if so, whether they have submitted their move yet.

     * <p>
     *
     * @docauthor Trelent
     */
    private void updateButtons() {
        if(!getGameManager().gameStarted || getGameManager().turnManager.getCurrentTurnIndex() == -1) return;
        if(getGameManager().turnManager.getCurrentTurn() == model.getPlayerId()){
            submit.get().setVisible(true);
            skipTurn.get().setVisible(true);
            resign.get().setVisible(true);
            startGame.get().setVisible(false);
        }
        else{
            submit.get().setVisible(false);
            skipTurn.get().setVisible(false);
            resign.get().setVisible(false);
            startGame.get().setVisible(false);
        }
    }

    /**
     * The updateTiles function updates the tiles in the player's hand.
     * <p>
     *
     *
     * @docauthor Trelent
     */
    private void updateTiles() {
        Player player = model.getGameManager().getPlayers().get(model.getPlayerId());
        List<Tile> tiles = player.getTiles();
        for(int i = 0; i < player.getTiles().size(); i++){
            tileScores.get(i).setValue(String.valueOf(tiles.get(i).score));
            tileLetters.get(i).setValue(String.valueOf(tiles.get(i).letter));
        }
    }

    /**
     * The managePlayerAction function is responsible for managing the actions of a player.
     * It takes in a string that represents an action and then performs the appropriate action.
     * The function will call tryPlaceWord, challenge, skipTurn or swapTiles depending on what it receives as input.

     *
     * @param  newAction Determine what the player wants to do
     *
     * @docauthor Trelent
     */
    public void managePlayerAction(String newAction){
        String[] splitted = newAction.split(",");
        switch(splitted[0]){
            case "Submit" -> {
                System.out.println("managePlayerAction -> Submit");
                tryPlaceWord();
            }
            case "Challenge" -> challenge();
            case "SkipTurn" -> skipTurn();
            case "SwapTiles" -> swapTiles();
            default -> System.out.println("default");
        }
    }

    private void swapTiles() {
        model.swapTiles();
    }

    private void skipTurn() {
        model.skipTurn();
    }

    public void challenge(){
        String[] splittedStr = lastWord.get().split(",");
        model.challenge(splittedStr[0], Integer.parseInt(splittedStr[1])
                , Integer.parseInt(splittedStr[2]), Boolean.parseBoolean(splittedStr[3]));
    }

    public void tryPlaceWord(){
        String[] splittedStr = lastWord.get().split(",");
        model.tryPlaceWord(splittedStr[0], Integer.parseInt(splittedStr[1])
                , Integer.parseInt(splittedStr[2]), Boolean.parseBoolean(splittedStr[3]));
    }



    public void setGameManager(GameManager gameManager) {
        model.setGameManager(gameManager);
        updateView();
    }

    public GameManager getGameManager() {
        return model.getGameManager();
    }

    public void setModel(Model newModel) {
        this.model = newModel;

    }

    public StringProperty getLastWord() {
        return lastWord;
    }

    public Model getModel() {
        return model;
    }

    public ObjectProperty<String> getCellLabel() {
        cellLabel = new SimpleObjectProperty<>();
        return cellLabel;
    }

    public StringProperty getViewModelUpdates() {
        viewModelUpdates = new SimpleStringProperty();
        return viewModelUpdates;
    }
}
