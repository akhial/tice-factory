package com.team33.gui;

import com.jfoenix.controls.JFXDialog;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LongDialogController implements Controller {

    private MainApp mainApp;
    private DuplicateCheckController duplicateCheckController;
    private JFXDialog dialog;
    private Stage stage;

    @FXML
    private Label title;

    @FXML
    private Text content;

    @FXML
    private void onFirst() {
        duplicateCheckController.setResult(1);
        stage.close();
    }

    @FXML
    private void onSecond() {
        duplicateCheckController.setResult(2);
        stage.close();
    }

    void setDuplicateCheckController(DuplicateCheckController duplicateCheckController) {
        this.duplicateCheckController = duplicateCheckController;
    }

    @Override
    public void cancel() {

    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

    void setDialog(String title, String content) {
        this.title.setText(title);
        this.content.setText(content);
    }

    void setDialog(JFXDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
