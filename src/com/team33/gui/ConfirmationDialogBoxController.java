package com.team33.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class ConfirmationDialogBoxController {

    @FXML
    private Label title;

    @FXML
    private Text content;

    @FXML
    private JFXButton confirmButton;

    private JFXDialog dialog;

    @FXML
    private void initialize() {
        confirmButton.setOnAction(e -> {
            dialog.close();
        });
    }

    void setDialog(JFXDialog dialog) {
        this.dialog = dialog;
    }

    void setDialog(String title, String content) {
        this.title.setText(title);
        this.content.setText(content);
    }
}
