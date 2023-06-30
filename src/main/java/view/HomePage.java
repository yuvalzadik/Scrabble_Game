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

    /**
     * The start function is the main function of the program. It creates a new
     * GridPane object called gameBoard, which will be used to display all the
     * tiles in our Scrabble board. The start function also creates a new HBox
     * object called buttonBox, which will be used to display all of our buttons at
     * the bottom of our window. Finally, it creates a VBox object called leftTabs,
     * which will be used to display information about each player on their own tab

     *
     * @param  primaryStage Stage Set the title of the window
        public static void main(string[] args) {
            launch(args);
        }

        private gridpane creategameboard() {


     *
     * @docauthor Trelent
     */
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

    /**
     * The createGameBoard function creates a grid of squares that will be used to play the game.
     * <p>
     *
     *
     * @return A gridpane object
     *
     * @docauthor Trelent
     */
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

    /**
     * The createSquare function creates a square with the size of SQUARE_SIZE and fills it with white color.
     * It also sets the stroke to black.

     * <p>
     *
     * @return A rectangle object
     *
     * @docauthor Trelent
     */
    private Rectangle createSquare() {
        Rectangle square = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
        square.setFill(Color.WHITE);
        square.setStroke(Color.BLACK);
        return square;
    }

    /**
     * The createButtonBox function creates a HBox containing the buttons for the game.
     * <p>
     *
     *
     * @return A hbox
     *
     * @docauthor Trelent
     */
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

    /**
     * The createTileBox function creates a HBox containing the buttons for each tile.
     * <p>
     *
     *
     * @return An hbox object
     *
     * @docauthor Trelent
     */
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

    /**
     * The createLeftTabs function creates the left tabs that are displayed on the screen.
     * <p>
     *
     *
     * @return A vbox
     *
     * @docauthor Trelent
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
    /**
     * The createCircle function creates a circle with the given text in it.
     * <p>
     *
     * @param  text Set the text of the text object
     *
     * @return A stackpane object
     *
     * @docauthor Trelent
     */
    private StackPane createCircle(String text) {
        Circle circle = new Circle(25, Color.PINK);
        Text circleText = new Text(text);
        circleText.setFill(Color.WHITE);
        circleText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 1px;-fx-margin-left:2px");

        StackPane circlePane = new StackPane(circle, circleText);
        StackPane.setAlignment(circleText, Pos.CENTER);

        return circlePane;
    }
    /**
     * The createLargerTab function creates a larger tab that is used to display the
     * information of the selected tab. It returns a rectangle with dimensions 300x120,
     * filled with white and outlined in black.

     * <p>
     *
     * @return A rectangle object
     *
     * @docauthor Trelent
     */
    private Rectangle createLargerTab() {
        Rectangle largerTab = new Rectangle(300, 120);
        largerTab.setFill(Color.WHITE);
        largerTab.setStroke(Color.BLACK);
        return largerTab;
    }
    /**
     * The createTab function creates a rectangle with the dimensions of 150 by 80.
     * It then sets up an array of stops, which are used to create a linear gradient.
     * The linear gradient is set as the fill for the rectangle and it's stroke is set to black.

     * <p>
     *
     * @return A rectangle
     *
     * @docauthor Trelent
     */
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

    /**
     * The createLabel function creates a label with the given text and sets its style to bold.
     * <p>
     *
     * @param  text Set the text of the label
     *
     * @return A label object
     *
     * @docauthor Trelent
     */
    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-weight: bold");
        return label;
    }

    /**
     * The createNumberLabel function creates a Label object with the given number as its text.
     *
     *
     * @param  number Create the label
     *
     * @return A label object that contains a number
     *
     * @docauthor Trelent
     */
    private Label createNumberLabel(String number) {
        Label numberLabel = new Label(number);
        numberLabel.setStyle("-fx-font-size: 20px");
        return numberLabel;
    }

    /**
     * The main function of the program.
     * <p>
     *
     * @param  args Pass command line arguments to the application
     *
     * @docauthor Trelent
     */
    public static void main(String[] args) {
        launch(args);
    }
}