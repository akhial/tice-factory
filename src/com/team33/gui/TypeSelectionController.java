package com.team33.gui;

import javafx.fxml.FXML;

public class TypeSelectionController implements Controller {

    private MainApp mainApp;

    @FXML
    private void onStudentListButton() {
        mainApp.setAggregateHelper(new AggregateHelper("list"));
        mainApp.getMainViewController().setScene(MainApp.STUDENT_SELECT, MainApp.CONVERT_NAME);

        // TODO if type is group don't even show 3CS
    }

    // TODO add other types

    @FXML
    private void onTeacherListButton() {
        mainApp.getMainViewController().setScene(MainApp.TEACHER_SELECT, MainApp.CONVERT_NAME);
        ((TeacherSelectionController) mainApp.getCurrentController()).setWithAssignment(false);
    }

    @FXML
    private void onTeacherListAssignButton() {
        mainApp.getMainViewController().setScene(MainApp.TEACHER_SELECT, MainApp.CONVERT_NAME);
        ((TeacherSelectionController) mainApp.getCurrentController()).setWithAssignment(true);
    }

    @Override
    public void cancel() {

    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
