package com.team33.gui;

import javafx.fxml.FXML;

public class TypeSelectionController implements Controller {

    private MainApp mainApp;

    @FXML
    private void onStudentListButton() {
        mainApp.setAggregateHelper(new AggregateHelper("list"));
        mainApp.getMainViewController().setScene(MainApp.STUDENT_SELECT, MainApp.CONVERT_NAME);
        ((StudentSelectionController) mainApp.getCurrentController()).setup();
    }

    @FXML
    private void onStudentGroupButton() {
        mainApp.setAggregateHelper(new AggregateHelper("group"));
        mainApp.getMainViewController().setScene(MainApp.STUDENT_SELECT, MainApp.CONVERT_NAME);
        ((StudentSelectionController) mainApp.getCurrentController()).setup();
    }

    @FXML
    private void onStudentGradesButton() {
        mainApp.setAggregateHelper(new AggregateHelper("grades"));
        mainApp.getMainViewController().setScene(MainApp.STUDENT_SELECT, MainApp.CONVERT_NAME);
        ((StudentSelectionController) mainApp.getCurrentController()).setup();
    }

    @FXML
    private void onStudentLessonsButton() {
        mainApp.setAggregateHelper(new AggregateHelper("courses"));
        mainApp.getMainViewController().setScene(MainApp.STUDENT_SELECT, MainApp.CONVERT_NAME);
        ((StudentSelectionController) mainApp.getCurrentController()).setup();
    }

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
