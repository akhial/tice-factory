package com.team33.model.csv.students;


import com.team33.model.Utilities.Util;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by hamza on 15/04/2017.
 */
public class ExistingStudents {
    private HashMap<String,Student> students;
    private String csvFileName;

    public ExistingStudents(String csvFileName) {
        this.csvFileName = csvFileName;;
        this.students = new HashMap<>();
    }

    public HashMap<String, Student> getStudents() {
        return students;
    }

    void loadExistingStudents() throws IOException {
        System.out.println(csvFileName);
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(csvFileName.replace(".csv",".xlsx")));
        Row row = workbook.getSheetAt(0).getRow(0);
        int colUsername = Util.getInstance().column(row,"username");
        int colPassword = Util.getInstance().column(row,"password");
        int colFirstname = Util.getInstance().column(row,"firstname");
        int colLastname = Util.getInstance().column(row,"lastname");
        int colEmail = Util.getInstance().column(row,"email");
        int colIdnumber = Util.getInstance().column(row,"idnumber");
        int i = 1;
        row = workbook.getSheetAt(0).getRow(i);
        while (row != null){
            Student student = new Student();
            student.setUsername(row.getCell(colUsername).toString());
            student.setEmail(row.getCell(colEmail).toString());
            student.setFirstName(row.getCell(colFirstname).toString());
            student.setLastNameInMoodle(row.getCell(colLastname).toString());
            student.setPassword(row.getCell(colPassword).toString());
            student.setIdnumber(row.getCell(colIdnumber).toString());
            this.students.put(student.getIdnumber(),student);
            i++;
            row = workbook.getSheetAt(0).getRow(i);
        }
    }
}
