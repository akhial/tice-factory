package com.team33.gui;

import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.team33.model.assertions.*;
import com.team33.model.csv.*;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TeacherSelectionController implements Controller {

    private MainApp mainApp;
    private boolean withAssignment = false;
    private boolean chargesSelected = false;
    private boolean emailSelected = false;
    private boolean cancel = false;

    private CSVFormat format;
    private CSVBuilder csvBuilder;

    private HashMap<String, String> finalMails = new HashMap<>();

    @FXML
    private JFXTextField webTextField;

    @FXML
    private JFXTextField mailTextField;

    @FXML
    private JFXRadioButton randomButton;

    @FXML
    private JFXRadioButton nameButton;

    @FXML
    private void initialize() {
        ToggleGroup group = new ToggleGroup();
        randomButton.setToggleGroup(group);
        nameButton.setToggleGroup(group);
        randomButton.setSelected(true);
    }

    @FXML
    private void onOpenChargesButton() {
        FileChooser chooser = new FileChooser();
        chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Fichiers Excel", "*.xlsx"));
        chooser.setTitle("Séléctionner le fichier des charges...");
        File result = chooser.showOpenDialog(null);
        if(result != null) {
            webTextField.setText(result.getAbsolutePath());
            chargesSelected = true;
        }
    }

    @FXML
    private void onOpenEmailButton() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers Excel", "*.xlsx"));
        chooser.setTitle("Séléctionner le fichier des emails...");
        File result = chooser.showOpenDialog(null);
        if(result != null) {
            mailTextField.setText(result.getAbsolutePath());
            emailSelected = true;
        }
    }

    // TODO add file selection view like Hamza, choose auto password or name

    @FXML
    private void onNextButton() {
        if(!emailSelected) mainApp.getMainViewController().showConfirmationDialog("Erreur",
                "Veuillez choisir le fichier des emails!");
        else if(!chargesSelected) mainApp.getMainViewController().showConfirmationDialog("Erreur",
                "Veuillez choisir le fichier des charges!");
        else {
            TeacherFormatChecker teacherFormatChecker = new TeacherFormatChecker();
            try {
                teacherFormatChecker.checkFormat(webTextField.getText());
            } catch(IOException e) {
                mainApp.getMainViewController().showConfirmationDialog("Erreur",
                        "Une erreur c'est produite lors de la lecture du fichier " + webTextField.getText());
                cancel = true;
            } catch(NoLineFoundException | MissingFieldsException e) {
                mainApp.getMainViewController().showConfirmationDialog("Erreur",
                        e.getMessage());
                cancel = true;
            }
            if(!cancel) {
                TeacherEmailFormatChecker teacherEmailFormatChecker = new TeacherEmailFormatChecker();
                try {
                    teacherEmailFormatChecker.checkFormat(mailTextField.getText());
                } catch(IOException e) {
                    mainApp.getMainViewController().showConfirmationDialog("Erreur",
                            "Une erreur c'est produite lors de la lecture du fichier " + mailTextField.getText());
                    cancel = true;
                } catch(MissingFieldsException e) {
                    mainApp.getMainViewController().showConfirmationDialog("Erreur",
                            e.getMessage());
                    cancel = true;
                }
                if(!cancel) {
                    ArrayList<String> workbookPaths = new ArrayList<>();
                    workbookPaths.add(webTextField.getText());
                    workbookPaths.add(mailTextField.getText());

                    DirectoryChooser directoryChooser = new DirectoryChooser();
                    File save = directoryChooser.showDialog(null);

                    format = withAssignment ? new AssigningTeacherToCourseFormat() : new TeacherFormat();
                    ((UserFormat) format).setGeneratedPassword(randomButton.isSelected());
                    csvBuilder = new CSVBuilder(workbookPaths, format, save.getAbsolutePath());
                    try {
                        csvBuilder.buildCSV();
                    } catch(IOException | InvalidFormatException e) {
                        mainApp.getMainViewController().showConfirmationDialog("Erreur",
                                "Une erreur c'est produite pendant l'ecriture dans le fichier " + csvBuilder.getTempPath());
                    }

                    HashMap<String, ArrayList<String>> mails;
                    if(withAssignment) {
                        mails = ((AssigningTeacherToCourseFormat) format).getUnHandledEmails();
                    } else {
                        mails = ((TeacherFormat) format).getUnHandledEmails();
                    }
                    if(!mails.isEmpty()) {
                        System.out.println(mails);
                        mainApp.getMainViewController().setScene(MainApp.TEACHER_EXCEPTION, MainApp.CONVERT_NAME);
                        ((TeacherExceptionController) mainApp.getCurrentController()).setUnhandledEmails(mails);
                        ((TeacherExceptionController) mainApp.getCurrentController()).setTeacherSelectionController(this);
                        ((TeacherExceptionController) mainApp.getCurrentController()).setup();
                    }
                }
            }
        }
        cancel = false;
    }

    @FXML
    private void onCancelButton() {

    }

    void setFinalMails(HashMap<String, String> finalMails) {
        this.finalMails = finalMails;
    }

    void finishCSV() {
        System.out.println("here");
        try {
            if(withAssignment) {
                ((AssigningTeacherToCourseFormat) format).AddingMissingEmails(finalMails);
            } else {
                ((TeacherFormat) format).AddingMissingEmails(finalMails);
            }
        } catch(IOException e) {
            mainApp.getMainViewController().showConfirmationDialog("Erreur",
                    "Erreur pendant l'écriture du fichier CSV");
        }
        csvBuilder.convertToCSV();
        mainApp.getMainViewController().showConfirmationDialog("Succés",
                "Fichier CSV créé aves succés!");
        mainApp.getMainViewController().setScene(MainApp.DASHBOARD, MainApp.DASHBOARD_NAME);
    }

    @Override
    public void cancel() {

    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setWithAssignment(boolean withAssignment) {
        this.withAssignment = withAssignment;
    }
}
