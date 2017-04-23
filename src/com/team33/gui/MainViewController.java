package com.team33.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class MainViewController implements Controller {

    @FXML
    private GridPane gridPane;

    @FXML
    private ImageView userImage;

    @FXML
    protected void initialize() {
        Circle c = new Circle(20);
        c.setCenterX(userImage.getFitWidth()/2);
        c.setCenterY(userImage.getFitHeight()/2);
        userImage.setClip(c);
    }

    public void setScene(String scene) {
        if(gridPane.getChildren().size() > 3)
            gridPane.getChildren().remove(3);
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource(scene));
        } catch(IOException e) {
            e.printStackTrace();
        }
        gridPane.add(parent, 1, 1);
    }

    @Override
    public void setMainApp(MainApp mainApp) {

    }

    @Override
    public void cancel() {

    }
}
