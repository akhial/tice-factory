package com.team33.model.csv;

import com.team33.model.Util;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Amine on 13/02/2017.
 */
public class AffectingStudentToCourseFormat extends UserFormat {

    private CourseFormat courseFormat;
    private ArrayList<Student> listOfStudentsWithoutEmail;


    public AffectingStudentToCourseFormat() {
        this.courseFormat = new CourseFormat();
        this.courseFormat.openWrkbook();
        this.listOfStudentsWithoutEmail = new ArrayList<>();

    }

    public ArrayList<Student> getListOfStudentsWithoutEmail() {
        return listOfStudentsWithoutEmail;
    }

    private void generateHeader(String level)// gener le header ie ecrire dans la première ligne (username,fistname,lastname,email) -> le format accepté par moodle
    {

        for (int i = 0; i < (5+courseFormat.getNumberOfCourses(level));i++)
        {
            this.getHeader().createCell(i);
        }

        this.getHeader().getCell(0).setCellValue("username");
        this.getHeader().getCell(1).setCellValue("password");
        this.getHeader().getCell(2).setCellValue("firstname");
        this.getHeader().getCell(3).setCellValue("lastname");
        this.getHeader().getCell(4).setCellValue("email");
        for(int i = 0; i < courseFormat.getNumberOfCourses(level); i++)
        {
            this.getHeader().getCell(i+5).setCellValue("course"+(i+1));
        }
    }

    public void generateRow(int numRow, Student student)// générer une ligne cde fichier résultat contenant les coordonné d'un étudiant
    {
        Row rw = this.getWorkbookOut().getSheetAt(0).createRow(numRow);
        for (int i = 0; i < (5+courseFormat.getNumberOfCourses(student.getLevel()));i++) {
            rw.createCell(i);
        }
        rw.getCell(0).setCellValue(student.getUsername());
        rw.getCell(1).setCellValue(student.getPassword());
        rw.getCell(2).setCellValue(student.getFirstName());
        rw.getCell(3).setCellValue(student.getLastNameInMoodle());
        rw.getCell(4).setCellValue(student.getEmail());
        for(int i = 0; i < courseFormat.getNumberOfCourses(student.getLevel()); i++)
        {
            rw.getCell(i+5).setCellValue(student.getCourses().get(i));
        }
    }

    private void createStudentList(int indexOfEmailsSheet, String filePathOut, String optin, String level)  {
        int colNom = -1;
        int colPrenom = -1;
        int colGroupe = -1;
        int numRow = 1;

        generateHeader(level);
        for(Sheet sheet : this.getWorkbookIn()) {
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
            EmailFinder emailFinder = new EmailFinder(colNom, colPrenom, indexOfEmailsSheet, this.getEmailsWorkbook(), this.getWorkbookIn());

            while (rowIterator.hasNext()) {
                if (Util.getInstance().existInRow(rw, optin)) {
                    Student student = new Student();
                    student.setFirstName(rw.getCell(colNom).toString());
                    student.setLastName(rw.getCell(colPrenom).toString());
                    student.setLevel(level);
                    student.setOptin(optin);
                    student.setPositionInWorkbookIn(rw.getRowNum());
                    emailFinder.setStudent(student);
                    emailFinder.getEmails(optin);
                    student.tryToSetEmail();
                    if (!student.hasEmail()) {
                        student.setPositionInWorkbookOut(numRow);
                        this.listOfStudentsWithoutEmail.add(student);
                    }
                    student.generateUsename();
                    student.setCourses(courseFormat.getListOfCourses(level));
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
