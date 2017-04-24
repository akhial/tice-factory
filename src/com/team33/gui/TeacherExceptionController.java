package com.team33.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.team33.model.csv.Students.Student;
import com.team33.model.csv.Students.StudentInterface;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.*;

public class TeacherExceptionController implements Controller {

    private MainApp mainApp;
    private HashMap<String, ArrayList<String>> unhandledEmails = new HashMap<>();
    private ArrayList<String> listOfUsedEmails = new ArrayList<>();
    private HashMap<JFXButton, Node> comboBoxes = new HashMap<>();
    private HashMap<JFXButton, Boolean> verified = new HashMap<>();

    private TeacherSelectionController teacherSelectionController;

    @FXML
    private GridPane exceptionPane;

    @FXML
    @SuppressWarnings("unused")
    private void initialize() {
        exceptionPane.setHgap(10);
        exceptionPane.setVgap(10);

        Node node;
        Object[] keys = unhandledEmails.keySet().toArray();
        for(int i = 0; i < keys.length; i++) {
            String next = (String) keys[i];
            final int width = 1200;
            if(unhandledEmails.get(next).isEmpty()) {
                node = new JFXTextField();
                ((JFXTextField) node).setPromptText("Veuillez saisir une addresse email");
                ((JFXTextField) node).setPrefWidth(width);
            } else {
                JFXComboBox<String> box = new JFXComboBox<>();
                box.setPromptText("Veuillez choisir une addresse email");
                for(String s : unhandledEmails.get(next)) {
                    box.getItems().add(s);
                }
                box.setPrefWidth(width);
                node = box;
            }
            final JFXButton button = new JFXButton("B");
            // TODO add confirm icon
            comboBoxes.put(button, node);
            verified.put(button, Boolean.FALSE);
            button.setOnMouseClicked(e -> {
                verified.replace(button, Boolean.TRUE);
                Node input = comboBoxes.get(button);
                String result;
                if(input instanceof JFXTextField) {
                    result = ((JFXTextField) input).getText();
                } else {
                    result = ((JFXComboBox) input).getSelectionModel().getSelectedItem().toString();
                    listOfUsedEmails.add(result);
                }
                if(!result.isEmpty()) {
                    comboBoxes.forEach((b, n) -> {
                        if(n instanceof JFXComboBox) {
                            if(n != input) {
                                ((JFXComboBox) n).getItems().removeAll(listOfUsedEmails);
                                listOfUsedEmails.clear();
                            }
                        }
                    });
                    input.setDisable(true);
                    // TODO delete used mails, refresh view
                }
            });
            exceptionPane.addRow(i+1, new Label(next), node, button);
        }
    }

    @FXML
    private void onFinishButton() {
        if(verified.containsValue(Boolean.FALSE)) {
            mainApp.getMainViewController().showConfirmationDialog("Erreur",
                    "Veuillez vérifier tout les emails!");
        } else {
            HashMap<String, String> finalMails = new HashMap<>();
            // TODO generate the mails
        }
    }

    void setTeacherSelectionController(TeacherSelectionController teacherSelectionController) {
        this.teacherSelectionController = teacherSelectionController;
    }

    public void setUnhandledEmails(HashMap<String, ArrayList<String>> unhandledEmails) {
        this.unhandledEmails = unhandledEmails;
    }

    @Override
    public void cancel() {

    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
