package com.team33.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
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
    private HashMap<JFXButton, String> verified = new HashMap<>();
    private HashMap<String, String> finalMails = new HashMap<>();

    private TeacherSelectionController teacherSelectionController;

    @FXML
    private GridPane exceptionPane;

    public void setup() {
        exceptionPane.setHgap(10);
        exceptionPane.setVgap(10);

        exceptionPane.getChildren().remove(2, exceptionPane.getChildren().size());

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
            final JFXButton button = new JFXButton("");
            verified.put(button, next);
            button.setId("green-confirm-button");
            comboBoxes.put(button, node);
            button.setOnMouseClicked(e -> {
                Node input = comboBoxes.get(button);
                String result = "";
                if(input instanceof JFXTextField) {
                    result = ((JFXTextField) input).getText();
                } else {
                    if(((JFXComboBox) input).getSelectionModel().getSelectedItem() != null) {
                        result = ((JFXComboBox) input).getSelectionModel().getSelectedItem().toString();
                        listOfUsedEmails.add(result);
                    } else {
                        result = "";
                    }
                }
                if(!result.isEmpty()) {
                    comboBoxes.forEach((b, n) -> {
                        if(n instanceof JFXComboBox) {
                            if(n != input) {
                                ((JFXComboBox) n).getItems().removeAll(listOfUsedEmails);
                                if(((JFXComboBox) n).getItems().size() == 1) {
                                    ((JFXComboBox) n).getSelectionModel().selectFirst();
                                    n.setDisable(true);
                                    String name = "";
                                    for(Map.Entry<JFXButton, Node> entry : comboBoxes.entrySet()) {
                                        if(entry.getValue().equals(n)) {
                                            JFXButton key = entry.getKey();
                                            key.setDisable(true);
                                            name = verified.get(key);
                                        }
                                    }
                                    finalMails.put(name, ((JFXComboBox) n).getSelectionModel().getSelectedItem().toString());
                                }
                            }
                        }
                    });
                    listOfUsedEmails.clear();
                    input.setDisable(true);
                    button.setDisable(true);
                } else {
                    input.setDisable(true);
                    button.setDisable(true);
                }
                finalMails.put(verified.get(button), result);
            });
            String parsed = next.replace('*', ' ');
            exceptionPane.addRow(i+1, new Label(parsed), node, button);
        }
    }

    @FXML
    private void onFinishButton() {
        if(containsEmptyNode()) {
            mainApp.getMainViewController().showConfirmationDialog("Erreur",
                    "Veuillez vérifier tout les emails!");
        } else {
            teacherSelectionController.setFinalMails(finalMails);
            teacherSelectionController.finishCSV();
        }
    }

    private boolean containsEmptyNode() {
        for(Node n : comboBoxes.values()) {
            if(!n.isDisable()) return true;
        }
        return false;
    }

    void setTeacherSelectionController(TeacherSelectionController teacherSelectionController) {
        this.teacherSelectionController = teacherSelectionController;
    }

    public void setUnhandledEmails(HashMap<String, ArrayList<String>> unhandledEmails) {
        this.unhandledEmails = (HashMap<String, ArrayList<String>>) unhandledEmails.clone();
    }

    @Override
    public void cancel() {

    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
