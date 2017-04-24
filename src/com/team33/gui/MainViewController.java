package com.team33.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXSnackbar;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainViewController implements Controller {

    private MainApp mainApp;
    private HashMap<String, Parent> views = new HashMap<>();

    @FXML
    private StackPane rootStackPane;

    @FXML
    private Label contentLabel;

    @FXML
    private GridPane gridPane;

    @FXML
    private ImageView userImage;

    @FXML
    private JFXButton homeButton;

    @FXML
    private JFXButton convertButton;

    @FXML
    private JFXButton mailButton;

    @FXML
    private JFXButton statButton;

    @FXML
    private JFXButton duplicatesButton;

    @FXML
    private JFXButton lessonsButton;

    private ArrayList<JFXButton> buttons = new ArrayList<>();

    @FXML
    @SuppressWarnings("unused")
    private void initialize() {
        setupButtons();
        setButtonSelected(homeButton);

        Circle c = new Circle(15);
        c.setCenterX(userImage.getFitWidth()/2);
        c.setCenterY(userImage.getFitHeight()/2);
        userImage.setClip(c);
    }

    void setScene(final String scene, final String sceneName) {
        contentLabel.setText(sceneName);
        if(gridPane.getChildren().size() > 3)
            gridPane.getChildren().remove(3);
        Parent parent = null;
        if(!views.containsKey(scene)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(scene));
            try {
                parent = loader.load();
            } catch(IOException e) {
                e.printStackTrace();
            }
            views.put(scene, parent);
            try {
                ((Controller) loader.getController()).setMainApp(mainApp);
                mainApp.setCurrentController(loader.getController());
            } catch(NullPointerException e) {
                System.err.println("No controller set in "+loader.getLocation());
            }
        } else {
            parent = views.get(scene);
        }
        gridPane.add(parent, 1, 1);
    }

    private void setButtonSelected(JFXButton button) {
        button.setId(getButtonName(button) + "-selected");
    }

    private void setButtonUnselected(JFXButton button) {
        button.setId(getButtonName(button));
    }

    private void clearButtons() {
        buttons.forEach(this::setButtonUnselected);
    }

    private String getButtonName(JFXButton button) {
        if(button == homeButton) return "home-button";
        if(button == convertButton) return "convert-button";
        if(button == mailButton) return "mail-button";
        if(button == statButton) return "stat-button";
        if(button == duplicatesButton) return "duplicates-button";
        if(button == lessonsButton) return "lessons-button";
        return "";
    }

    private void setupButtons() {
        buttons.add(homeButton);
        buttons.add(convertButton);
        buttons.add(mailButton);
        buttons.add(statButton);
        buttons.add(duplicatesButton);
        buttons.add(lessonsButton);
    }

    @FXML
    private void onMainButtonClick(Event e) {
        clearButtons();
        setButtonSelected((JFXButton) e.getSource());

        if(e.getSource() == homeButton) {
            mainApp.cancel();
            setScene(MainApp.DASHBOARD, MainApp.DASHBOARD_NAME);
        } else if(e.getSource() == convertButton) {
            mainApp.cancel();
            setScene(MainApp.TYPE_SELECT, MainApp.CONVERT_NAME);
        } else if(e.getSource() == mailButton) {
            showDialog();
        } else if(e.getSource() == statButton) {
            showDialog();
        } else if(e.getSource() == duplicatesButton) {
            showDialog();
        } else if(e.getSource() == lessonsButton) {
            showDialog();
        }

        // TODO fix these
    }

    private void showDialog() {
        JFXDialog dialog = new JFXDialog();
        Label content = new Label("Coming soon!");
        content.getStylesheets().add("/fxml/style.css");
        content.setId("bold-label");
        content.setPadding(new Insets(40, 40,40, 40));
        dialog.setContent(content);
        dialog.show(rootStackPane);
    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @Override
    public void cancel() {

    }
}
