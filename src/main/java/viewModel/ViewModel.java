package viewModel;

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

    public StringProperty getMessageFromHost() {
        return messageFromHost;
    }

    public StringProperty getPlayerAction() {
        return playerAction;
    }

    public void initializeHostAction(){
        messageFromHost = new SimpleStringProperty();
        messageFromHost.bind(model.getMessageFromHost());
        model.getMessageFromHost().addListener(((observable, oldAction, newAction) -> {
            manageHostMessage(newAction);
        }));
    }

    public void manageHostMessage(String newAction){
        switch(newAction){
            case "wordInsertSuccessfully" -> System.out.println("Your turn has ended!");
            case "dictionaryNotLegal" -> dictionaryNotLegal();
            case "updateView" -> updateView();
            case "playTurn" -> playTurn();
            case "bindButtons" -> bindButtons();
            case "boardNotLegal" -> boardNotLegal();
            default -> System.out.println("default");
        }
    }

    private void boardNotLegal() {
        System.out.println("reach to view model");
        viewModelUpdates.setValue("boardNotLegal");

    }

    private void bindButtons() {
        viewModelUpdates.setValue("bindButtons");
    }

    private void playTurn() {
        updateButtons();
    }

    private void dictionaryNotLegal(){
        viewModelUpdates.setValue("dictionaryNotLegal");
    }

    public void updateView() {
        Platform.runLater(() -> {
            updateTiles();
            updateButtons();
            updatePlayers();
            updateBoard();
        });
        Board.printBoard(model.getGameManager().board);
    }

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

    private void updateTiles() {
        Player player = model.getGameManager().getPlayers().get(model.getPlayerId());
        List<Tile> tiles = player.getTiles();
        for(int i = 0; i < player.getTiles().size(); i++){
            tileScores.get(i).setValue(String.valueOf(tiles.get(i).score));
            tileLetters.get(i).setValue(String.valueOf(tiles.get(i).letter));
        }
    }

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
