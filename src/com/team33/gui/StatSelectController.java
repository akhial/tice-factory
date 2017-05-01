package com.team33.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.team33.model.statistics.StatisticsGenerator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDate;
import java.util.TreeSet;

public class StatSelectController implements Controller {

    private MainApp mainApp;

    @FXML
    private JFXDatePicker start;

    @FXML
    private JFXDatePicker end;

    @FXML
    private JFXButton applyButton;

    @FXML
    private JFXComboBox<String> criteria;

    @FXML
    private Label criteriaLabel;

    @FXML
    private JFXTextField fileField;

    @FXML
    private void initialize() {
        applyButton.setDisable(true);
        criteria.setDisable(true);
        criteriaLabel.setDisable(true);
        fileField.setEditable(false);

        start.setOnAction(e -> checkConditions());
        end.setOnAction(e -> checkConditions());
    }

    @FXML
    private void onFileSelect() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers excel", "*.xlsx"));
        File result = fileChooser.showOpenDialog(null);
        if(result != null) {
            fileField.setText(result.getAbsolutePath());
            checkConditions();
        }
    }

    private void checkConditions() {
        if(end.getValue() != null && start.getValue() != null && !fileField.getText().isEmpty()) {
            criteria.setDisable(false);
            applyButton.setDisable(false);
            start.setDisable(true);
            end.setDisable(true);
            fileField.setDisable(true);
        }
    }

    @FXML
    private void onApplyButton() {
        LocalDate dateStart = start.getValue();
        LocalDate dateEnd = end.getValue();
        String startDate = dateStart.toString();
        String endDate = dateEnd.toString();

        try {
            TreeSet<String> propositions = StatisticsGenerator.getPropositions(fileField.getText());
            criteria.setDisable(false);
            criteriaLabel.setDisable(false);
            criteria.getItems().addAll(propositions);
        } catch(Exception e) {
            mainApp.getMainViewController().showConfirmationDialog("Erreur",
                    e.getMessage());
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
