package com.team33.gui;

import com.jfoenix.controls.JFXListView;
import com.team33.model.statistics.BarChartData;
import com.team33.model.statistics.BaseData;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import sun.reflect.generics.tree.Tree;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.TreeSet;

public class DashboardViewController implements Controller {

    private MainApp mainApp;

    @FXML
    private Label noFile;

    @FXML
    private JFXListView<String> fileLists;

    @FXML
    private BarChart barChart;

    @FXML
    private VBox container;

    @FXML
    public void initialize() {
        ArrayList<String> strings = RecentFileHandler.readFile();
        if(!strings.isEmpty() && fileLists.getItems().isEmpty()) {
            noFile.setVisible(false);
            for(String s : strings) {
                fileLists.getItems().add(s);
            }
        }
        try {
            ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get("chart.dat")));
            ChartData chartData = (ChartData) ois.readObject();
            TreeSet<BaseData> data = chartData.getData();
            barChart.setVisible(false);

            final CategoryAxis xAxis = new CategoryAxis();
            final NumberAxis yAxis = new NumberAxis();
            final BarChart<String,Number> bc =
                    new BarChart<>(xAxis,yAxis);

            xAxis.setLabel("Dates");
            yAxis.setLabel("Occurrences");

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            for(BaseData bd : data) {
                series.getData().add(new XYChart.Data<>(bd.getDate(), bd.getOccurrence()));
            }
            bc.getData().add(series);
            bc.setId("b-chart");
            container.getChildren().remove(container.getChildren().size()-1);
            container.getChildren().add(bc);
            AnchorPane.setTopAnchor(bc, 30.0);
            AnchorPane.setBottomAnchor(bc, 30.0);
            AnchorPane.setRightAnchor(bc, 30.0);
            AnchorPane.setLeftAnchor(bc, 30.0);

        } catch(IOException | ClassNotFoundException e) {
            // file not found so do nothing
        }
    }

    @FXML
    private void onResetButton() {
        RecentFileHandler.clear();
        fileLists.getItems().clear();
        noFile.setVisible(true);
    }

    @Override
    public void cancel() {

    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
