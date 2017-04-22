package com.team33.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class MainViewController implements Controller {

    @FXML
    private GridPane gridPane;

    @FXML
    protected void initialize() {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("/fxml/DashboardView.fxml"));
        } catch(IOException e) {
            e.printStackTrace();
        }
        gridPane.add(parent, 1, 1);
    }

    @Override
    public void cancel() {

    }
}
