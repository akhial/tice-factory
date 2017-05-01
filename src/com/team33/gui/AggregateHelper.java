package com.team33.gui;

import com.team33.model.csv.CSVBuilder;
import com.team33.model.csv.CSVFormat;
import com.team33.model.csv.students.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class AggregateHelper {

    private MainApp mainApp;
    private ObservableList<Label> selectedItems = FXCollections.observableArrayList();
    private int i = 0;

    private HashMap<String, String> levelPaths = new HashMap<>();
    private String webPath;
    private String outPath;
    private String buttonType;
    private CSVFormat format = null;
    private CSVBuilder builder;

    AggregateHelper(String buttonType) {
        this.buttonType = buttonType;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void makeLevelCSV() {
        if(i < levelPaths.size()) {
            String level = selectedItems.get(i).getText();
            ArrayList<String> workbookPaths = new ArrayList<>();
            workbookPaths.add(webPath);
            workbookPaths.add(levelPaths.get(level));
            StringTokenizer tokenizer = new StringTokenizer(level, "-");

            String levelName = tokenizer.nextToken();
            switch(buttonType) {
                case "list":
                    if(levelName.equals("3CS"))
                        format = new IntershipStudentFormat(levelName, getOption(level), "");
                    else
                        format = new StudentFormat(levelName, getOption(level), "");
                    break;
                case "courses":
                    try {
                        if(levelName.equals("3CS"))
                            format = new AffectingIntershipStudentToCourseFormat(levelName, getOption(level), "");
                        else
                            format = new AffectingStudentToCourseFormat(levelName, getOption(level), "");
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "group":
                    try {
                        format = new GroupFormat(levelName, getOption(level), "");
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "grades":
                    format = new GradesFormat(levelName, getOption(level), "");
                    break;
            }

            builder = new CSVBuilder(workbookPaths, format, outPath);
            System.out.println(workbookPaths);
            try {
                builder.buildCSV();
            } catch(IOException | InvalidFormatException e) {
                e.printStackTrace();
            }
            mainApp.getMainViewController().setScene(MainApp.STUDENT_EXCEPTION, MainApp.CONVERT_NAME);
            ((StudentExceptionController) mainApp.getCurrentController()).setup();
            ((StudentExceptionController) mainApp.getCurrentController()).setAggregateHelper(this);
            i++;
        } else {
            mainApp.getMainViewController().showConfirmationDialog("Succés", "Tout les fichiers ont étés créés");
            mainApp.getMainViewController().setScene(MainApp.DASHBOARD, MainApp.CONVERT_NAME);
        }
    }

    public void finishCSV() throws IOException {
        ((StudentInterface) format).saveUsersList(new File(builder.getTempPath()));
        builder.convertToCSV();
        makeLevelCSV();
    }

    private String getOption(String level) {
        StringTokenizer tokenizer = new StringTokenizer(level, "-");
        String first = tokenizer.nextToken();
        switch(first) {
            case "1CPI":
            case "2CPI":
                return "CPI";
            case "1CS":
                return "SC";
            case "2CS":
            case "3CS":
                return tokenizer.nextToken();
        }
        return ""; // unreachable
    }

    void addLevelPaths(String level, String path) {
        levelPaths.put(level, path);
    }

    public void setWebPath(String webPath) {
        this.webPath = webPath;
    }

    public void setOutPath(String outPath) {
        this.outPath = outPath;
    }

    CSVFormat getFormat() {
        return format;
    }

    CSVBuilder getBuilder() {
        return builder;
    }

    public String getButtonType() {
        return buttonType;
    }

    public HashMap<String, String> getLevelPaths() {
        return levelPaths;
    }

    public void setLevels(ObservableList<Label> levels) {
        for(Label l : levels) {
            System.out.println(l.getText());
            selectedItems.add(l);
        }
    }

    public ObservableList<Label> getLevels() {
        return selectedItems;
    }
}
