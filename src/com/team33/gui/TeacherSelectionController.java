package com.team33.gui;

import javafx.fxml.FXML;

public class TeacherSelectionController implements Controller {

    private MainApp mainApp;

    @FXML
    private void onOpenChargesButtonClick() {
        // TODO choose excel
    }

    @FXML
    private void onOpenEmailButtonClick() {
        // TODO same
    }

    @Override
    public void cancel() {

    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
