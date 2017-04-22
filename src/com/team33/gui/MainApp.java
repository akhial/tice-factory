package com.team33.gui;

import com.jfoenix.controls.JFXDecorator;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    public static final String MAIN_APP = "/fxml/MainAppView.fxml";
    public static final String STUDENT_SELECT = "/fxml/StudentSelectionView.fxml";
    public static final String WEB_SELECT = "/fxml/WebSelectionView.fxml";
    public static final String FILE_SELECT = "/fxml/FileSelectionView.fxml";
    public static final String SAVE_SELECT = "/fxml/SaveSelectionView.fxml";

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(MAIN_APP));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setMainApp(this);

        JFXDecorator decorator = new JFXDecorator(primaryStage, root);

        primaryStage.setTitle("Doodle 2017");
        primaryStage.setScene(new Scene(decorator, 850, 650));
        primaryStage.setMinWidth(850);
        primaryStage.setMinHeight(650);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
