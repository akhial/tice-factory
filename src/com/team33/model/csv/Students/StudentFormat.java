package com.team33.model.csv.Students;

import com.team33.model.Util;
import com.team33.model.csv.CSVFormat;
import com.team33.model.csv.UserFormat;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.*;
import java.net.URI;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by Amine on 13/02/2017.
 */

public class StudentFormat extends UserFormat implements CSVFormat {

    private String level;
    private String optin;
    private String filePathOut;
    private ArrayList<Student> listOfStudentsWithoutEmail;

    public ArrayList<Student> getListOfStudentsWithoutEmail() {
        return listOfStudentsWithoutEmail;
    }

    public StudentFormat(String level, String optin, String filePathOut) {
        this.listOfStudentsWithoutEmail = new ArrayList<Student>();
        this.level = level;
        this.optin = optin;
        if(filePathOut == null || filePathOut.equals(""))
        {
            if(optin.equals("CPI") || level.equals("1CS"))
                filePathOut = level;
            else filePathOut = level + optin ;
        }
        this.filePathOut = filePathOut+".xlsx";
    }


    /*
     *Méthodes utilitaires
     */


    public void generateRow(int numRow, Student student)// générer une ligne cde fichier résultat contenant les coordonné d'un étudiant
    {
        Row rw = getWorkbookOut().getSheetAt(0).createRow(numRow);
        for (int j = 0; j < 4; j++) {
            rw.createCell(j);
        }
        rw.getCell(0).setCellValue(student.getUsername());
        rw.getCell(1).setCellValue(student.getFirstName());
        rw.getCell(2).setCellValue(student.getLastNameInMoodle());
        rw.getCell(3).setCellValue(student.getEmail());
    }

    private String getLevel() {
        int i = 0;
        String niveau;
        if (Util.getInstance().existInRow(this.getWorkbookIn().getSheetAt(0).getRow(0), "3CS-SIL")) return "3CS-SIL";
        if (Util.getInstance().existInRow(this.getWorkbookIn().getSheetAt(0).getRow(0), "3CS-SIQ")) return "3CS-SIQ";
        if (Util.getInstance().existInRow(this.getWorkbookIn().getSheetAt(0).getRow(0), "3CS-SIT")) return "3CS-SIT";
        for (Sheet sh : getWorkbookIn()) {
            for (Row rw : sh) {
                for (Cell cell : rw) {
                    niveau = cell.toString();
                    if (niveau.toUpperCase().contains("1CPI")) {
                        return "1CPI";
                    }
                    if (niveau.toUpperCase().contains("2CPI")) {
                        return "2CPI";
                    }
                    if (niveau.toUpperCase().contains("SC") || niveau.toLowerCase().contains("1ère")) {
                        i = 1;
                    }
                    if (niveau.toLowerCase().contains("2ème")) {
                        i = 2;
                    }
                    if (niveau.toUpperCase().contains("CPI") && i == 1) {
                        i = 3;
                    }
                    if (niveau.toUpperCase().contains("CPI") && i == 2) {
                        i = 4;
                    }
                    if (niveau.toUpperCase().contains("SIL")) {
                        return "2CS-SIL";
                    }
                    if (niveau.toUpperCase().contains("SIQ")) {
                        return "2CS-SIQ";
                    }
                    if (niveau.toUpperCase().contains("SIT")) {
                        return "2CS-SIT";
                    }
                }
            }
        }
        if (i == 1) {
            return "1CS";
        }
        if (i == 2) {
            return "2CS";
        }
        if (i == 3) {
            return "1CPI";
        }
        if (i == 4) {
            return "2CPI";
        }
        return "";
    }

    private String getFileType(File file) {


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

    private String ConvertWordTableToExcel(String wordPath) {
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
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";

    }

    private String nameOfEmailSheet() {
        if (this.level.equals("1CPI")) return "1cpi";
        else if (this.level.equals("2CPI")) return "2cpi";
        else if (this.level.equals("3CS")) return "3cs";
        else return this.level + this.optin;
    }

    private void createStudentList() {
        int numRow = 1;

        generateHeader();

        FileInformationExtractor extractor = new FileInformationExtractor(getWorkbookIn(), optin);
        ArrayList<Student> students = extractor.findStudents();
        HashMap<Student, Integer> studentHashMap = extractor.createStudentsHashMap();
        EmailFinder emailFinder = new EmailFinder(nameOfEmailSheet(), getEmailsWorkbook(), studentHashMap);


        for (Student student : students) {
            student.setLevel(level);
            emailFinder.setStudent(student);
            emailFinder.getEmails();
            student.setStudentInformations();
            if (!student.hasEmail()) {
                student.setPositionInWorkbookOut(numRow);
                this.listOfStudentsWithoutEmail.add(student);
            }
            generateRow(numRow, student);
            numRow++;
        }
        File file = new File(filePathOut);
        saveUsersList(file);

    }


    @Override
    public String buildCSV(ArrayList<String> workbooksPaths)  {
        // WorkbooksPaths should contain only list of first semester and list of e-mails

        String type;
        FileInformationExtractor extractor = new FileInformationExtractor();
        for (String workbooksPath : workbooksPaths) {
            if(workbooksPath.contains(".docx"))
            {

                workbooksPath = extractor.ConvertWordTableToExcel(workbooksPath);
            }
            File file = new File(workbooksPath);
            type = extractor.getFileType(file);
            if (type.equals("Solarite")) openWorkbookIn(workbooksPath);
            else openEmailWorkbook(workbooksPath);
        }
        createStudentList();

        return filePathOut;
    }
}