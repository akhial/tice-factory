package com.team33.gui;

import com.team33.model.statistics.BarChartData;
import com.team33.model.statistics.BaseData;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.TreeSet;

public class BarChartController implements Controller {

    private MainApp mainApp;
    private String criteria;

    @FXML
    private Label criteriaLabel;

    @FXML
    private AnchorPane box;

    void setup() {
        criteriaLabel.setText("OCCURRENCES DU CRITÈRE: " + criteria.toUpperCase());
        BarChartData chartData = new BarChartData(criteria);
        try {
            chartData.readFromFile("temp.xlsx");
            TreeSet<BaseData> data = chartData.getDatas();

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
            bc.setLegendVisible(false);
            box.getChildren().add(bc);
            AnchorPane.setTopAnchor(bc, 30.0);
            AnchorPane.setBottomAnchor(bc, 30.0);
            AnchorPane.setRightAnchor(bc, 30.0);
            AnchorPane.setLeftAnchor(bc, 30.0);

            ChartData dataWrapper = new ChartData(data);

            ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get("chart.dat")));
            oos.writeObject(dataWrapper);
            oos.close();
        } catch(Exception e) {
            mainApp.getMainViewController().showConfirmationDialog("Erreur",
                    "Une erreur c'est produite lors de la lecture du fichier temporaire!");
            mainApp.getMainViewController().setScene(MainApp.STAT_SELECT, MainApp.STAT_NAME);
        }
    }

    void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    @Override
    public void cancel() {

    }

    @Override
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
