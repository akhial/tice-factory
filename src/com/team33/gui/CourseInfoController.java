package com.team33.gui;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;

public class CourseInfoController implements Controller {

    private MainApp mainApp;
    private CourseEditController courseEditController;
    private int semester;
    private TreeItem<String> root;
    private JFXDialog dialog;

    @FXML
    private JFXTextField fullField;

    @FXML
    private JFXTextField shortField;

    @FXML
    private void onConfirmButton() {
        courseEditController.addCourse(root, semester, fullField.getText(), shortField.getText());
        dialog.close();
    }

    @FXML
    private void onCancelButton() {
        dialog.close();
    }

    void setCourseEditController(CourseEditController courseEditController) {
        this.courseEditController = courseEditController;
    }

    void setSemester(int semester) {
        this.semester = semester;
    }

    void setRoot(TreeItem<String> root) {
        this.root = root;
    }

    public void setDialog(JFXDialog dialog) {
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
