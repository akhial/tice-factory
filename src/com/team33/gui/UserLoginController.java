package com.team33.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.team33.model.accounts.AuthenticationType;
import javafx.fxml.FXML;

public class UserLoginController implements Controller {

    private MainApp mainApp;

    @FXML
    private JFXTextField userField;

    @FXML
    private JFXPasswordField passField;

    @FXML
    private JFXButton connectButton;

    @FXML
    private JFXButton signupButton;

    @FXML
    private void initialize() {

    }

    @FXML
    private void onConnectButton() {
        String username = userField.getText();
        String password = passField.getText();

        if(username.equals("root") && password.equals("sudo")) {
            mainApp.login("root", AuthenticationType.ADMIN);
        }
        // TODO authenticate here
    }

    @FXML
    private void onSignupButton() {

    }

    @Override
    public void cancel() {

    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
