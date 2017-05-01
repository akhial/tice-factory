package com.team33.gui;

import com.jfoenix.controls.JFXListView;
import com.team33.model.csv.students.courses.CoursesStore;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;

import java.util.ArrayList;
import java.util.Arrays;

public class StudentSelectionController implements Controller {

    private MainApp mainApp;

    private ObservableList<Label> selectedItems;

    @FXML
    private JFXListView<Label> levelList;

    public void setup() {
        CoursesStore store = new CoursesStore();
        store.load();
        ArrayList<String> options = store.getCycleSuperieur().getCS2().getOptions();
        String[] levels = {"1CPI", "2CPI", "1CS", "2CS", "3CS"};
        if(mainApp.getHelper().getButtonType().equals("grades") || mainApp.getHelper().getButtonType().equals("group")) {
            levels = Arrays.copyOfRange(levels, 0, 4);
        }
        for(int i = 0; i < 3; i++) {
            levelList.getItems().add(new Label(levels[i]));
        }
        for(int i = 3; i < levels.length; i++) {
            for(String option : options) {
                levelList.getItems().add(new Label(levels[i] + "-" + option));
            }
        }

        levelList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    private void onNextButtonClick() {
        selectedItems =  levelList.getSelectionModel().getSelectedItems();
        if(!selectedItems.isEmpty()) {
            mainApp.getHelper().setLevels(selectedItems);
            mainApp.getMainViewController().setScene(MainApp.WEB_SELECT, MainApp.CONVERT_NAME);
        } else
            mainApp.getMainViewController().showConfirmationDialog("Alerte",
                    "Veuillez sélectionner au moins un fichier!");
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @Override
    public void cancel() {

    }
}
