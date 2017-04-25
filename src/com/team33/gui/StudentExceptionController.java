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

public class StudentExceptionController implements Controller {

    private MainApp mainApp;
    private ArrayList<String> listOfUsedEmails = new ArrayList<>();
    private HashMap<JFXButton, Node> comboBoxes = new HashMap<>();
    private HashMap<JFXButton, Student> students = new HashMap<>();

    @FXML
    private GridPane exceptionPane;

    @FXML
    @SuppressWarnings("unused")
    private void initialize() {
        ArrayList<Student> listOfStudentsWithoutEmail;
        listOfStudentsWithoutEmail = ((StudentInterface) mainApp.getHelper().getFormat()).getListOfStudentsWithoutEmail();

        exceptionPane.setHgap(10);
        exceptionPane.setVgap(10);

        Node node;
        int i = 0;
        for(Student s : listOfStudentsWithoutEmail) {
            final int width = 700;
            if(s.getListOfEmails().isEmpty()) {
                node = new JFXTextField();
                ((JFXTextField) node).setPromptText("Veuillez saisir une addresse email");
                ((JFXTextField) node).setPrefWidth(width);
            } else {
                JFXComboBox<String> box = new JFXComboBox<>();
                box.setPromptText("Veuillez choisir une addresse email");
                for(String mail : s.getListOfEmails()) {
                    box.getItems().add(mail);
                }
                box.setPrefWidth(width);
                node = box;
            }
            final JFXButton button = new JFXButton("");
            button.setId("green-confirm-button");
            comboBoxes.put(button, node);
            students.put(button, s);
            button.setOnMouseClicked(e -> {
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
                            }
                        }
                    });
                    listOfUsedEmails.clear();
                    Student current = students.get(button);
                    current.setEmail(result);
                    current.generateUsename();
                    ((StudentInterface) mainApp.getHelper().getFormat()).updateRow(current.getPositionInWorkbookOut(), current);
                    input.setDisable(true);
                }
            });
            i++;
            exceptionPane.addRow(i+1, new Label(s.getFirstName() + " " + s.getLastName()), node, button);
        }

    }

    @Override
    public void cancel() {

    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
