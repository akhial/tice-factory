package com.team33.model;

import com.team33.model.csv.AssigningTeacherToCourseFormat;

import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {

        AssigningTeacherToCourseFormat affectingTeacherToCourseFormat = new AssigningTeacherToCourseFormat();
        affectingTeacherToCourseFormat.buildCSV("C:\\Users\\Amine\\IdeaProjects\\" +
                "FichierDeDonnées\\CHARGES_enseignants_Février2017_2016-2017 (1).xlsx","C:\\Users\\Amine\\IdeaProjects\\" +
                "FichierDeDonnées\\Emails enseignants.xlsx");
    }
}
