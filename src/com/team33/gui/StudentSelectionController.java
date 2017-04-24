package com.team33.gui;

import com.jfoenix.controls.JFXListView;
import com.team33.model.csv.Students.Courses.CoursesStore;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;

import java.io.IOException;
import java.util.ArrayList;

public class StudentSelectionController implements Controller {

    private MainApp mainApp;

    private ObservableList<Label> selectedItems;

    @FXML
    private JFXListView<Label> levelList;

    @FXML
    @SuppressWarnings("unused")
    private void initialize() {
        CoursesStore store = new CoursesStore();
        try {
            store.load();
        } catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
            //TODO show dialog
        }
        ArrayList<String> options = store.getCycleSuperieur().getCS2().getOptions();
        String[] levels = {"1CPI", "2CPI", "1CS", "2CS", "3CS"};

        // TODO 3CS has no groups
        // TODO when user doesn't select anything show dialog box
        for(int i = 0; i < 3; i++) {
            levelList.getItems().add(new Label(levels[i]));
        }
        for(int i = 3; i < 5; i++) {
            for(String option : options) {
                levelList.getItems().add(new Label(levels[i] + "-" +     option));
            }
        }

        levelList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    private void onNextButtonClick() {
       selectedItems =  levelList.getSelectionModel().getSelectedItems();
       mainApp.getHelper().setLevels(selectedItems);
       mainApp.getMainViewController().setScene(MainApp.WEB_SELECT, MainApp.CONVERT_NAME);
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @Override
    public void cancel() {

    }
}
