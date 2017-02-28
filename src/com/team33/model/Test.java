package com.team33.model;

import com.team33.model.csv.StudentFormat;

import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {
	// write your code here

        StudentFormat studentFormat = new StudentFormat();
        studentFormat.createStudentListe("Listes-Etudiants_2CPI_S1_2016-2017(1) (2).xlsx","liste email tous les etudiants.xlsx",1,"test.xlsx","CPI","2CPI");
    }
}
