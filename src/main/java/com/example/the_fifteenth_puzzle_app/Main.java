package com.example.the_fifteenth_puzzle_app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.LinkedList;
import java.util.Optional;


public class Main extends Application {

    static Tile[] tiles_scope;
    static Tile[] tiles_win;
    public static boolean isCountingMode;
    public static int countTimes;
    public static Text showCountTimes;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Setup object for game and checks
        tiles_scope = new Tile[15];
        tiles_win = new Tile[15];
        setupName(tiles_scope);
        setupPosition(tiles_scope);
        setupName(tiles_win);
        setupPosition(tiles_win);
        Tile.emptyPos = new int[]{3, 3};
        showCountTimes = new Text();
        setupNewGame();

        //Main visual block with button
        VBox buttons_pane = new VBox();
        Button newGame = new Button("New Game");
        Button closeGame = new Button("Close");
        Button changeForm = new Button("Change form");
        buttons_pane.getChildren().add(newGame);
        buttons_pane.getChildren().add(changeForm);
        buttons_pane.getChildren().add(closeGame);
        buttons_pane.getChildren().add(showCountTimes);
        buttons_pane.setMinWidth(150); //temporary, need investigate

        //Button events
        closeGame.setOnAction(event -> {
            Platform.exit();
        });

        newGame.setOnAction(event -> {
            setupNewGame();
        });

        changeForm.setOnAction(event -> {
            for (Tile tile : tiles_scope) {
                tile.changeForm();
            }
        });

        //Add tiles on main visual block
        BorderPane all_pane = new BorderPane();
        all_pane.setLeft(print_square(tiles_scope));
        all_pane.setRight(buttons_pane);

        Scene scene = new Scene(all_pane, 530, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //Method for check win conditions after every move
    public static boolean checkWin (Tile[] data, Tile[] win) {
        boolean res = true;
        for (int i = 0; i < data.length; i++) {
            if (data[i].position[0] != win[i].position[0] || data[i].position[1] != win[i].position[1]) {
                res = false;
                break;
            }
        }
        return res;
    }

    //Some random tiles moves
    public static void randomize (Tile[] data, int comp) {
        for (int i = 0; i < comp; i++) {
            LinkedList<String> neighbors = Tile.getNeighbor(data);
            String r_tile_name;
            boolean isMoved;
            int num = rnd(neighbors.size());
            r_tile_name = neighbors.get(num);
            new Tile();
            Tile r_tile_obj;
            r_tile_obj = Tile.getTile(data, r_tile_name);
            System.out.println(r_tile_obj.name);
            isMoved = r_tile_obj.moveTile();
            //System.out.println(isMoved);
        }
    }

    public static int rnd(int max) {
        return (int) (Math.random() * max);
    }

    public static void setupNewGame(){
        //Random moves when game start
        randomize(tiles_scope, 1); // !!! TEMPORARY
        setupCountingMode();
        showCountTimes.setText(countString(countTimes));
    }

    public static void setupCountingMode(){
        isCountingMode = false; // !!! TEMPORARY
        countTimes = 30;
    }

    public static String countString(int countTimes) {
        return "Steps left - " + countTimes;
    }

    //Move action + check win + win popup
    public static void handleRectangleClick(Tile tile) {
        boolean isMoved = tile.moveTile();

        if (isCountingMode && isMoved) {
            countTimes--;
            if (countTimes == -1) {
                endingAlert(false);
            } else {
                showCountTimes.setText(countString(countTimes));
            }
            if (checkWin(tiles_scope, tiles_win)) {
                endingAlert(true);
            }
        }
    }

    public static void animation (Tile tile) {

    }


    public static void endingAlert(boolean win) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        if (win) {
            alert.setTitle("Win!");
            alert.setHeaderText("Wow, You win!");
        } else {
            alert.setTitle("Lose :(");
            alert.setHeaderText("Next time will be better!");
        }

        ButtonType newGame = new ButtonType("Start new game");
        ButtonType cancelButton = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(newGame, cancelButton);
        alert.setOnCloseRequest(event -> {
            Optional<ButtonType> result = Optional.ofNullable(alert.getResult());
            if (result.isPresent()) {
                if (result.get() == newGame) {
                    setupNewGame();
                } else if (result.get() == cancelButton) {
                    isCountingMode = false;
                    showCountTimes.setText("Free mode");
                }
            }
        });
        alert.showAndWait();
    }

    public static void setupName (Tile[] data) {
        for (int i = 0; i < data.length; i++) {
            data[i] = new Tile();
            if (i < 9) {
                data[i].setName("0" + (i + 1));
            } else {
                data[i].setName("" + (i + 1));
            }
            data[i].addName();
        }
    }

    public static void setupPosition (Tile[] data) {
        for (int i = 0; i < data.length; i++) {
            int y = (i / 4);
            int x = (i % 4);
            data[i].setPosition(x, y);
        }
    }

    //Adding tiles inside their positions, add click
    public static GridPane print_square (Tile[] data) {
        GridPane pane = new GridPane();
        for (int i = 0; i < 4; i++) {
            pane.getColumnConstraints().add(new ColumnConstraints(100));
            pane.getRowConstraints().add(new RowConstraints(100));
        }
        for (int i = 0; i < data.length; i++) {
            Tile tile = data[i];
            pane.add(tile.visual, tile.position[0], tile.position[1]);
            int finalI = i;
            data[i].visual.setOnMouseClicked(event -> handleRectangleClick(data[finalI]));
        }
        return pane;
    }

    // animation move here
}
