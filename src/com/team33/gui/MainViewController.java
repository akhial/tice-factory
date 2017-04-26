package com.team33.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainViewController implements Controller {

    private MainApp mainApp;
    private HashMap<String, Parent> views = new HashMap<>();
    private JFXDialog loadingDialog;

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
        //if(!views.containsKey(scene)) {
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
        //} else {
        //    parent = views.get(scene);
            // TODO cache controllers
        //}
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
            ((DashboardViewController) mainApp.getCurrentController()).initialize();
        } else if(e.getSource() == convertButton) {
            mainApp.cancel();
            setScene(MainApp.TYPE_SELECT, MainApp.CONVERT_NAME);
        } else if(e.getSource() == mailButton) {
            mainApp.cancel();
            setScene(MainApp.EMAIL_LOGIN, MainApp.MAIL_NAME);
        } else if(e.getSource() == statButton) {
            mainApp.cancel();
            mainApp.getMainViewController().setScene(MainApp.STAT_SELECT, MainApp.STAT_NAME);
        } else if(e.getSource() == duplicatesButton) {
            mainApp.cancel();
            setScene(MainApp.DUPLICATES, MainApp.DUPLICATE_NAME);
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

    void showConfirmationDialog(final String title, final   String message) {
        JFXDialog dialog = new JFXDialog();
        loadingDialog = dialog;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/ConfirmationDialogBox.fxml"));
        Region region = null;
        try {
            region = loader.load();
        } catch(IOException e) {
            e.printStackTrace();
        }
        ((ConfirmationDialogBoxController) loader.getController()).setDialog(title, message);
        ((ConfirmationDialogBoxController) loader.getController()).setDialog(dialog);
        dialog.setContent(region);
        dialog.show(rootStackPane);
    }

    void showLoadingDialog() {
        JFXDialog dialog = new JFXDialog();
        FXMLLoader loader = new FXMLLoader();
        loadingDialog = dialog;
        loader.setLocation(getClass().getResource("/fxml/LoadingView.fxml"));
        Region region = null;
        try {
            region = loader.load();
        } catch(IOException e) {
            e.printStackTrace();
        }
        dialog.setContent(region);
        dialog.setDisable(true);
        dialog.show(rootStackPane);
    }

    void hideLoadingDialog() {
        loadingDialog.close();
    }

    void showLongConfirmationDialog(final String title, final String message, DuplicateCheckController controller) {
        JFXDialog dialog = new JFXDialog();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/LongDialogBox.fxml"));
        Region region = null;
        try {
            region = loader.load();
        } catch(IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(region));
        stage.setTitle("Doublon trouvé");
        ((LongDialogController) loader.getController()).setDialog(title, message);
        ((LongDialogController) loader.getController()).setStage(stage);
        ((LongDialogController) loader.getController()).setDuplicateCheckController(controller);
        //dialog.setContent(region);
        //dialog.show(rootStackPane);
        stage.showAndWait();
    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @Override
    public void cancel() {

    }
}
