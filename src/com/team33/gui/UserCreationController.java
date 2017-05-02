package com.team33.gui;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.team33.model.accounts.AuthenticationType;
import com.team33.model.accounts.User;
import com.team33.model.accounts.UserWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UserCreationController implements Controller {

    private MainApp mainApp;
    private UserLoginController userLoginController;

    @FXML
    private JFXTextField userField;

    @FXML
    private JFXPasswordField passField;

    @FXML
    private JFXPasswordField confirmField;

    @FXML
    private JFXRadioButton adminButton;

    @FXML
    private JFXRadioButton userButton;

    @FXML
    private void initialize() {
        ToggleGroup group = new ToggleGroup();
        adminButton.setToggleGroup(group);
        userButton.setToggleGroup(group);
        userButton.setSelected(true);
    }

    @FXML
    private void onConfirmButton() {
        if(userField.getText().isEmpty()) {
            showErrorDialog("Veuillez entrer un nom d'utilisateur!");
            return;
        } else if(passField.getText().isEmpty()) {
            showErrorDialog("Veuillez entrer un mot de passe!");
            return;
        } else if(confirmField.getText().isEmpty()) {
            showErrorDialog("Veuillez confirmer votre mot de passe!");
            return;
        }
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(Files.newInputStream(Paths.get("users.dat")));
            UserWrapper userWrapper = (UserWrapper) ois.readObject();

            if(!(passField.getText().equals(confirmField.getText()))) {
                showErrorDialog("Les deux mots de passe ne correspondent pas!");
                return;
            }
            if(userButton.isSelected()) {
                if(!(userWrapper.userExists(userField.getText()))) {
                    User user = new User(userField.getText(), passField.getText(), AuthenticationType.USER);
                    userWrapper.addUser(user);
                    ois.close();
                    ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get("users.dat")));
                    oos.writeObject(userWrapper);
                    oos.close();

                    userLoginController.hideAll();
                    userLoginController.showSuccessDialog();
                } else {
                    showErrorDialog("Le nom d'utilisateur existe!");
                }
            } else {
                if(!(userWrapper.userExists(userField.getText()))) {
                    showAdminDialog(userWrapper);
                } else {
                    showErrorDialog("Le nom d'utilisateur existe!");
                }
            }
        } catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
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

    private void showAdminDialog(UserWrapper userWrapper) {
        JFXDialog dialog = new JFXDialog();
        userLoginController.addDialog(dialog);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/AdminConfirmationView.fxml"));
        Region region = null;
        try {
            region = loader.load();
        } catch(IOException e) {
            e.printStackTrace();
        }
        ((AdminConfirmationController) loader.getController()).setUserWrapper(userWrapper);
        ((AdminConfirmationController) loader.getController()).setUserLoginController(userLoginController);
        ((AdminConfirmationController) loader.getController()).setUserName(userField.getText());
        ((AdminConfirmationController) loader.getController()).setPassword(passField.getText());
        dialog.setContent(region);
        dialog.show(userLoginController.getRootStackPane());
    }

    void setUserLoginController(UserLoginController userLoginController) {
        this.userLoginController = userLoginController;
    }

    @Override
    public void cancel() {

    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
