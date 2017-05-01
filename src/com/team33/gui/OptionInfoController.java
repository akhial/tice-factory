package com.team33.gui;

import com.jfoenix.controls.JFXDialog;
import javafx.fxml.FXML;

public class OptionInfoController implements Controller {

    private MainApp mainApp;
    private CourseEditController courseEditController;
    private JFXDialog dialog;

    @FXML
    private void onConfirmButton() {
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

    @Override
    public void cancel() {

    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
