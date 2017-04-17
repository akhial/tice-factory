package com.team33.model;

import com.team33.model.csv.AssigningTeacherToCourseFormat;
import com.team33.model.csv.Concatenater;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {

        Concatenater concatenater = new Concatenater();
        try {
            concatenater.Concatenate(
                    "C:\\Users\\Amine\\IdeaProjects\\team-33\\Excel Sheets\\1CSgroupes.xlsx" ,
                    "C:\\Users\\Amine\\IdeaProjects\\team-33\\Excel Sheets\\2CPIgroupes.xlsx" ,
                    "C:\\Users\\Amine\\IdeaProjects\\team-33\\Excel Sheets\\2CSSITgroupes.xlsx");
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }
}
