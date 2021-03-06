package com.team33.gui;

import com.jfoenix.controls.JFXDecorator;
import com.team33.model.accounts.AuthenticationType;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private static final String MAIN_APP = "/fxml/MainAppView.fxml";
    static final String DASHBOARD = "/fxml/DashboardView.fxml";
    static final String DASHBOARD_NAME = "ACCEUIL";

    static final String STUDENT_SELECT = "/fxml/StudentSelectionView.fxml";
    static final String TEACHER_SELECT = "/fxml/TeacherSelectionView.fxml";
    static final String WEB_SELECT = "/fxml/WebSelectionView.fxml";
    static final String FILE_SELECT = "/fxml/FileSelectionView.fxml";
    static final String SAVE_SELECT = "/fxml/SaveSelectionView.fxml";
    static final String STUDENT_EXCEPTION = "/fxml/StudentExceptionView.fxml";
    static final String TYPE_SELECT = "/fxml/TypeSelectionView.fxml";
    static final String TEACHER_EXCEPTION = "/fxml/TeacherExceptionView.fxml";
    static final String CONVERT_NAME = "CONVERTIR";
    static final String DUPLICATES = "/fxml/DuplicateCheckView.fxml";
    static final String DUPLICATE_NAME = "DOUBLONS";
    static final String EMAIL_LOGIN = "/fxml/EmailLoginView.fxml";
    static final String MAIL_TYPE_SELECT = "/fxml/EmailTypeView.fxml";
    static final String MAIL_SEND = "/fxml/MailSelectView.fxml";
    static final String MAIL_NAME = "EMAILS";
    static final String STAT_SELECT = "/fxml/StatSelectView.fxml";
    static final String STAT_NAME = "STATISTIQUES";
    static final String COURSE_EDIT = "/fxml/CourseEditView.fxml";
    static final String COURSE_NAME = "COURS";
    static final String BAR_CHART = "/fxml/BarChartView.fxml";
    static final String LOGIN_SCREEN = "/fxml/UserLoginView.fxml";

    private Controller currentController;
    private MainViewController mainViewController;
    private AggregateHelper aggregateHelper;
    private Stage primary;

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(LOGIN_SCREEN));
        Parent login = loader.load();

        ((Controller) loader.getController()).setMainApp(this);

        JFXDecorator decorator = new JFXDecorator(primaryStage, login);

        primaryStage.setTitle("TICE Factory");
        primaryStage.setScene(new Scene(decorator, 800, 600));
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primary = primaryStage;
        primaryStage.show();
    }

    void login(String name, AuthenticationType authenticationType) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(MAIN_APP));
        Parent root = null;
        try {
            root = loader.load();
            mainViewController = loader.getController();
            mainViewController.setMainApp(this);
            mainViewController.setScene(DASHBOARD, DASHBOARD_NAME);

            currentController = mainViewController;

            mainViewController.setUsername(name);
            mainViewController.setTypeLabel(authenticationType);

            if(authenticationType == AuthenticationType.USER) {
                mainViewController.disableFeatures();
            }

            primary.close();
            Stage stage = new Stage();
            JFXDecorator decorator = new JFXDecorator(stage, root);
            stage.setScene(new Scene(decorator, 950, 650));
            stage.setMinWidth(950);
            stage.setMinHeight(650);
            stage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    AggregateHelper getHelper() {
        return aggregateHelper;
    }

    void setAggregateHelper(AggregateHelper aggregateHelper) {
        this.aggregateHelper = aggregateHelper;
        aggregateHelper.setMainApp(this);
    }

    void setCurrentController(Controller controller) {
        currentController = controller;
    }

    public Controller getCurrentController() {
        return currentController;
    }

    MainViewController getMainViewController() {
        return mainViewController;
    }

    void setup() {
        ((FileSelectionController) currentController).setup();
    }

    void cancel() {
        currentController.cancel();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
