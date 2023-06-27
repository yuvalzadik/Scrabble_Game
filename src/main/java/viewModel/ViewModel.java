package viewModel;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.GameManager;
import model.Model;
import model.Player;
import scrabble_game.Board;
import scrabble_game.Tile;
import view.MainWindowController;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ViewModel {

    Model model;

    StringProperty messageFromHost;
    StringProperty playerAction;
    StringProperty lastWord;

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

    List<StringProperty> tileLetters;
    List<StringProperty> tileScores;

    public ViewModel(){
        model = null;
        messageFromHost = new SimpleStringProperty();
        playerAction = new SimpleStringProperty();
        playerAction.addListener(((observable, oldAction, newAction) -> {
            managePlayerAction(newAction);
        }));
        lastWord = new SimpleStringProperty();
        tileLetters = new ArrayList<>();
        tileScores = new ArrayList<>();
        initializeTileProperty();
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
            case "updateView" -> updateView();
            default -> System.out.println("default");
        }
    }

    private void updateView() {
        Platform.runLater(() -> {
            updateTiles();
            updateButtons();
            updatePlayers();
            updateBoard();
        });
    }

    private void updateBoard() {
    }

    private void updatePlayers() {
    }

    private void updateButtons() {
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
        switch(newAction){
            case "Submit" -> {
                System.out.println("managePlayerAction -> Submit");
                tryPlaceWord();
            }
            default -> System.out.println("default");
        }
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
}
