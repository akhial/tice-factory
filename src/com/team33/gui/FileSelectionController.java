package com.team33.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.team33.model.assertions.InternshipFormatChecker;
import com.team33.model.assertions.MissingFieldsException;
import com.team33.model.assertions.NoLineFoundException;
import com.team33.model.assertions.SchoolServiceFormatChecker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class FileSelectionController implements Controller {

    private MainApp mainApp;
    private ObservableList<Label> levels = FXCollections.observableArrayList();

    @FXML
    private VBox levelContainer;

    void setup() {

        levels = mainApp.getHelper().getLevels();

        levelContainer.setSpacing(10);

        for(Label label : levels) {
            HBox box = new HBox();
            box.setAlignment(Pos.CENTER_LEFT);

            JFXTextField textField = new JFXTextField();
            textField.setPrefWidth(1200);

            label.setPrefWidth(70);
            label.setMinWidth(70);

            JFXButton button = new JFXButton("");
            button.setId("file-button");

            button.setOnAction(e -> {
                String s;
                if(label.getText().startsWith("3CS")) {
                    s = chooseWordFile();
                    textField.setText(s);
                    InternshipFormatChecker internshipFormatChecker = new InternshipFormatChecker();
                    try {
                        internshipFormatChecker.checkFormat(s);
                        mainApp.getHelper().getLevelPaths().put(label.getText(), s);
                    } catch(IOException e1) {
                        mainApp.getMainViewController().showConfirmationDialog("Erreur",
                                "Erreur pendant l'ouverture du ficher!");
                    } catch(MissingFieldsException | NoLineFoundException e1) {
                        mainApp.getMainViewController().showConfirmationDialog("Erreur",
                                e1.getMessage());
                    }
                } else {
                    s = chooseFile();
                    textField.setText(s);
                    SchoolServiceFormatChecker schoolServiceFormatChecker = new SchoolServiceFormatChecker();
                    try {
                        schoolServiceFormatChecker.checkFormat(s);
                        System.out.println(label.getText() + " " + s);
                        mainApp.getHelper().getLevelPaths().put(label.getText(), s);
                    } catch(IOException e1) {
                        mainApp.getMainViewController().showConfirmationDialog("Erreur",
                                "Erreur pendant l'ouverture du fichier!");
                    } catch(MissingFieldsException | NoLineFoundException e1) {
                        mainApp.getMainViewController().showConfirmationDialog("Erreur",
                                e1.getMessage());
                    }
                }
            });

            box.getChildren().add(label);
            box.getChildren().add(textField);
            box.getChildren().add(button);

            levelContainer.getChildren().add(box);
        }
    }

    private String chooseFile() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers Excel", "*.xlsx"));
        chooser.setTitle("Séléctionner le fichier scolarité...");
        File result = chooser.showOpenDialog(null);
        if(result != null) {
            RecentFileHandler.writeFile(result.getAbsolutePath());
            return result.getAbsolutePath();
        }
        else
            mainApp.getMainViewController().showConfirmationDialog("Alerte",
                    "Veuillez choisir un fichier!");
        return "";
    }

    private String chooseWordFile() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers Word", "*.docx"));
        chooser.setTitle("Séléctionner le fichier scolarité...");
        File result = chooser.showOpenDialog(null);
        if(result != null) {
            RecentFileHandler.writeFile(result.getAbsolutePath());
            return result.getAbsolutePath();
        }
        else
            mainApp.getMainViewController().showConfirmationDialog("Alerte",
                    "Veuillez choisir un fichier!");
        return "";
    }

    @FXML
    void onNextButton() {
        if(mainApp.getHelper().getLevelPaths().size() >= levels.size()) {
            mainApp.getMainViewController().setScene(MainApp.SAVE_SELECT, MainApp.CONVERT_NAME);
        }
        else
            mainApp.getMainViewController().showConfirmationDialog("Alerte",
                    "Veuillez choisir un fichier pour tout les niveaux!");
    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @Override
    public void cancel() {

    }
}
