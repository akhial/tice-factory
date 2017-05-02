package com.team33.gui;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.team33.model.Emails.EmailSender;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;

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
    private String username = "";
    private String apiKey = "";
    private String secretKey = "";

    @FXML
    private void onFileButton() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers CSV", "*.csv"));
        chooser.setTitle("Séléctionner le fichier qui contient les mails...");
        File result = chooser.showOpenDialog(null);
        if(result != null) {
            fileField.setText(result.getAbsolutePath());
            RecentFileHandler.writeFile(result.getAbsolutePath());
        } else
            mainApp.getMainViewController().showConfirmationDialog("Alerte",
                    "Veuillez choisir un fichier!");
    }

    @FXML
    private void onSendButton() {
        if(fileField.getText().isEmpty()) {
            mainApp.getMainViewController().showConfirmationDialog("Erreur",
                    "Veuillez choisir un fichier!");
        }
        if(subject.getText().isEmpty()) {
            mainApp.getMainViewController().showConfirmationDialog("Erreur",
                    "Sujet ne doit pas être vide!");
        } else if(message.getText().isEmpty()) {
            mainApp.getMainViewController().showConfirmationDialog("Erreur",
                    "Message ne doit pas être vide!");
        } else {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource(MainApp.EMAIL_LOGIN));
                Region region = loader.load();

                ((LoginViewController) loader.getController()).setMainApp(mainApp);
                mainApp.getMainViewController().showDialog(region);
            } catch(IOException e) {
                // not gonna happen
            }
        }
    }

    void continueSend() {
        if(username.isEmpty() || apiKey.isEmpty() || secretKey.isEmpty()) {
            return;
        }
        try {
            if(unique) {
                EmailSender.SendEmails(username, apiKey, secretKey, subject.getText(), message.getText(), fileField.getText(), true);
            } else {
                EmailSender.SendEmails(username, apiKey, secretKey, subject.getText(), message.getText(), fileField.getText());
            }
            mainApp.getMainViewController().setScene(MainApp.DASHBOARD, MainApp.DASHBOARD_NAME);
            mainApp.getMainViewController().showConfirmationDialog("Succés",
                    "Le mail à été envoyé avec succés!");
        } catch(IOException e) {
            mainApp.getMainViewController().showConfirmationDialog("Erreur",
                    "Erreur pendant lecture du fichier!");
        } catch(Exception e) {
            mainApp.getMainViewController().showConfirmationDialog("Erreur",
                    e.getMessage());
        }
    }

    void setUsername(String username) {
        this.username = username;
    }

    void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    void setUnique(boolean unique) {
        this.unique = unique;
    }

    @Override
    public void cancel() {

    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
