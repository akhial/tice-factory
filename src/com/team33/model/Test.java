package com.team33.model;

import com.team33.model.Utilities.ToCSV;
import com.team33.model.csv.AssigningTeacherToCourseFormat;
import com.team33.model.csv.CSVBuilder;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.ArrayList;

public class Test {

    public static void main(String[] args) {
        ArrayList<String> workbooks = new ArrayList<>();
        workbooks.add("CHARGES_enseignants_Février2017_2016-2017 (1).xlsx" );
        workbooks.add("Emails enseignants.xlsx");
        CSVBuilder csvBuilder = new CSVBuilder(workbooks,new AssigningTeacherToCourseFormat());
        String tempPath;
        try {
             csvBuilder.buildCSV();
             tempPath = csvBuilder.getTempPath();
            ToCSV toCSV = new ToCSV();
            toCSV.convertExcelToCSV(tempPath,".");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }


    }
}
