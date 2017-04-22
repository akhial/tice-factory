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
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("/fxml/DashboardView.fxml"));
        } catch(IOException e) {
            e.printStackTrace();
        }
        gridPane.add(parent, 1, 1);
        Circle c = new Circle(20);
        c.setCenterX(userImage.getFitWidth()/2);
        c.setCenterY(userImage.getFitHeight()/2);
        userImage.setClip(c);
    }

    @Override
    public void setMainApp(MainApp mainApp) {

    }

    @Override
    public void cancel() {

    }
}
