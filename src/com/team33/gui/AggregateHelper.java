package com.team33.gui;

import com.team33.model.csv.CSVBuilder;
import com.team33.model.csv.CSVFormat;
import com.team33.model.csv.Students.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class AggregateHelper {

    private ObservableList<Label> selectedItems = FXCollections.observableArrayList();

    private HashMap<String, String> levelPaths = new HashMap<>();
    private String webPath;
    private String outPath;
    private String buttonType;
    private CSVFormat format = null;
    private CSVBuilder builder;

    AggregateHelper(String buttonType) {
        this.buttonType = buttonType;
    }

    public void makeLevelCSV(String level) {
        ArrayList<String> workbookPaths = new ArrayList<>();
        workbookPaths.add(webPath);
        workbookPaths.add(levelPaths.get(level));
        StringTokenizer tokenizer = new StringTokenizer(level, "-");

        switch(buttonType) {
            case "list":
                format = new StudentFormat(tokenizer.nextToken(), getOption(level), "");
                // TODO 3CS need InternshipStudentFormat
                break;
            case "courses":
                try {
                    format = new AffectingStudentToCourseFormat(tokenizer.nextToken(), getOption(level), "");
                } catch(IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    // TODO show dialog unreachable
                }
                break;
            case "group":
                try {
                    format = new GroupFormat(tokenizer.nextToken(), getOption(level), "");
                } catch(IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    // TODO show dialog unreachable
                }
                break;
            case "grades":
                format = new GradesFormat(tokenizer.nextToken(), getOption(level), "");
                break;
        }

        builder = new CSVBuilder(workbookPaths, format, outPath);
        try {
            builder.buildCSV();
        } catch(IOException e) {
            e.printStackTrace();
            // TODO show dialog unreachable
        }
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
