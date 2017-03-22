package com.team33.model.csv.Students;

import com.team33.model.Util;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hamza on 20/03/2017.
 */
    class FileInformationExtractor {
    private XSSFWorkbook workbook;
    private String optin;

    public FileInformationExtractor( XSSFWorkbook workbook, String optin) {
        this.workbook = workbook;
        this.optin = optin;
    }


    public ArrayList<Student> findStudents() {
        ArrayList<Student> students = new ArrayList<Student>();
        for(Sheet sheet : this.workbook)
        {
            ColumnsInformationBox box = new ColumnsInformationBox(sheet);
            box.extractInformationsFromFile();
            for (Row rw : sheet) {
                if (Util.getInstance().existInRow(rw, optin)) {
                    Student student = new Student();
                    student.setBox(box);
                    student.rowToBasicInformations(rw);
                    students.add(student);
                }

            }
        }

        return students;
    }

    public HashMap<Student, Integer> createStudentsHashMap() {
        HashMap<Student, Integer> hashMap = new HashMap<Student, Integer>();
        for(Sheet sheet : this.workbook)
        {
            ColumnsInformationBox box = new ColumnsInformationBox(sheet);
            box.extractInformationsFromFile();
            for (Row row : sheet) {
                if (Util.getInstance().existInRow(row, this.optin)) {
                    Student student = new Student();
                    student.setBox(box);
                    student.rowToBasicInformations(row);
                    if (hashMap.containsKey(student)) {
                        int i = hashMap.get(student);
                        i++;
                        hashMap.put(student, i);
                    } else {
                        hashMap.put(student, 1);
                    }
                }

            }
        }

        return hashMap;
    }



}