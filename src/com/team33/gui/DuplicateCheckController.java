package com.team33.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.team33.model.assertions.*;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class DuplicateCheckController implements Controller {

    private MainApp mainApp;
    private boolean fileChosen = false;
    private int result = 0;
    private boolean finished = false;
    private boolean found = false;

    @FXML
    private JFXComboBox<String> typeBox;

    @FXML
    private JFXButton fileButton;

    @FXML
    private JFXTextField fileField;

    @FXML
    private void initialize() {
        typeBox.getItems().add("Emails des enseignants");
        typeBox.getItems().add("Charges enseignants");
        typeBox.getItems().add("Service scolarité");
        typeBox.getItems().add("Email des étudiants");

        fileButton.setText("");
    }

    @FXML
    private void onCheckButton() {
        if(fileChosen) {
            if(!(typeBox.getSelectionModel().getSelectedIndex() == -1)) {
                String fileName = fileField.getText();
                ExcelFormat formatChecker = new TeacherEmailFormatChecker();
                switch(typeBox.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        break;
                    case 1:
                        formatChecker = new TeacherFormatChecker();
                        break;
                    case 2:
                        formatChecker = new SchoolServiceFormatChecker();
                        break;
                    case 3:
                        formatChecker = new WebServiceFormatChecker();
                        break;
                }

                try {
                    formatChecker.checkFormat(fileName);
                } catch(IOException e) {
                    e.printStackTrace();
                    mainApp.getMainViewController().showConfirmationDialog("Erreur",
                            "Fichier introuvable!");
                    return;
                } catch(Exception e) {
                    mainApp.getMainViewController().showConfirmationDialog("Erreur",
                            e.getMessage());
                    return;
                }
                int[] locations = new int[4];
                while(!finished) {
                    try {
                        while(!formatChecker.checkDoublants(fileName, locations)) ;
                        if(!found)
                            mainApp.getMainViewController().showConfirmationDialog("Succés",
                                    "Aucun doublon trouvé!");
                        finished = true;
                        break;
                    } catch(Exception e) {
                        found = true;
                        mainApp.getMainViewController().showLongConfirmationDialog("Trouvé",
                                e.getMessage(), this);
                        if(result == 1) {
                            try {
                                formatChecker.DeleteDuplicate(fileName, locations[0], locations[2]);
                            } catch(Exception e1) {
                                e1.printStackTrace();
                            }
                        } else if(result == 2) {
                            try {
                                formatChecker.DeleteDuplicate(fileName, locations[1], locations[3]);
                            } catch(Exception e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                }
                if(found)
                    mainApp.getMainViewController().showConfirmationDialog("Succés",
                            "Operation terminée!");
                found = false;
                finished = false;
            } else {
                mainApp.getMainViewController().showConfirmationDialog("Erreur", "Veuillez choisir le type du fichier!");
            }
        } else if(!fileChosen) {
            mainApp.getMainViewController().showConfirmationDialog("Erreur", "Veuillez choisir un fichier!");
        }
    }

    @FXML
    private void onFileButton() {
        FileChooser chooser = new FileChooser();
        chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Fichiers Excel", ".xlsx"));
        chooser.setTitle("Séléctionner un fichier...");
        File result = chooser.showOpenDialog(null);
        if(result != null) {
            fileField.setText(result.getAbsolutePath());
            fileChosen = true;
        }
    }

    public void setResult(int result) {
        this.result = result;
    }

    @Override
    public void cancel() {

    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
