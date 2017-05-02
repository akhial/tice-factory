package com.team33.gui;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

public class LoginViewController implements Controller {

    private MainApp mainApp;

    @FXML
    private JFXTextField userField;

    @FXML
    private JFXTextField apiField;

    @FXML
    private JFXTextField secretField;

    @FXML
    private void checkConnection() {
        String username = userField.getText();
        if(username.isEmpty()) {
            mainApp.getMainViewController().showConfirmationDialog("Erreur",
                    "Le champ expéditeur ne doit pas être vide!");
            return;
        }
        String apiKey = apiField.getText();
        if(apiKey.isEmpty()) {
            mainApp.getMainViewController().showConfirmationDialog("Erreur",
                    "Le champ API-Key ne doit pas être vide!");
            return;
        }
        String secretKey = secretField.getText();
        if(secretKey.isEmpty()) {
            mainApp.getMainViewController().showConfirmationDialog("Erreur",
                    "Le champ Secret-Key ne doit pas être vide!");
            return;
        }
        mainApp.getMainViewController().hideLoadingDialog();

        ((MailSelectController) mainApp.getCurrentController()).setUsername(username);
        ((MailSelectController) mainApp.getCurrentController()).setApiKey(apiKey);
        ((MailSelectController) mainApp.getCurrentController()).setSecretKey(secretKey);

        ((MailSelectController) mainApp.getCurrentController()).continueSend();
    }

    @Override
    public void cancel() {

    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
