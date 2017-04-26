package com.team33.gui;

import com.jfoenix.controls.JFXRadioButton;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;

public class EmailTypeController implements Controller {

    private MainApp mainApp;

    @FXML
    private JFXRadioButton simple;

    @FXML
    private JFXRadioButton unique;

    private String username;
    private String password;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @FXML
    private void initialize() {
        ToggleGroup group = new ToggleGroup();
        simple.setToggleGroup(group);
        unique.setToggleGroup(group);
        simple.setSelected(true);
    }

    @FXML
    private void onNextButton() {
        mainApp.getMainViewController().setScene(MainApp.MAIL_SEND, MainApp.MAIL_NAME);
        ((MailSelectController) mainApp.getCurrentController()).setUsername(username);
        ((MailSelectController) mainApp.getCurrentController()).setPassword(password);
        if(simple.isSelected()) {
            ((MailSelectController) mainApp.getCurrentController()).setUnique(false);
        } else if(unique.isSelected()) {
            ((MailSelectController) mainApp.getCurrentController()).setUnique(true);
        }
    }

    @Override
    public void cancel() {

    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
