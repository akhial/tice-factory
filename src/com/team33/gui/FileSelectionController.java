package com.team33.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;

public class FileSelectionController implements Controller {

    private MainApp mainApp;
    private ObservableList<Label> test = FXCollections.observableArrayList();
    private HashMap<Label, JFXButton> buttons = new HashMap<>();

    @FXML
    private VBox levelContainer;

    void setup() {

        test = mainApp.getHelper().getLevels();
        // TODO use real data

        levelContainer.setSpacing(10);

        for(Label label : test) {
            HBox box = new HBox();
            box.setAlignment(Pos.CENTER_LEFT);

            JFXTextField textField = new JFXTextField();
            textField.setPrefWidth(500);

            label.setPrefWidth(100);

            JFXButton button = new JFXButton("");
            button.setId("file-button");
            buttons.put(label, button);

            box.getChildren().add(label);
            box.getChildren().add(textField);
            box.getChildren().add(button);

            levelContainer.getChildren().add(box);
        }
        // TODO return results
    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @Override
    public void cancel() {

    }
}
