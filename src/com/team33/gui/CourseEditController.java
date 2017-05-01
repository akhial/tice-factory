package com.team33.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeView;
import com.team33.model.csv.students.courses.Course;
import com.team33.model.csv.students.courses.CoursesStore;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import sun.reflect.generics.tree.Tree;

import java.util.Collection;
import java.util.HashMap;

public class CourseEditController implements Controller {

    private MainApp mainApp;

    @FXML
    private JFXTreeView<String> treeView;

    private HashMap<JFXButton, Course> courses = new HashMap<>();

    @FXML

    private void initialize() {
        TreeItem<String> root = new TreeItem<>("Root");

        TreeItem<String> prepa = new TreeItem<>("CYCLE PRÉPARATOIRE");
        TreeItem<String> sup = new TreeItem<>("CYCLE SUPÉRIEUR");
        root.getChildren().add(prepa);
        root.getChildren().add(sup);

        TreeItem<String> first = new TreeItem<>("1CPI");
        TreeItem<String> firstS1 = new TreeItem<>("SEMESTRE 1");
        TreeItem<String> firstS2 = new TreeItem<>("SEMESTRE 2");
        first.getChildren().add(firstS1);
        first.getChildren().add(firstS2);
        TreeItem<String> second = new TreeItem<>("2CPI");
        TreeItem<String> secondS1 = new TreeItem<>("SEMESTRE 1");
        TreeItem<String> secondS2 = new TreeItem<>("SEMESTRE 2");
        second.getChildren().add(secondS1);
        second.getChildren().add(secondS2);

        prepa.getChildren().add(first);
        prepa.getChildren().add(second);

        CoursesStore coursesStore = new CoursesStore();
        coursesStore.load();

        setupSemester(firstS1, coursesStore.getCyclePreparatoire().getCPI1().getSemestre1());
        setupSemester(firstS2, coursesStore.getCyclePreparatoire().getCPI1().getSemestre2());
        setupSemester(secondS1, coursesStore.getCyclePreparatoire().getCPI2().getSemestre1());
        setupSemester(secondS2, coursesStore.getCyclePreparatoire().getCPI2().getSemestre2());

        TreeItem<String> third = new TreeItem<>("1CS");
        TreeItem<String> thirdS1 = new TreeItem<>("SEMESTRE 1");
        TreeItem<String> thirdS2 = new TreeItem<>("SEMESTRE 2");

        sup.getChildren().add(third);

        setupSemester(thirdS1, coursesStore.getCycleSuperieur().getCS1().getSemestre1());
        setupSemester(thirdS2, coursesStore.getCycleSuperieur().getCS1().getSemestre2());

        third.getChildren().add(thirdS1);
        third.getChildren().add(thirdS2);

        treeView.setRoot(root);
        treeView.setShowRoot(false);
    }

    private void setupSemester(TreeItem<String> root, Collection<Course> courses) {
        for(Course course : courses) {
            JFXButton button = new JFXButton("");
            button.setId("delete-button");
            TreeItem<String> c = new TreeItem<>(course.toString(), button);
            root.getChildren().add(c);
            this.courses.put(button, course);
        }
        JFXButton addButton = new JFXButton("");
        addButton.setId("add-button");
        TreeItem<String> add2 = new TreeItem<>("Ajouter cours", addButton);
        addButton.setOnAction(e -> {
            // TODO show dialog box to add a course
        });
        root.getChildren().add(add2);
    }

    @Override

    public void cancel() {

    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
