package com.team33.gui;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.team33.model.Emails.EmailSender;
import javafx.fxml.FXML;

public class LoginViewController implements Controller {

    private MainApp mainApp;

    @FXML
    private JFXTextField userField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private void checkConnection() {
        String username = userField.getText();
        if(username.isEmpty()) {
            mainApp.getMainViewController().showConfirmationDialog("Erreur",
                    "Le champ utilisateur ne doit pas être vide!");
            return;
        }
        String password = passwordField.getText();
        if(password.isEmpty()) {
            mainApp.getMainViewController().showConfirmationDialog("Erreur",
                    "Le champ mot de passe ne doit pas être vide!");
            return;
        }
        try {
            EmailSender.testLogin(username, password);
        } catch(Exception e) {
            mainApp.getMainViewController().hideLoadingDialog();
            mainApp.getMainViewController().showConfirmationDialog("Erreur",
                    e.getMessage());
            return;
        }
        mainApp.getMainViewController().hideLoadingDialog();
        mainApp.getMainViewController().setScene(MainApp.MAIL_TYPE_SELECT, MainApp.MAIL_NAME);
        ((EmailTypeController) mainApp.getCurrentController()).setUsername(username);
        ((EmailTypeController) mainApp.getCurrentController()).setPassword(password);
    }

    @Override
    public void cancel() {

    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
