package com.team33.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.HashMap;

public class FileSelectionController implements Controller {

    private MainApp mainApp;
    private ObservableList<Label> test = FXCollections.observableArrayList();
    private HashMap<Label, JFXButton> buttons = new HashMap<>();

    @FXML
    private VBox levelContainer;

    void setup() {

        test = mainApp.getHelper().getLevels();
        String type = mainApp.getHelper().getButtonType();
        // TODO use real data

        levelContainer.setSpacing(10);

        for(Label label : test) {
            if(label.getText().startsWith("3CS") && (type.equals("grades") || type.equals("group"))) {
            } else {
                HBox box = new HBox();
                box.setAlignment(Pos.CENTER_LEFT);

                JFXTextField textField = new JFXTextField();
                textField.setPrefWidth(500);

                label.setPrefWidth(100);

                JFXButton button = new JFXButton("");
                button.setId("file-button");

                button.setOnAction(e -> {
                    String s = chooseFile();
                    mainApp.getHelper().getLevelPaths().put(label.getText(), s);
                    label.setText(s);
                });

                box.getChildren().add(label);
                box.getChildren().add(textField);
                box.getChildren().add(button);

                levelContainer.getChildren().add(box);
            }
        }
        // TODO return results
    }

    // TODO 3CS choose word file .docx
    private String chooseFile() {
        FileChooser chooser = new FileChooser();
        chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Fichiers Excel", ".xlsx"));
        chooser.setTitle("Séléctionner le fichier scolarité...");
        File result = chooser.showOpenDialog(null);
        if(result != null)
            return result.getAbsolutePath();
        return "";
    }

    @FXML
    void onNextButton() {
        mainApp.getMainViewController().setScene(MainApp.SAVE_SELECT, MainApp.CONVERT_NAME);
    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @Override
    public void cancel() {

    }
}
