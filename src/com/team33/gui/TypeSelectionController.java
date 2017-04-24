package com.team33.gui;

import javafx.fxml.FXML;

public class TypeSelectionController implements Controller {

    private MainApp mainApp;

    @FXML
    private void onStudentListButton() {
        mainApp.setAggregateHelper(new AggregateHelper("list"));
        mainApp.getMainViewController().setScene(MainApp.STUDENT_SELECT, MainApp.CONVERT_NAME);
    }

    @Override
    public void cancel() {

    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
