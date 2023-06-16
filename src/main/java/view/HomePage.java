package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HomePage extends Application {
    private static final int GRID_SIZE = 18;
    private static final int SQUARE_SIZE = 30;
    private static final String[] names = {"null", "Nofar", "Yuvi", "Tamar", "Romi"};

    @Override
    public void start(Stage primaryStage) {
        GridPane gameBoard = createGameBoard();
        HBox buttonBox = createButtonBox(); /*the footer buttons*/
        HBox tileLettersBox = createTileBox(); /*the tiles buttons*/
        VBox leftTabs = createLeftTabs(); /*the players list*/

        VBox bottomSection = new VBox(tileLettersBox, buttonBox);
        bottomSection.setSpacing(10);

        BorderPane root = new BorderPane();
        root.setLeft(leftTabs);
        root.setCenter(gameBoard);
        root.setBottom(bottomSection);
        BorderPane.setMargin(leftTabs, new Insets(10));
        BorderPane.setMargin(gameBoard, new Insets(10));
        BorderPane.setMargin(bottomSection, new Insets(10));

        Scene scene = new Scene(root);
        // Set background color for the scene
        scene.setFill(Color.LIGHTGRAY);

        // Apply CSS styling to the scene
        scene.getStylesheets().add("styles.css");
        /*  scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());*/
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setTitle("Game Board");
        primaryStage.show();
    }

    private GridPane createGameBoard() {
        GridPane gridPane = new GridPane();

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                Rectangle square = createSquare();
                gridPane.add(square, col, row);
            }
        }

        return gridPane;
    }

    private Rectangle createSquare() {
        Rectangle square = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
        square.setFill(Color.WHITE);
        square.setStroke(Color.BLACK);
        return square;
    }

    private HBox createButtonBox() {
        Button b_skip = new Button("Skip");
        Button b_swap = new Button("Swap");
        Button b_Submit = new Button("Submit");
        Button b_challenge = new Button("Challenge");

        HBox buttonBox = new HBox(b_skip, b_swap, b_Submit, b_challenge);
        buttonBox.getStyleClass().add("button-box");
        buttonBox.setSpacing(10);

        return buttonBox;
    }

    private HBox createTileBox() {
        Button b_tile1 = new Button("A");
        Button b_tile2 = new Button("B");
        Button b_tile3 = new Button("C");
        Button b_tile4 = new Button("D");
        Button b_tile5 = new Button("E");
        Button b_tile6 = new Button("F");
        Button b_tile7 = new Button("G");
        b_tile1.getStyleClass().add("tile-button");
        b_tile2.getStyleClass().add("tile-button");
        b_tile3.getStyleClass().add("tile-button");
        b_tile4.getStyleClass().add("tile-button");
        b_tile5.getStyleClass().add("tile-button");
        b_tile6.getStyleClass().add("tile-button");
        b_tile7.getStyleClass().add("tile-button");

        // Create the refresh button
        Image refreshIcon = new Image(getClass().getResourceAsStream("refresh.png"));
        ImageView refreshImageView = new ImageView(refreshIcon);
        refreshImageView.setFitWidth(17);
        refreshImageView.setFitHeight(17);

        Button refreshButton = new Button();
        refreshButton.setGraphic(refreshImageView);
        refreshButton.setOnAction(event -> {
            // Handle refresh button action
            System.out.println("Refresh button clicked");
        });

        // Create the shuffle button
        Image shuffleIcon = new Image(getClass().getResourceAsStream("shuffle.jpg"));
        ImageView shuffleImageView = new ImageView(shuffleIcon);
        shuffleImageView.setFitWidth(17);
        shuffleImageView.setFitHeight(17);

        Button shuffleButton = new Button();
        shuffleButton.setGraphic(shuffleImageView);
        shuffleButton.setOnAction(event -> {
            // Handle refresh button action
            System.out.println("Shuffle button clicked");
        });
        refreshButton.getStyleClass().add("icon-button");
        shuffleButton.getStyleClass().add("icon-button");

        HBox tileBox = new HBox(shuffleButton, b_tile1, b_tile2, b_tile3, b_tile4, b_tile5, b_tile6, b_tile7, refreshButton);
        tileBox.getStyleClass().add("tile-letters-box");
        tileBox.setSpacing(5);

        return tileBox;
    }
    /*
        private VBox createLeftTabs() {
            VBox leftTabs = new VBox();
            leftTabs.getStyleClass().add("left-tabs");
            leftTabs.setSpacing(10);

            for (int i = 1; i <= 4; i++) {
                Rectangle tab = createTab();
                String player_name = names[i];
                char firstChar = player_name.charAt(1);
                String firstCharString = Character.toString(firstChar);
                Label label = createLabel(player_name);
                Label numberLabel = createNumberLabel("5");
                /* Circle circle = createCircle("N"firstCharString);*/
