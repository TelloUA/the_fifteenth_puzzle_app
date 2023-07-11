module com.example.the_fifteenth_puzzle_app {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.the_fifteenth_puzzle_app to javafx.fxml;
    exports com.example.the_fifteenth_puzzle_app;
}