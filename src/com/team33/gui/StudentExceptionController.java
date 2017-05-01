package com.team33.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.team33.model.csv.students.Student;
import com.team33.model.csv.students.StudentInterface;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.*;

public class StudentExceptionController implements Controller {

    private MainApp mainApp;
    private ArrayList<String> listOfUsedEmails = new ArrayList<>();
    private HashMap<JFXButton, Node> comboBoxes = new HashMap<>();
    private HashMap<JFXButton, Student> students = new HashMap<>();

    private AggregateHelper aggregateHelper;

    @FXML
    private GridPane exceptionPane;

    void setup() {
        ArrayList<Student> listOfStudentsWithoutEmail;
        listOfStudentsWithoutEmail = ((StudentInterface) mainApp.getHelper().getFormat()).getListOfStudentsWithoutEmail();

        exceptionPane.setHgap(10);
        exceptionPane.setVgap(10);

        exceptionPane.getChildren().remove(2, exceptionPane.getChildren().size());

        Node node;
        int i = 0;
        for(Student s : listOfStudentsWithoutEmail) {
            final int width = 1200;
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
                    Object selectedItem = ((JFXComboBox) input).getSelectionModel().getSelectedItem();
                    if(selectedItem != null)
                        result = selectedItem.toString();
                    else
                        result = "";
                }
                listOfUsedEmails.add(result);
                button.setDisable(true);
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

            });
            i++;
            Label label = new Label(s.getFirstName()+" "+s.getLastName());
            label.setMinWidth(100);
            exceptionPane.addRow(i+1, label, node, button);
        }
    }

    @FXML
    private void onFinishButton() {
        if(containsEmptyNode()) {
            mainApp.getMainViewController().showConfirmationDialog("Erreur",
                    "Veuillez vérifier tout les emails!");
        } else {
            try {
                aggregateHelper.finishCSV();
            } catch(IOException e) {
                mainApp.getMainViewController().showConfirmationDialog("Erreur",
                        "Une erreur c'est produite lors de l'ecriture du fichier!");
            }
        }
    }

    private boolean containsEmptyNode() {
        // TODO change verification criteria
        for(Node n : comboBoxes.values()) {
            if(!n.isDisable()) return true;
        }
        return false;
    }

    void setAggregateHelper(AggregateHelper aggregateHelper) {
        this.aggregateHelper = aggregateHelper;
    }

    @Override
    public void cancel() {

    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
