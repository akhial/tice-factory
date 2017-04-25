package com.team33.gui;

import com.jfoenix.controls.JFXTextField;
import com.team33.model.assertions.*;
import com.team33.model.csv.AssigningTeacherToCourseFormat;
import com.team33.model.csv.CSVBuilder;
import com.team33.model.csv.CSVFormat;
import com.team33.model.csv.TeacherFormat;
import javafx.fxml.FXML;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class TeacherSelectionController implements Controller {

    private MainApp mainApp;
    private boolean withAssignment = false;
    private boolean chargesSelected = false;
    private boolean emailSelected = false;
    private boolean cancel = false;

    @FXML
    private JFXTextField webTextField;

    @FXML
    private JFXTextField mailTextField;

    @FXML
    private void onOpenChargesButton() {
        FileChooser chooser = new FileChooser();
        chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Fichiers Excel", ".xlsx"));
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
        chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Fichiers Excel", ".xlsx"));
        chooser.setTitle("Séléctionner le fichier des emails...");
        File result = chooser.showOpenDialog(null);
        if(result != null) {
            mailTextField.setText(result.getAbsolutePath());
            emailSelected = true;
        }
    }

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

                    CSVFormat format = withAssignment ? new AssigningTeacherToCourseFormat() : new TeacherFormat();
                    CSVBuilder csvBuilder = new CSVBuilder(workbookPaths, format, save.getAbsolutePath());
                    try {
                        csvBuilder.buildCSV();
                    } catch(IOException e) {
                        mainApp.getMainViewController().showConfirmationDialog("Erreur",
                                "Une erreur c'est produite pendant l'ecriture dans le fichier " + csvBuilder.getTempPath());
                    }

                    HashMap<String, ArrayList<String>> mails;
                    if(withAssignment) {
                        mails = ((AssigningTeacherToCourseFormat) format).getUnHandledEmails();
                    } else {
                        mails = ((TeacherFormat) format).getUnHandledEmails();
                    }
                    mainApp.getMainViewController().setScene(MainApp.TEACHER_EXCEPTION, MainApp.CONVERT_NAME);
                    ((TeacherExceptionController) mainApp.getCurrentController()).setUnhandledEmails(mails);
                    ((TeacherExceptionController) mainApp.getCurrentController()).setTeacherSelectionController(this);
                    HashMap<String, String> finalEmails = null;

                    try {
                        FXUtilities.runAndWait(() -> {

                        });
                    } catch(InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    try {
                        if(withAssignment) {
                            ((AssigningTeacherToCourseFormat) format).AddingMissingEmails(finalEmails);
                        } else {
                            ((TeacherFormat) format).AddingMissingEmails(finalEmails);
                        }
                    } catch(IOException e) {
                        // TODO show dialog
                    }
                    csvBuilder.convertToCSV();
                }
            }
        }
    }

    @FXML
    private void onCancelButton() {

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
