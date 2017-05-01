package com.team33.gui;

import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class SaveSelectionController implements Controller {

    private MainApp mainApp;

    @FXML
    private JFXRadioButton singleButton;

    @FXML
    private JFXRadioButton multiButton;

    @FXML
    private JFXTextField fileLabel;

    @FXML
    private void initialize() {
        ToggleGroup group = new ToggleGroup();
        singleButton.setToggleGroup(group);
        multiButton.setToggleGroup(group);
        singleButton.setSelected(true);
    }

    @FXML
    private void onFileSelectButton() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Veuillez choisir un dossier...");
        File file = directoryChooser.showDialog(null);
        fileLabel.setText(file.getAbsolutePath());
    }

    @FXML
    private void onNextButton() {
        mainApp.getHelper().setOutPath(fileLabel.getText());
        mainApp.getHelper().makeLevelCSV();
    }

    @Override
    public void cancel() {

    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
