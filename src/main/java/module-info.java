module com.example.scrabble_game {
    requires javafx.controls;
    requires javafx.fxml;

    opens view to javafx.fxml;
    exports view;
}