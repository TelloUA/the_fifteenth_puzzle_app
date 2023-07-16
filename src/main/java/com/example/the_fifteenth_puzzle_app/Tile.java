package com.example.the_fifteenth_puzzle_app;

import javafx.animation.TranslateTransition;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;


import java.util.LinkedList;
import java.util.Objects;

public class Tile extends Pane {

    public String name;
    //position[0] = x
    //position[1] = y
    public int[] position;
    public StackPane visual = new StackPane();
    public static int[] emptyPos; //empty_position
    private static final int STEP = 1;
    private boolean isHeart;

    //small duration, because have bug with fast click
    private static final Duration ANIMATION_DURATION = Duration.millis(200);
    private static final int BLOCK_SIZE = 100;

    //Setup default name, position away from field and start type of form - rectangle
    public Tile() {
        name = "00";
        position = new int[] {-2, -2};
        visualRectangle();
    }

    //Using when changing form between rectangles and hearts
    public void changeForm() {
        visual.getChildren().clear();
        if (isHeart) {
            visualRectangle();
        } else {
            visualHeart();
        }
        addName();
    }

    //Setup visual rectangle
    public void visualRectangle(){
        isHeart = false;
        String color = "-fx-fill: #6b8e23; ";
        Rectangle r1 = new Rectangle(80, 80);
        r1.setStyle(color);
        visual.getChildren().add(r1);
    }

    //Setup visual heart
    public void visualHeart(){
        isHeart = true;
        String color = "-fx-fill: #ed24e3; ";
        Rectangle h1 = new Rectangle(50, 50);
        Circle h2 = new Circle(25);
        Circle h3 = new Circle(25);
        h1.setStyle(color + "-fx-rotate: 45;");
        h2.setStyle(color + "-fx-translate-x: 18px; -fx-translate-y: -18px;");
        h3.setStyle(color + "-fx-translate-x: -18px; -fx-translate-y: -18px;");
        visual.getChildren().add(h1);
        visual.getChildren().add(h2);
        visual.getChildren().add(h3);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPosition (int x, int y) {
        this.position[0] = x;
        this.position[1] = y;
    }

    //Add numbers (name of tile) on figures
    public void addName() {
        Text name = new Text(this.name);
        if (isHeart) {
            name.setStyle("-fx-font-size: 20px; -fx-translate-y: -9px;");
        } else {
            name.setStyle("-fx-font-size: 20px;");
        }
        visual.getChildren().add(name);
    }


    //For debug
    public void printInfo () {
        System.out.println("Number: " + this.name + ". Position: " + this.position[0] + "-" + this.position[1]);
    }

    //Take tile name (number), for finding neighbors
    public static String getName(Tile[] data, int x, int y) {
        for (Tile datum : data) {
            if (datum.position[0] == x) {
                if (datum.position[1] == y) {
                    return datum.name;
                }
            }
        }
        return "__";
    }

    public static Tile getTile (Tile[] data, String name) {
        Tile t = new Tile();
        for (int i = 0; i < data.length; i++) {
            if (Objects.equals(data[i].name, name)) {
                t = data[i];
            }
        }
        return t;
    }

    public boolean moveTile(boolean isRandomize) {
        System.out.println("Empty " + emptyPos[0] + "-" + emptyPos[1] + ", Num" + this.name + " " + this.position[0] + "-" + this.position[1]);
        //System.out.println("In moveTile()");

        boolean isMoved = false;
        int[] moveDir;
        String dir;
        if (this.position[0] == emptyPos[0] && this.position[1] == emptyPos[1] + STEP) { //y- (up)
            moveDir = new int[]{0, -1};
            dir = "up";
            isMoved = this.setupNewOldPosition(moveDir, dir, isRandomize);
        } else if (this.position[0] == emptyPos[0] && this.position[1] == emptyPos[1] - STEP) { //y+ (down)
            moveDir = new int[]{0, 1};
            dir = "down";
            isMoved = this.setupNewOldPosition(moveDir, dir, isRandomize);
        } else if (this.position[1] == emptyPos[1] && this.position[0] == emptyPos[0] + STEP) { //x- (left)
            moveDir = new int[]{-1, 0};
            dir = "left";
            isMoved = this.setupNewOldPosition(moveDir, dir, isRandomize);
        } else if (this.position[1] == emptyPos[1] && this.position[0] == emptyPos[0] - STEP) { //x+ (right)
            moveDir = new int[]{1, 0};
            dir = "right";
            isMoved = this.setupNewOldPosition(moveDir, dir, isRandomize);
        }
        System.out.println("In moveTile(), " + isMoved);
        return isMoved;
    }

    private boolean setupNewOldPosition (int[] moveDir, String dir, boolean isRandomize) {
        int[] previous = new int[] {this.position[0], this.position[1]};
        System.out.println("moveDir - " + moveDir[0] + ", " + moveDir[1] + " - " + dir);
        if (isRandomize) {
            //old moves with setConstraints, didn't work with animation
            GridPane.setConstraints(this.visual, Tile.emptyPos[0], Tile.emptyPos[1]);
            //System.out.println("Current - " + GridPane.getColumnIndex(this.visual) + "-" + GridPane.getRowIndex(this.visual));
        } else {
            this.moveVisualTo(moveDir[0], moveDir[1]);
        }
        this.setPosition(emptyPos[0], emptyPos[1]);
        emptyPos = previous;
        System.out.println("Empty " + emptyPos[0] + "-" + emptyPos[1] + ", Num" + this.name + " " + this.position[0] + "-" + this.position[1]);
        return true;
    }

    private void moveVisualTo(int column, int row) {
        // Calculate the destination coordinates
        double destinationX = column * BLOCK_SIZE;
        double destinationY = row * BLOCK_SIZE;
        System.out.println("Destination - " + destinationX + ", " + destinationY);

        // Create a TranslateTransition to animate the visual's movement
        TranslateTransition transition = new TranslateTransition(ANIMATION_DURATION, this.visual);
        //System.out.println("Our x & y - " + transition.getFromX() + ", " + transition.getFromY());
        transition.setByX(destinationX);
        transition.setByY(destinationY);
        transition.play();
    }

    public static LinkedList<String> getNeighbor (Tile[] data) {
        LinkedList<String> neighbors = new LinkedList<>();
        String check = "";
        check = Tile.getName(data, (emptyPos[0] - STEP), emptyPos[1]);
        if (!Objects.equals(check, "__"))
            neighbors.add(check);
        check = Tile.getName(data,(emptyPos[0]), (emptyPos[1] + STEP));
        if (!Objects.equals(check, "__"))
            neighbors.add(check);
        check = Tile.getName(data, (emptyPos[0] + STEP), emptyPos[1]);
        if (!Objects.equals(check, "__"))
            neighbors.add(check);
        check = Tile.getName(data,(emptyPos[0]), (emptyPos[1] - STEP));
        if (!Objects.equals(check, "__"))
            neighbors.add(check);
        return neighbors;
    }

}

