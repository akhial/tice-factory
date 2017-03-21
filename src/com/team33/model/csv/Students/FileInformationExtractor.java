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
public class FileInformationExtractor {
    private ColumnsInformationBox box;
    private Sheet sheet;
    private String optin;

    public FileInformationExtractor(ColumnsInformationBox box, Sheet sheet, String optin) {
        this.box = box;
        this.sheet = sheet;
        this.optin = optin;
    }

    public ColumnsInformationBox getBox() {
        return box;
    }

    public void setBox(ColumnsInformationBox box) {
        this.box = box;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    public ArrayList<Student> findStudents() {
        ArrayList<Student> students = new ArrayList<Student>();
        for (Row rw : this.sheet) {
            if (Util.getInstance().existInRow(rw, optin)) {
                Student student = new Student();
                student.setBox(this.box);
                student.rowToBasicInformations(rw);
                students.add(student);
            }

        }
        return students;
    }

    public HashMap<Student, Integer> createStudentsHashMap() {
        HashMap<Student, Integer> hashMap = new HashMap<Student, Integer>();
        for (Row row : this.sheet) {
            if (Util.getInstance().existInRow(row, this.optin)) {
                Student student = new Student();
                student.setBox(this.box);
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
        return hashMap;
    }



}