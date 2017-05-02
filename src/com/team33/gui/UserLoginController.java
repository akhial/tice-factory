package com.team33.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.team33.model.accounts.AuthenticationType;
import com.team33.model.accounts.UserWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

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
    private StackPane rootStackPane;

    @FXML
    private void initialize() {

    }

    @FXML
    private void onConnectButton() {
        String username = userField.getText();
        String password = passField.getText();

        try {
            ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get("users.dat")));
            UserWrapper userWrapper = (UserWrapper) ois.readObject();
            if(userWrapper.checkAuthentication(username, password)) {
                mainApp.login(username, userWrapper.getUser(username).getAuthenticationType());
            } else {
                JFXDialog dialog = new JFXDialog();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/fxml/ConfirmationDialogBox.fxml"));
                Region region = null;
                try {
                    region = loader.load();
                } catch(IOException e) {
                    e.printStackTrace();
                }
                ((ConfirmationDialogBoxController) loader.getController()).setDialog("Erreur",
                        "Nom d'utilisateur ou mot de passe incorrect!");
                ((ConfirmationDialogBoxController) loader.getController()).setDialog(dialog);
                dialog.setContent(region);
                dialog.show(rootStackPane);
            }
        } catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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
