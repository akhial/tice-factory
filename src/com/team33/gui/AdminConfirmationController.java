package com.team33.gui;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.team33.model.accounts.AuthenticationType;
import com.team33.model.accounts.User;
import com.team33.model.accounts.UserWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AdminConfirmationController implements Controller {

    private MainApp mainApp;
    private UserWrapper userWrapper;
    private UserLoginController userLoginController;

    private String userName;
    private String password;

    @FXML
    private JFXTextField userField;

    @FXML
    private JFXPasswordField passField;

    @FXML
    private void onConfirmButton() {
        if(userField.getText().isEmpty()) {
            showErrorDialog("Veuillez entrer un nom d'utilisateur!");
            return;
        } else if(passField.getText().isEmpty()) {
            showErrorDialog("Veuillez entrer un mot de passe!");
            return;
        }
        if(userWrapper.checkAuthentication(userField.getText(), passField.getText())) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get("users.dat")));

                User user = new User(userField.getText(), passField.getText(), AuthenticationType.ADMIN);
                userWrapper.addUser(user);
                oos.writeObject(userWrapper);
                oos.close();

                userLoginController.hideAll();
                userLoginController.showSuccessDialog();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showErrorDialog(String s) {
        JFXDialog dialog = new JFXDialog();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/ConfirmationDialogBox.fxml"));
        Region region = null;
        try {
            region = loader.load();
        } catch(IOException e) {
            e.printStackTrace();
        }
        ((ConfirmationDialogBoxController) loader.getController()).setDialog("Alerte",
                s);
        ((ConfirmationDialogBoxController) loader.getController()).setDialog(dialog);
        dialog.setContent(region);
        dialog.show(userLoginController.getRootStackPane());
    }

    public void setUserLoginController(UserLoginController userLoginController) {
        this.userLoginController = userLoginController;
    }

    public void setUserWrapper(UserWrapper userWrapper) {
        this.userWrapper = userWrapper;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void cancel() {

    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
