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
    protected void initialize() {
        ArrayList<Student> listOfStudentsWithoutEmail = null;
        //listOfStudentsWithoutEmail =
        //((StudentInterface) mainApp.getHelper().getFormat()).getListOfStudentsWithoutEmail();

        HashMap<String, List<String>> students = new HashMap<>();
        students.put("Adel Khial", Arrays.asList("adel", "bilal", "djamel"));
        students.put("Rafik Drissi", Arrays.asList("adel", "bilal", "djamel"));
        students.put("Hamza Zaidi", Collections.EMPTY_LIST);
        students.put("Amine Guerras", Arrays.asList("adel", "bilal", "djamel"));

        // TODO replace with real data

        exceptionPane.setHgap(10);
        exceptionPane.setVgap(10);

        Node node = null;
        Object[] keys = students.keySet().toArray();
        for(int i = 0; i < keys.length; i++) {
            String next = (String) keys[i];
            final int width = 1200;
            if(students.get(next).isEmpty()) {
                node = new JFXTextField();
                ((JFXTextField) node).setPrefWidth(width);
            } else {
                JFXComboBox<String> box = new JFXComboBox<>();
                for(String s : students.get(next)) {
                    box.getItems().add(s);
                }
                box.setPrefWidth(width);
                node = box;
            }
            final JFXButton button = new JFXButton("B");
            comboBoxes.put(button, node);
            //this.students.put(button, listOfStudentsWithoutEmail.get(i));
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
                                listOfUsedEmails.clear();
                            }
                        }
                    });
                    Student current = this.students.get(button);
                    current.setEmail(result);
                    current.generateUsename();
                    ((StudentInterface) mainApp.getHelper().getFormat()).updateRow(current.getPositionInWorkbookOut(), current);
                    input.setDisable(true);
                    // TODO delete used mails, refresh view
                }
            });
            exceptionPane.addRow(i+1, new Label(next), node, button);
        }

    }

    @Override
    public void cancel() {

    }

    @Override
    public void setMainApp(MainApp mainApp) {

    }
}
