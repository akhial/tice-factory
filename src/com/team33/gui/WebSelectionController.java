package com.team33.gui;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

import java.io.File;

public class WebSelectionController implements Controller {

    private String filePath;

    @FXML
    private JFXTextField webTextField;

    @FXML
    private void onOpenChooserButtonClick() {
        FileChooser chooser = new FileChooser();
        chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Fichiers Excel", ".xlsx"));
        chooser.setTitle("Séléctionner le fichier web...");
        File result = chooser.showOpenDialog(null);
        if(result != null)
            webTextField.setText(result.getAbsolutePath());
        //else
            //TODO show dialog
    }

    @FXML
    private void onNextButtonClick() {
        filePath = webTextField.getText();
    }

    @Override
    public void setMainApp(MainApp mainApp) {

    }

    @Override
    public void cancel() {

    }
}
