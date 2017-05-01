package com.team33.gui;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;

public class OptionInfoController implements Controller {

    private MainApp mainApp;
    private CourseEditController courseEditController;
    private TreeItem<String> root;
    private JFXDialog dialog;

    @FXML
    private JFXTextField optionField;

    @FXML
    private void onConfirmButton() {
        courseEditController.addOption(root, optionField.getText());
        dialog.close();
    }

    @FXML
    private void onCancelButton() {
        dialog.close();
    }

    void setCourseEditController(CourseEditController courseEditController) {
        this.courseEditController = courseEditController;
    }

    void setDialog(JFXDialog dialog) {
        this.dialog = dialog;
    }

    public void setRoot(TreeItem<String> root) {
        this.root = root;
    }

    @Override
    public void cancel() {

    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
