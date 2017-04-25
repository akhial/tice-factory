package com.team33.gui;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.team33.model.Emails.EmailSender;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;

public class MailSelectController implements Controller {

    private MainApp mainApp;

    @FXML
    private JFXTextField fileField;

    @FXML
    private JFXTextField subject;

    @FXML
    private JFXTextArea message;

    private boolean unique;
    private String username;
    private String password;

    @FXML
    private void onFileButton() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers Excel", "*.xlsx"));
        chooser.setTitle("Séléctionner le fichier scolarité...");
        File result = chooser.showOpenDialog(null);
        if(result != null)
            fileField.setText(result.getAbsolutePath());
        else
            mainApp.getMainViewController().showConfirmationDialog("Alerte",
                    "Veuillez choisir un fichier!");
    }

    @FXML
    private void onSendButton() {
        if(subject.getText().isEmpty()) {
            mainApp.getMainViewController().showConfirmationDialog("Erreur",
                    "Sujet ne doit pas être vide!");
        } else if(message.getText().isEmpty()) {
            mainApp.getMainViewController().showConfirmationDialog("Erreur",
                    "Message ne doit pas être vide!");
        } else {
            try {
                if(unique) {
                    EmailSender.SendEmails(username, password, subject.getText(), message.getText(), fileField.getText(), true);
                } else {
                    EmailSender.SendEmails(username, password, subject.getText(), message.getText(), fileField.getText());
                }
            } catch(IOException e) {
                mainApp.getMainViewController().showConfirmationDialog("Erreur",
                        "Erreur pendant lecture du fichier!");
            } catch(MessagingException e1) {
                mainApp.getMainViewController().showConfirmationDialog("Erreur de messagerie",
                        e1.getMessage());
            } catch(Exception e) {
                mainApp.getMainViewController().showConfirmationDialog("Erreur",
                        e.getMessage());
            }
        }
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void cancel() {

    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
