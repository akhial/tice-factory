package com.team33.model.csv.Students;

import com.team33.model.Utilities.Util;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
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
import java.util.Map;

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

    String ConvertWordTableToExcel(String wordPath, String optin) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(wordPath);
        XWPFDocument xwpfDocument = new XWPFDocument(fileInputStream);
        XWPFWordExtractor we = new XWPFWordExtractor(xwpfDocument);
        String excelName = optin + ".xlsx";
        FileOutputStream fileOutputStream = new FileOutputStream(excelName);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        Sheet sheet = xssfWorkbook.createSheet();
        int i = 0;
        int j = 0;
        int nbColumns = 0;
        Row excelRow = sheet.createRow(j);
        for (XWPFTable table : xwpfDocument.getTables()) {
            for (XWPFTableRow row : table.getRows()) {

                for (XWPFTableCell cell : row.getTableCells()) {
                    excelRow.createCell(i).setCellValue(cell.getText());
                    i++;
                }
                excelRow.createCell(i).setCellValue(optin);
                nbColumns = i;
                i = 0;
                j++;
                excelRow = sheet.createRow(j);
            }
            sheet.getRow(j - table.getNumberOfRows()).getCell(nbColumns).setCellValue("Optin");
        }
        xssfWorkbook.write(fileOutputStream);
        fileInputStream.close();
        return excelName;
    }

     void changeInvalidOptin()
    {
        boolean found = false;
        int i = -1;
        String fileOptin = "";
        Iterator<Row> rowIterator = this.workbook.getSheetAt(0).iterator();
        while (rowIterator.hasNext() && !found)
        {
            Row row = rowIterator.next();

            if(Util.getInstance().existInRow(row,"Optin"))
            {
                found = true;
                i = Util.getInstance().column(row,"Optin");
                fileOptin = this.workbook.getSheetAt(0).getRow(row.getRowNum()+1).getCell(i).toString();
            }
        }
        if(!fileOptin.equals(this.optin))
        {
            for(Sheet sheet : this.workbook)
            {
                for(Row  row : sheet)
                    if(Util.getInstance().existInRow(row,fileOptin))
                    {
                        row.getCell(i).setCellValue(this.optin);
                    }
            }
        }
    }

    HashMap<String,Student> findStudents() {
        HashMap<String,Student> students = new HashMap<>();
        changeInvalidOptin();
        for (Sheet sheet : this.workbook) {
            ColumnsInformationBox box = new ColumnsInformationBox(sheet);
            box.extractInformationsFromFile();
            for (Row rw : sheet) {
                if (Util.getInstance().existInRow(rw, optin)) {
                    Student student = new Student();
                    student.setBox(box);
                    student.rowToBasicInformations(rw);
                    students.put(student.getIdnumber(),student);
                }

            }
        }

        return students;
    }

    public ArrayList<Student> findIntershipStudents() {
        ArrayList<Student> students = new ArrayList<Student>();
        for (Row rw : this.workbook.getSheetAt(0)) {
            if (Util.getInstance().existInRow(rw, optin)) {
                Student student = new Student();
                ColumnsInformationBox box = new ColumnsInformationBox(this.workbook.getSheetAt(0));
                box.extractInformationsFromFile();
                student.setBox(box);
                student.rowToBasicInformations(rw);
                students.add(student);
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



     String getFileType(File file) throws IOException {


            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();
            Row rw = rowIterator.next();
            while (rowIterator.hasNext()) {
                if (Util.getInstance().existInRow(rw, "Optin")) {
                    return "Solarite";
                } else {
                    rw = rowIterator.next();
                }
            }
            return "web";

        }

     HashMap<String,ArrayList<String>> extractOptionalModules()
    {
        String str = null;
        int colGroupe = -1;
        String key = "";

        HashMap<String,ArrayList<String>> optionalModules = new HashMap<String,ArrayList<String>>();
        ArrayList<String> modules = null;
        for(Sheet sheet : this.workbook)
        {
            for (Row row : sheet) {
                if (Util.getInstance().rowContainsIgnoreCase(row, "Modules Optionnels")) {
                    modules = extractOptionalModule(row);
                }
                if (Util.getInstance().existInRow(row, "NG")) {
                    colGroupe = Util.getInstance().column(row, "NG");
                    Row rw = sheet.getRow(row.getRowNum() + 1);
                    rw.getCell(colGroupe).setCellType(CellType.STRING);
                    key = rw.getCell(colGroupe).getStringCellValue();
                    if(!optionalModules.containsKey(key))
                    {
                        optionalModules.put(key,modules);
                    }

                }
            }
        }

        return optionalModules;
    }

    private ArrayList<String> extractOptionalModule(Row row)
    {
        ArrayList<String> modulesOptionnels = new ArrayList<>();
        String str = "";
        for(Cell cell : row)
        {
            str = str + cell.toString();
        }
        str = str.toLowerCase();
        str = str.replace("modules","");
        str = str.replace("optionnels","");
        str = str.replace(":","");
        str = str.replace(" ","");
        str = str.toUpperCase();
        char[] array = str.toCharArray();
        str ="";
        for(int i = 0; i < array.length;i++)
        {
            if((array[i] == '_'))
            {
                modulesOptionnels.add(str);
                str ="";
            }
            else
            {
                str = str + array[i];
                if(i == array.length - 1) modulesOptionnels.add(str);
            }
        }
        return modulesOptionnels;
    }

}