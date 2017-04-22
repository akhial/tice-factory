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

import java.util.ArrayList;

public class FileSelectionController implements Controller {

    private ObservableList<Label> test = FXCollections.observableArrayList();

    private ArrayList<JFXButton> buttons = new ArrayList<>();

    @FXML
    private VBox levelContainer;

    @FXML
    protected void initialize() {
        test.add(new Label("1CPI"));
        test.add(new Label("2CPI"));
        test.add(new Label("1CS"));
        test.add(new Label("2CS-SIT"));
        test.add(new Label("3CS-SIL"));

        levelContainer.setSpacing(10);

        for(Label label : test) {
            HBox box = new HBox();
            box.setAlignment(Pos.CENTER_LEFT);

            JFXTextField textField = new JFXTextField();
            textField.setPrefWidth(700);

            label.setPrefWidth(100);

            JFXButton button = new JFXButton("B");
            buttons.add(button);

            box.getChildren().add(label);
            box.getChildren().add(textField);
            box.getChildren().add(button);

            levelContainer.getChildren().add(box);
        }
    }

    @Override
    public void setMainApp(MainApp mainApp) {

    }

    @Override
    public void cancel() {

    }
}