/*
            tab.getStyleClass().add("tab");
            numberLabel.getStyleClass().add("number-label");
            label.getStyleClass().add("label");

            VBox tabContent = new VBox(label, numberLabel);
            tabContent.setSpacing(5);
            tabContent.setAlignment(Pos.CENTER);

            StackPane stackPane = new StackPane(tab, tabContent);
            stackPane.setPadding(new Insets(10));

            leftTabs.getChildren().add(stackPane);
        }

        Rectangle largerTab = createLargerTab();
        Label largerLabel = createLabel("Tile Bag");
        Label largerNumberLabel = createNumberLabel("10");
        largerTab.getStyleClass().add("larger-tab");
        largerLabel.getStyleClass().add("larger-label");


        VBox largerTabContent = new VBox(largerLabel, largerNumberLabel);
        largerTabContent.setSpacing(5);
        largerTabContent.setAlignment(Pos.CENTER);

        StackPane largerStackPane = new StackPane(largerTab, largerTabContent);
        largerStackPane.setPadding(new Insets(10));

        leftTabs.getChildren().add(largerStackPane);

        return leftTabs;
    }
*/
    private VBox createLeftTabs() {
        VBox leftTabs = new VBox();
        leftTabs.getStyleClass().add("left-tabs");
        leftTabs.setSpacing(10);

        for (int i = 1; i <= 4; i++) {
            Rectangle tab = createTab();
            String playerName = names[i];
            char firstChar = playerName.charAt(0);
            String firstCharString = Character.toString(firstChar);
            Label label = createLabel(playerName);
            Label numberLabel = createNumberLabel("5");
            StackPane circle = createCircle(firstCharString);

            tab.getStyleClass().add("tab");
            numberLabel.getStyleClass().add("number-label");
            label.getStyleClass().add("label");

            VBox tabContent = new VBox();
            HBox contentBox = new HBox(circle, tabContent);

            tabContent.getChildren().addAll(label, numberLabel);
            tabContent.setSpacing(5);
            tabContent.setAlignment(Pos.CENTER_RIGHT);

            contentBox.setAlignment(Pos.CENTER_LEFT);
            contentBox.setSpacing(5);

            StackPane stackPane = new StackPane(tab, contentBox);
            stackPane.setPadding(new Insets(10));

            leftTabs.getChildren().add(stackPane);
        }

        Rectangle largerTab = createTab();
        Label largerLabel = createLabel("Tile Bag");
        Label largerNumberLabel = createNumberLabel("10");

        largerTab.getStyleClass().add("larger-tab");
        largerLabel.getStyleClass().add("larger-label");

        VBox largerTabContent = new VBox(largerLabel, largerNumberLabel);
        largerTabContent.setSpacing(5);
        largerTabContent.setAlignment(Pos.CENTER);

        StackPane largerStackPane = new StackPane(largerTab, largerTabContent);
        largerStackPane.setPadding(new Insets(10));

        leftTabs.getChildren().add(largerStackPane);

        return leftTabs;
    }
    private StackPane createCircle(String text) {
        Circle circle = new Circle(25, Color.PINK);
        Text circleText = new Text(text);
        circleText.setFill(Color.WHITE);
        circleText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 1px;-fx-margin-left:2px");

        StackPane circlePane = new StackPane(circle, circleText);
        StackPane.setAlignment(circleText, Pos.CENTER);

        return circlePane;
    }
    private Rectangle createLargerTab() {
        Rectangle largerTab = new Rectangle(300, 120);
        largerTab.setFill(Color.WHITE);
        largerTab.setStroke(Color.BLACK);
        return largerTab;
    }
    private Rectangle createTab() {
        Rectangle tab = new Rectangle(150, 80);

        Stop[] stops = new Stop[]{
                new Stop(0, Color.web("#C5E0DC")),
                new Stop(0.5, Color.web("#C5E0DC")),
                new Stop(0.5, Color.WHITE),
                new Stop(1, Color.WHITE)
        };

        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        tab.setFill(gradient);
        tab.setStroke(Color.BLACK);
        tab.getStyleClass().add("tab");
        return tab;
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-weight: bold");
        return label;
    }

    private Label createNumberLabel(String number) {
        Label numberLabel = new Label(number);
        numberLabel.setStyle("-fx-font-size: 20px");
        return numberLabel;
    }

    public static void main(String[] args) {
        launch(args);
    }
}