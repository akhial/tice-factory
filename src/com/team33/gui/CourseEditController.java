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
       // coursesStore.initialize();
        coursesStore.load();

        setupSemester(firstS1, coursesStore.getCyclePreparatoire().getCPI1().getSemestre1(), 1, "");
        setupSemester(firstS2, coursesStore.getCyclePreparatoire().getCPI1().getSemestre2(), 2, "");
        setupSemester(secondS1, coursesStore.getCyclePreparatoire().getCPI2().getSemestre1(), 3, "");
        setupSemester(secondS2, coursesStore.getCyclePreparatoire().getCPI2().getSemestre2(), 4, "");

        TreeItem<String> third = new TreeItem<>("1CS");
        TreeItem<String> thirdS1 = new TreeItem<>("SEMESTRE 1");
        TreeItem<String> thirdS2 = new TreeItem<>("SEMESTRE 2");

        sup.getChildren().add(third);

        setupSemester(thirdS1, coursesStore.getCycleSuperieur().getCS1().getSemestre1(), 5, "");
        setupSemester(thirdS2, coursesStore.getCycleSuperieur().getCS1().getSemestre2(), 6, "");

        third.getChildren().add(thirdS1);
        third.getChildren().add(thirdS2);

        TreeItem<String> fourth = new TreeItem<>("2CS");
        for(String s : coursesStore.getCycleSuperieur().getCS2().getOptions()) {
            JFXButton button = new JFXButton("");
            button.setId("delete-button");
            TreeItem<String> option = new TreeItem<>(s, button);

            TreeItem<String> fourthS1 = new TreeItem<>("SEMESTRE 1");
            TreeItem<String> fourthS2 = new TreeItem<>("SEMESTRE 2");

            setupSemester(fourthS1, coursesStore.getCycleSuperieur().getCS2().getSemestre1(s), 7, s);
            setupSemester(fourthS2, coursesStore.getCycleSuperieur().getCS2().getSemestre2(s), 8, s);

            option.getChildren().add(fourthS1);
            option.getChildren().add(fourthS2);

            fourth.getChildren().add(option);
            button.setOnAction(e -> {
                coursesStore.getCycleSuperieur().getCS2().getSemestre1(s).clear();
                coursesStore.getCycleSuperieur().getCS2().getSemestre2(s).clear();
                coursesStore.getCycleSuperieur().getCS2().getOptions().remove(s);
                coursesStore.saveChanges();
                fourth.getChildren().remove(option);
            });
        }

        JFXButton addOption = new JFXButton("");
        addOption.setId("add-button");
        TreeItem<String> add = new TreeItem<>("Ajouter option", addOption);
        addOption.setOnAction(e -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/OptionInfoView.fxml"));
            Region region = null;
            try {
                region = loader.load();
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }
            OptionInfoController controller = loader.getController();
            controller.setCourseEditController(this);
            controller.setRoot(fourth);
            controller.setMainApp(mainApp);
            controller.setDialog(mainApp.getMainViewController().showDialog(region));
        });
        fourth.getChildren().add(add);

        TreeItem<String> common = new TreeItem<>("MODULES COMMUNS");
        setupSemester(common, coursesStore.getCycleSuperieur().getCS2().getModulesCommuns(), 10, "");

        fourth.getChildren().add(common);

        sup.getChildren().add(fourth);

        TreeItem<String> fifth = new TreeItem<>("3CS");
        setupSemester(fifth, coursesStore.getCycleSuperieur().getCS3courses(), 9, "");

        sup.getChildren().add(fifth);

        treeView.setRoot(root);
        treeView.setShowRoot(false);
    }

    private void setupSemester(TreeItem<String> root, Collection<Course> courses, int semester, String option) {
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
                    case 7:
                        coursesStore.getCycleSuperieur().getCS2().supprimerCoursSemestre1(option, this.courses.get(button));
                        coursesStore.saveChanges();
                        root.getChildren().remove(c);
                        break;
                    case 8:
                        coursesStore.getCycleSuperieur().getCS2().supprimerCoursSemestre2(option, this.courses.get(button));
                        coursesStore.saveChanges();
                        root.getChildren().remove(c);
                        break;
                    case 9:
                        coursesStore.getCycleSuperieur().suprimerCours3CS(this.courses.get(button));
                        coursesStore.saveChanges();
                        root.getChildren().remove(c);
                        break;
                    case 10:
                        coursesStore.getCycleSuperieur().getCS2().getModulesCommuns().remove(this.courses.get(button));
                        coursesStore.saveChanges();
                        root.getChildren().remove(c);
                        break;
                }
            });
            this.courses.put(button, course);
        }
        JFXButton addButton = new JFXButton("");
        addButton.setId("add-button");
        TreeItem<String> add = new TreeItem<>("Ajouter cours", addButton);
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
            controller.setOption(option);
            controller.setSemester(semester);
            controller.setMainApp(mainApp);
            controller.setDialog(mainApp.getMainViewController().showDialog(region));
        });
        root.getChildren().add(add);
    }

    void addCourse(TreeItem<String> root, int semester, String fullName, String shortName, String option) {
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
                case 7:
                    coursesStore.getCycleSuperieur().getCS2().supprimerCoursSemestre1(option, this.courses.get(button));
                    coursesStore.saveChanges();
                    root.getChildren().remove(c);
                    break;
                case 8:
                    coursesStore.getCycleSuperieur().getCS2().supprimerCoursSemestre2(option, this.courses.get(button));
                    coursesStore.saveChanges();
                    root.getChildren().remove(c);
                    break;
                case 9:
                    coursesStore.getCycleSuperieur().suprimerCours3CS(this.courses.get(button));
                    coursesStore.saveChanges();
                    root.getChildren().remove(c);
                    break;
                case 10:
                    coursesStore.getCycleSuperieur().getCS2().getModulesCommuns().remove(this.courses.get(button));
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
            case 7:
                coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre1(option, shortName, fullName);
                coursesStore.saveChanges();
                break;
            case 8:
                coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre2(option, shortName, fullName);
                coursesStore.saveChanges();
                break;
            case 9:
                coursesStore.getCycleSuperieur().ajouterCours3CS(shortName, fullName);
                coursesStore.saveChanges();
                break;
            case 10:
                coursesStore.getCycleSuperieur().getCS2().ajouterModuleCommun(shortName, fullName);
                coursesStore.saveChanges();
                break;
        }
    }

    void addOption(TreeItem<String> root, String option) {
        coursesStore.getCycleSuperieur().getCS2().ajouterOption(option);
        coursesStore.saveChanges();
        JFXButton button = new JFXButton("");
        button.setId("delete-button");
        TreeItem<String> o = new TreeItem<>(option, button);

        TreeItem<String> s1 = new TreeItem<>("SEMESTRE 1");
        TreeItem<String> s2 = new TreeItem<>("SEMESTRE 2");

        setupSemester(s1, coursesStore.getCycleSuperieur().getCS2().getSemestre1(option), 7, option);
        setupSemester(s2, coursesStore.getCycleSuperieur().getCS2().getSemestre2(option), 8, option);

        o.getChildren().add(s1);
        o.getChildren().add(s2);

        button.setOnAction(e -> {
            coursesStore.getCycleSuperieur().getCS2().getSemestre1(option).clear();
            coursesStore.getCycleSuperieur().getCS2().getSemestre2(option).clear();
            coursesStore.getCycleSuperieur().getCS2().getOptions().remove(option);
            coursesStore.saveChanges();
            root.getChildren().remove(o);
        });
        root.getChildren().add(root.getChildren().size()-2, o);
    }

    @Override

    public void cancel() {

    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
