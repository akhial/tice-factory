package com.team33.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.team33.model.statistics.StatisticsGenerator;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDate;
import java.util.TreeSet;
import java.util.concurrent.FutureTask;

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

            RecentFileHandler.writeFile(fileField.getText());

            LocalDate dateStart = start.getValue();
            LocalDate dateEnd = end.getValue();
            StringBuilder builder = new StringBuilder();

            builder.append(dateStart.getDayOfMonth())
            .append("/")
            .append(dateStart.getMonthValue())
            .append("/")
            .append(dateStart.getYear());

            String startDate = builder.toString();
            builder = new StringBuilder();

            builder.append(dateEnd.getDayOfMonth())
                    .append("/")
                    .append(dateEnd.getMonthValue())
                    .append("/")
                    .append(dateEnd.getYear());

            String endDate = builder.toString();
            start.setDisable(true);
            end.setDisable(true);
            fileField.setDisable(true);

            try {

                mainApp.getMainViewController().showLoadingDialog();

                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        StatisticsGenerator.trierFichierExcel(fileField.getText(), "temp.xlsx", 5);
                        RecentFileHandler.writeFile(fileField.getText());
                        StatisticsGenerator.selectDates(startDate, endDate, "temp.xlsx", "temp.xlsx");
                        StatisticsGenerator.semiGeneralStats("temp.xlsx", "temp.xlsx", 1);
                        TreeSet<String> propositions = StatisticsGenerator.getPropositions("temp.xlsx");

                        criteria.setDisable(false);
                        criteriaLabel.setDisable(false);
                        applyButton.setDisable(false);
                        criteria.getItems().addAll(propositions);
                        mainApp.getMainViewController().hideLoadingDialog();
                        return null;
                    }
                };

                Thread thread = new Thread(task);
                thread.setDaemon(true);
                thread.start();

            } catch(Exception e) {
                mainApp.getMainViewController().showConfirmationDialog("Erreur",
                        "Une erreur c'est produite...");
                criteria.setDisable(true);
                applyButton.setDisable(true);
                start.setDisable(false);
                end.setDisable(false);
                fileField.setDisable(false);
            }
        }
    }

    @FXML
    private void onApplyButton() {
        if(criteria.getSelectionModel().getSelectedItem() != null) {
            mainApp.getMainViewController().setScene(MainApp.BAR_CHART, MainApp.STAT_NAME);
            ((BarChartController) mainApp.getCurrentController()).setCriteria(criteria.getSelectionModel().getSelectedItem());
            ((BarChartController) mainApp.getCurrentController()).setup();
        } else {
            mainApp.getMainViewController().showConfirmationDialog("Alerte",
                    "Veuillez séléctionner un critère!");
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
