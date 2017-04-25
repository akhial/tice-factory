package com.team33.gui;

import com.jfoenix.controls.JFXTextField;
import com.team33.model.assertions.MissingFieldsException;
import com.team33.model.assertions.WebServiceFormatChecker;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class WebSelectionController implements Controller {

    private MainApp mainApp;
    private String filePath;
    private boolean fileChosen = false;

    @FXML
    private JFXTextField webTextField;

    @FXML
    private void onOpenChooserButtonClick() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers Excel", "*.xlsx"));
        chooser.setTitle("Séléctionner le fichier web...");
        File result = chooser.showOpenDialog(null);
        if(result != null) {
            RecentFileHandler.writeFile(result.getAbsolutePath());
            System.out.println("Writing file " + result.getAbsolutePath());
            WebServiceFormatChecker webServiceFormatChecker = new WebServiceFormatChecker();
            try {
                webTextField.setText(result.getAbsolutePath());
                webServiceFormatChecker.checkFormat(result.getAbsolutePath());
                fileChosen = true;
            } catch(IOException e) {
                mainApp.getMainViewController().showConfirmationDialog("Erreur",
                        "Erreur pendant la lecture du fichier!");
            } catch(MissingFieldsException e) {
                mainApp.getMainViewController().showConfirmationDialog("Erreur",
                        e.getMessage());
            }
        }
    }

    @FXML
    private void onNextButtonClick() {
        if(fileChosen) {
            filePath = webTextField.getText();
            mainApp.getHelper().setWebPath(filePath);
            mainApp.getMainViewController().setScene(MainApp.FILE_SELECT, MainApp.CONVERT_NAME);
            mainApp.setup();
        } else {
            mainApp.getMainViewController().showConfirmationDialog("Erreur", "Veuillez choisir un fichier!");
        }
    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @Override
    public void cancel() {

    }
}
