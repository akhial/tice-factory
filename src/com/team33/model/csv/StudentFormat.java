package com.team33.model.csv;

import com.team33.model.Util;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Amine on 13/02/2017.
 */

public class StudentFormat extends UserFormat implements CSVFormat {


    private ArrayList<Student> listOfStudentsWithoutEmail;

    public ArrayList<Student> getListOfStudentsWithoutEmail() {
        return listOfStudentsWithoutEmail;
    }

    public StudentFormat() {
        this.listOfStudentsWithoutEmail = new ArrayList<Student>();
    }

    /*
     *Méthodes utilitaires
     */


    private void generateRow(int numRow, Student student)// générer une ligne cde fichier résultat contenant les coordonné d'un étudiant
    {
        Row rw = getWorkbookOut().getSheetAt(0).createRow(numRow);
        for (int j = 0; j < 4; j++) {
            rw.createCell(j);
        }
        rw.getCell(0).setCellValue(student.getUsername());
        rw.getCell(1).setCellValue(student.getPassword());
        rw.getCell(2).setCellValue(student.getFirstName());
        rw.getCell(3).setCellValue(student.getLastNameInMoodle());
        rw.getCell(4).setCellValue(student.getEmail());
    }


    private void createStudentList(int indexOfEmailsSheet, String filePathOut, String optin, String level)  {
        int colNom = -1;
        int colPrenom = -1;
        int colGroupe = -1;
        int numRow = 1;

        generateHeader();
        for(Sheet sheet : getWorkbookIn()) {
            boolean found = false;
            Iterator<Row> rowIterator = sheet.rowIterator();
            Row rw = rowIterator.next();
            while ((!found) && (rowIterator.hasNext())) {
                if (Util.getInstance().existInRow(rw, "Prenom")) {
                    found = true;
                    colNom = Util.getInstance().column(rw, "Nom");
                    colPrenom = Util.getInstance().column(rw, "Prenom");
                    colGroupe = Util.getInstance().column(rw, "NG");
                } else if (Util.getInstance().existInRow(rw, "Prénom")) {
                    found = true;
                    colNom = Util.getInstance().column(rw, "Nom");
                    colPrenom = Util.getInstance().column(rw, "Prénom");
                    colGroupe = Util.getInstance().column(rw, "NG");
                } else {
                    rw = rowIterator.next();
                }
            }
            EmailFinder emailFinder = new EmailFinder(colNom, colPrenom, indexOfEmailsSheet, getEmailsWorkbook(), getWorkbookIn());

            while (rowIterator.hasNext()) {
                if (Util.getInstance().existInRow(rw, optin)) {
                    Student student = new Student();
                    student.setFirstName(rw.getCell(colNom).toString());
                    student.setLastName(rw.getCell(colPrenom).toString());
                    student.setLevel(level);
                    student.setPositionInWorkbookIn(rw.getRowNum());
                    emailFinder.setStudent(student);
                    emailFinder.getEmails(optin);
                    student.setEmail();
                    if (!student.hasEmail()) {
                        student.setPositionInWorkbookOut(numRow);
                        this.listOfStudentsWithoutEmail.add(student);
                    }
                    student.generateUsename();
                    rw.getCell(colGroupe).setCellType(CellType.STRING);
                    student.setGroupe(rw.getCell(colGroupe).toString());
                    student.createLastNameInMoodle();
                    student.setPassword(student.getLastName());
                    generateRow(numRow, student);
                    numRow++;

                }
                rw = rowIterator.next();
            }
        }
        File file = new File(filePathOut);
        saveUsersList(file);
    }


    @Override
    public String buildCSV(ArrayList<String> workbooksPaths)  {//
        String type;
        for (String workbooksPath : workbooksPaths) {
            if(workbooksPath.contains(".docx"))
            {
                workbooksPath = Util.getInstance().ConvertWordTableToExcel(workbooksPath);
            }
            File file = new File(workbooksPath);
            type = Util.getInstance().getFileType(file);
            if (type.equals("Solarite")) openWorkbookIn(workbooksPath);
            else openEmailWorkbook(workbooksPath);
        }
        String level = Util.getInstance().getLevel(getWorkbookIn());
        switch (level) {
            case "1CPI":
                createStudentList(0, "temp1CPI.xlsx", "CPI", "1CPI");
                return "temp1CPI.xlsx";
            case "2CPI":
                createStudentList(1, "temp2CPI.xlsx", "CPI", "2CPI");
                return "temp2CPI.xlsx";
            case "1CS":
                createStudentList(2, "tempCS.xlsx", "SC", "1CS");
                return "tempCS.xlsx";
            case "2CS-SIL":
                createStudentList(3, "temp2CS-SIL.xlsx", "SIL", "2CS-SIL");
                return "temp2CS-SIL.xlsx";
            case "2CS-SIT":
                createStudentList(4, "temp2CS-SIL.xlsx", "SIT", "2CS-SIT");
                return "temp2CS-SIL.xlsx";
            case "2CS-SIQ":
                createStudentList(5, "temp2CS-SIL.xlsx", "SIQ", "2CS-SIQ");
                return "temp2CS-SIQ.xlsx";
            case "3CS-SIL":
                createStudentList(6, "temp3CS-SIL.xlsx", "3CS-SIL", "");
                return "temp3CS-SIL.xlsx";
            case "3CS-SIT":
                createStudentList(7, "temp3CS-SIT.xlsx", "3CS-SIT", "");
                return "temp3CS-SIT.xlsx";
            case "3CS-SIQ":
                createStudentList(8, "temp3CS-SIQ.xlsx", "3CS-SIQ", "");
                return "temp3CS-SIQ.xlsx";
        }

        return null;
    }
}
