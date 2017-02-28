package com.team33.model;

import com.team33.model.csv.CSVBuilder;
import com.team33.model.csv.StudentFormat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Test {

    public static void main(String[] args) throws IOException {
	// write your code here

        StudentFormat studentFormat = new StudentFormat();
        ArrayList<String> workbook = new ArrayList<>();
        workbook.add("liste email tous les etudiants.xlsx");
        workbook.add("Listes-Etudiants_2CPI_S1_2016-2017(1) (2).xlsx");
        CSVBuilder csvBuilder = new CSVBuilder(workbook,new StudentFormat(),"resut");
        csvBuilder.buildCSV();
        csvBuilder.convertToCSV();
    }
}
