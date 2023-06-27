package viewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.GameManager;
import model.Model;
import scrabble_game.Board;
import view.MainWindowController;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ViewModel {

    Model model;

    StringProperty messageFromHost;
    StringProperty playerAction;
    StringProperty lastWord;

    Label firstTileLetter;
    Label secondTileLetter;
    Label thirdTileLetter;
    Label fourthTileLetter;
    Label fifthTileLetter;
    Label sixTileLetter;
    Label sevenTileLetter;

    Label firstTileScore;
    Label secondTileScore;
    Label thirdTileScore;
    Label fourthTileScore;
    Label fifthTileScore;
    Label sixTileScore;
    Label sevenTileScore;

    List<Label> tileLetters;
    List<Label> tileScores;

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

    public StringProperty getMessageFromHost() {
        return messageFromHost;
    }

    public StringProperty getPlayerAction() {
        return playerAction;
    }

    public void initializeHostAction(){
        messageFromHost.bind(model.getMessageFromHost());
        model.getMessageFromHost().addListener(((observable, oldAction, newAction) -> {
            manageHostMessage(newAction);
        }));
    }

    public void manageHostMessage(String newAction){
        switch(newAction){
            case "wordInsertSuccessfully" -> Board.printBoard(model.getBoard());
            default -> System.out.println("default");
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
        System.out.println("Checking updated gameManager(ViewModel) -> Current players online: " + gameManager.getPlayers().keySet().size());
        model.setGameManager(gameManager);
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
