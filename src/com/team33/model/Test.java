package com.team33.model;

import com.team33.model.csv.TeacherFormat;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;

public class Test {

    public static void main(String[] args) {
        TeacherFormat teacherFormat = new TeacherFormat();
        teacherFormat.creatTeachersList("C:\\Users\\Amine\\IdeaProjects\\FichierDeDonnées\\CHARGES_enseignants_Février2017_2016-2017 (1).xlsx",
                "C:\\Users\\Amine\\IdeaProjects\\FichierDeDonnées\\Emails enseignants.xlsx",
                "C:\\Users\\Amine\\IdeaProjects\\FichierDeDonnées\\Resultat.xlsx");
        ToCSV toCSV = new ToCSV();
        try {
            toCSV.convertExcelToCSV("C:\\Users\\Amine\\IdeaProjects\\FichierDeDonnées\\Resultat.xlsx","C:\\Users\\Amine\\IdeaProjects\\FichierDeDonnées");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }
}
