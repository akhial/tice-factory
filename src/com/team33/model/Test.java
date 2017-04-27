package com.team33.model;


import com.team33.model.csv.CSVBuilder;
import com.team33.model.csv.TeacherFormat;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.ArrayList;

public class Test {

    public static void main(String[] args) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("C:\\Users\\Amine\\IdeaProjects\\team-33\\CHARGES_enseignants_Février2017_2016-2017 (1).xlsx");
        arrayList.add("C:\\Users\\Amine\\IdeaProjects\\team-33\\Emails enseignants.xlsx");
        CSVBuilder csvBuilder = new CSVBuilder(arrayList,new TeacherFormat(true),".");
        try {
            csvBuilder.buildCSV();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

    }
}
