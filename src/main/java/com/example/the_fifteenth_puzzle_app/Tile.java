package com.example.the_fifteenth_puzzle_app;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


import java.util.LinkedList;
import java.util.Objects;

public class Tile extends Pane {

    public String name;
    public byte[] position;
    public StackPane visual = new StackPane();
    public static byte[] ep; //empty_position
    private static final byte step = 1;
    private boolean isHeart;

    //Setup default name, position away from field and start type of form - rectangle
    public Tile() {
        name = "00";
        position = new byte[] {-2, -2};
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

    public void setPosition (byte a, byte b) {
        this.position[0] = a;
        this.position[1] = b;
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
    public static String getName(Tile[] data, byte a, byte b) {
        for (Tile datum : data) {
            if (datum.position[0] == a) {
                if (datum.position[1] == b) {
                    return datum.name;
                }
            }
        }
        return "__";
    }

    public static Tile getTile (Tile[] data, String name) {
        Tile a = new Tile();
        for (int i = 0; i < data.length; i++) {
            if (Objects.equals(data[i].name, name)) {
                a = data[i];
            }
        }
        return a;
    }

    public boolean moveTile() {
        // System.out.println("Empty " + ep[0] + "-" + ep[1] + "\nTile " + this.position[0] + "-" + this.position[1]);
        boolean isMoved = false;
        if (this.position[0] == ep[0] && this.position[1] == ep[1] + step) {
            isMoved = setupNewOldPosition();
        } else if (this.position[0] == ep[0] && this.position[1] == ep[1] - step) {
            isMoved = setupNewOldPosition();
        } else if (this.position[1] == ep[1] && this.position[0] == ep[0] + step) {
            isMoved = setupNewOldPosition();
        } else if (this.position[1] == ep[1] && this.position[0] == ep[0] - step) {
            isMoved = setupNewOldPosition();
        }
        return isMoved;
    }

    private boolean setupNewOldPosition () {
        byte[] previous = new byte[] {this.position[0], this.position[1]};
        GridPane.setConstraints(this.visual, Tile.ep[1], Tile.ep[0]);
        this.setPosition(ep[0], ep[1]);
        ep = previous;
        return true;
    }

    public static LinkedList<String> getNeighbor (Tile[] data) {
        LinkedList<String> neighbors = new LinkedList<>();
        String check = "";
        check = Tile.getName(data, (byte) (ep[0] - step), ep[1]);
        if (!Objects.equals(check, "__"))
            neighbors.add(check);
        check = Tile.getName(data,(ep[0]), (byte) (ep[1] + step));
        if (!Objects.equals(check, "__"))
            neighbors.add(check);
        check = Tile.getName(data, (byte) (ep[0] + step), ep[1]);
        if (!Objects.equals(check, "__"))
            neighbors.add(check);
        check = Tile.getName(data,(ep[0]), (byte) (ep[1] - step));
        if (!Objects.equals(check, "__"))
            neighbors.add(check);
        return neighbors;
    }

}

