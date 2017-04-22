package com.team33.gui;

import com.team33.model.csv.CSVBuilder;
import com.team33.model.csv.CSVFormat;
import com.team33.model.csv.Students.*;
import com.team33.model.csv.Students.Courses.Mailable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class AggregateHelper {

    private HashMap<String, String> levelPaths = new HashMap<>();
    private String webPath;
    private String outPath;
    private String buttonType;
    private ArrayList<String> listOfUsedEmails = new ArrayList<>();

    public AggregateHelper(String buttonType) {
        this.buttonType = buttonType;
    }

    public void makeLevelCSV(String level) {
        ArrayList<String> workbookPaths = new ArrayList<>();
        workbookPaths.add(webPath);
        workbookPaths.add(levelPaths.get(level));
        CSVFormat format = null;
        StringTokenizer tokenizer = new StringTokenizer(level, "-");

        switch(buttonType) {
            case "list":
                format = new StudentFormat(tokenizer.nextToken(), getOption(level), "");
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

        CSVBuilder csvBuilder = new CSVBuilder(workbookPaths, format, outPath);
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

    private void deleteUsedEmails(Student student) {
        student.getListOfEmails().removeAll(listOfUsedEmails);
    }
}
