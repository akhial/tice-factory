package com.team33.model.csv.Students;

import com.team33.model.Util;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by hamza on 20/03/2017.
 */
    class FileInformationExtractor {
    private XSSFWorkbook workbook;
    private String optin;

     FileInformationExtractor() {

    }

    FileInformationExtractor(XSSFWorkbook workbook, String optin) {
        this.workbook = workbook;
        this.optin = optin;
    }


    ArrayList<Student> findStudents() {
        ArrayList<Student> students = new ArrayList<Student>();
        for (Sheet sheet : this.workbook) {
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

     HashMap<Student, Integer> createStudentsHashMap() {
        HashMap<Student, Integer> hashMap = new HashMap<Student, Integer>();
        for (Sheet sheet : this.workbook) {
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

     String ConvertWordTableToExcel(String wordPath) {
        String type;
        try {
            FileInputStream fileInputStream = new FileInputStream(wordPath);
            XWPFDocument xwpfDocument = new XWPFDocument(fileInputStream);
            XWPFWordExtractor we = new XWPFWordExtractor(xwpfDocument);
            if (we.getText().contains("LISTE DES SUJETS DE PFE OPTION SIQ")) {
                type = "3CS-SIQ";
            } else if (we.getText().contains("LISTE DES SUJETS DE PFE OPTION SIT")) {
                type = "3CS-SIT";
            } else {
                type = "3CS-SIL";
            }
            String excelName = type + ".xlsx";
            FileOutputStream fileOutputStream = new FileOutputStream(excelName);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
            Sheet sheet = xssfWorkbook.createSheet();
            sheet.createRow(0).createCell(0).setCellValue(type);
            int i = 0;
            int j = 1;
            int nbColumns = 0;
            Row excelRow = sheet.createRow(j);
            for (XWPFTable table : xwpfDocument.getTables()) {
                for (XWPFTableRow row : table.getRows()) {

                    for (XWPFTableCell cell : row.getTableCells()) {
                        excelRow.createCell(i).setCellValue(cell.getText());
                        i++;
                    }
                    excelRow.createCell(i).setCellValue(type);
                    nbColumns = i;
                    i = 0;
                    j++;
                    excelRow = sheet.createRow(j);
                }
                sheet.getRow(j - table.getNumberOfRows()).getCell(nbColumns).setCellValue("NG");
            }
            xssfWorkbook.write(fileOutputStream);
            fileInputStream.close();
            return excelName;
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";

    }

     String getFileType(File file) {


        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();
            Row rw = rowIterator.next();
            while (rowIterator.hasNext()) {
                if (Util.getInstance().existInRow(rw, "NG")) {
                    return "Solarite";
                } else {
                    rw = rowIterator.next();
                }
            }
            return "web";

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";


    }
}