package com.team33.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeView;
import com.team33.model.csv.students.courses.Course;
import com.team33.model.csv.students.courses.CoursesStore;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

public class CourseEditController implements Controller {

    private MainApp mainApp;

    @FXML
    private JFXTreeView<String> treeView;

    private HashMap<JFXButton, Course> courses = new HashMap<>();
    private CoursesStore coursesStore;

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

        coursesStore = new CoursesStore();
        //coursesStore.initialize();
        coursesStore.load();

        setupSemester(firstS1, coursesStore.getCyclePreparatoire().getCPI1().getSemestre1(), 1);
        setupSemester(firstS2, coursesStore.getCyclePreparatoire().getCPI1().getSemestre2(), 2);
        setupSemester(secondS1, coursesStore.getCyclePreparatoire().getCPI2().getSemestre1(), 3);
        setupSemester(secondS2, coursesStore.getCyclePreparatoire().getCPI2().getSemestre2(), 4);

        TreeItem<String> third = new TreeItem<>("1CS");
        TreeItem<String> thirdS1 = new TreeItem<>("SEMESTRE 1");
        TreeItem<String> thirdS2 = new TreeItem<>("SEMESTRE 2");

        sup.getChildren().add(third);

        setupSemester(thirdS1, coursesStore.getCycleSuperieur().getCS1().getSemestre1(), 5);
        setupSemester(thirdS2, coursesStore.getCycleSuperieur().getCS1().getSemestre2(), 6);

        third.getChildren().add(thirdS1);
        third.getChildren().add(thirdS2);

        TreeItem<String> fourth = new TreeItem<>("2CS");
        for(String s : coursesStore.getCycleSuperieur().getCS2().getOptions()) {
            TreeItem<String> option = new TreeItem<>(s);
            fourth.getChildren().add(option);
        }
        JFXButton addOption = new JFXButton("");
        addOption.setId("add-button");
        TreeItem<String> add = new TreeItem<>("Ajouter option", addOption);
        addOption.setOnAction(e -> {
            // TODO show dialog box to add a option
        });
        fourth.getChildren().add(add);

        sup.getChildren().add(fourth);

        treeView.setRoot(root);
        treeView.setShowRoot(false);
    }

    private void setupSemester(TreeItem<String> root, Collection<Course> courses, int semester) {
        for(Course course : courses) {
            JFXButton button = new JFXButton("");
            button.setId("delete-button");
            TreeItem<String> c = new TreeItem<>(course.toString(), button);
            root.getChildren().add(c);
            button.setOnAction(e -> {
                switch(semester) {
                    case 1:
                        coursesStore.getCyclePreparatoire().getCPI1().supprimerCoursSemestre1(this.courses.get(button));
                        coursesStore.saveChanges();
                        root.getChildren().remove(c);
                        break;
                    case 2:
                        coursesStore.getCyclePreparatoire().getCPI1().supprimerCoursSemestre2(this.courses.get(button));
                        coursesStore.saveChanges();
                        root.getChildren().remove(c);
                        break;
                    case 3:
                        coursesStore.getCyclePreparatoire().getCPI2().supprimerCoursSemestre1(this.courses.get(button));
                        coursesStore.saveChanges();
                        root.getChildren().remove(c);
                        break;
                    case 4:
                        coursesStore.getCyclePreparatoire().getCPI2().supprimerCoursSemestre2(this.courses.get(button));
                        coursesStore.saveChanges();
                        root.getChildren().remove(c);
                        break;
                    case 5:
                        coursesStore.getCycleSuperieur().getCS1().supprimerCoursSemestre1(this.courses.get(button));
                        coursesStore.saveChanges();
                        root.getChildren().remove(c);
                        break;
                    case 6:
                        coursesStore.getCycleSuperieur().getCS1().supprimerCoursSemestre2(this.courses.get(button));
                        coursesStore.saveChanges();
                        root.getChildren().remove(c);
                        break;
                }
            });
            this.courses.put(button, course);
        }
        JFXButton addButton = new JFXButton("");
        addButton.setId("add-button");
        TreeItem<String> add2 = new TreeItem<>("Ajouter cours", addButton);
        addButton.setOnAction(e -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/CourseInfoView.fxml"));
            Region region = null;
            try {
                region = loader.load();
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }
            CourseInfoController controller = loader.getController();
            controller.setCourseEditController(this);
            controller.setRoot(root);
            controller.setSemester(semester);
            controller.setDialog(mainApp.getMainViewController().showDialog(region));
        });
        root.getChildren().add(add2);
    }

    void addCourse(TreeItem<String> root, int semester, String fullName, String shortName) {
        Course course = new Course(shortName, fullName);
        JFXButton button = new JFXButton("");
        button.setId("delete-button");
        TreeItem<String> c = new TreeItem<>(course.toString(), button);
        button.setOnAction(e -> {
            switch(semester) {
                case 1:
                    coursesStore.getCyclePreparatoire().getCPI1().supprimerCoursSemestre1(this.courses.get(button));
                    coursesStore.saveChanges();
                    root.getChildren().remove(c);
                    break;
                case 2:
                    coursesStore.getCyclePreparatoire().getCPI1().supprimerCoursSemestre2(this.courses.get(button));
                    coursesStore.saveChanges();
                    root.getChildren().remove(c);
                    break;
                case 3:
                    coursesStore.getCyclePreparatoire().getCPI2().supprimerCoursSemestre1(this.courses.get(button));
                    coursesStore.saveChanges();
                    root.getChildren().remove(c);
                    break;
                case 4:
                    coursesStore.getCyclePreparatoire().getCPI2().supprimerCoursSemestre2(this.courses.get(button));
                    coursesStore.saveChanges();
                    root.getChildren().remove(c);
                    break;
                case 5:
                    coursesStore.getCycleSuperieur().getCS1().supprimerCoursSemestre1(this.courses.get(button));
                    coursesStore.saveChanges();
                    root.getChildren().remove(c);
                    break;
                case 6:
                    coursesStore.getCycleSuperieur().getCS1().supprimerCoursSemestre2(this.courses.get(button));
                    coursesStore.saveChanges();
                    root.getChildren().remove(c);
                    break;
            }
        });
        root.getChildren().add(root.getChildren().size()-1, c);
        this.courses.put(button, course);
        switch(semester) {
            case 1:
                coursesStore.getCyclePreparatoire().getCPI1().ajouterCoursSemsetre1(shortName, fullName);
                coursesStore.saveChanges();
                break;
            case 2:
                coursesStore.getCyclePreparatoire().getCPI1().ajouterCoursSemstre2(shortName, fullName);
                coursesStore.saveChanges();
                break;
            case 3:
                coursesStore.getCyclePreparatoire().getCPI2().ajouterCoursSemsetre1(shortName, fullName);
                coursesStore.saveChanges();
                break;
            case 4:
                coursesStore.getCyclePreparatoire().getCPI2().ajouterCoursSemstre2(shortName, fullName);
                coursesStore.saveChanges();
                break;
            case 5:
                coursesStore.getCycleSuperieur().getCS1().ajouterCoursSemsetre1(shortName, fullName);
                coursesStore.saveChanges();
                break;
            case 6:
                coursesStore.getCycleSuperieur().getCS1().ajouterCoursSemstre2(shortName, fullName);
                coursesStore.saveChanges();
                break;
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
