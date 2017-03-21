package com.team33.model.csv.Students;

import com.team33.model.Util;
import com.team33.model.csv.UserFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Amine on 13/02/2017.
 */
public class AffectingStudentToCourseFormat extends UserFormat {

    private CourseFormat courseFormat;
    private ArrayList<Student> listOfStudentsWithoutEmail;
    private XSSFWorkbook workbookSecondeSemester;
    private HashMap<Student, Integer> studentHashMap;

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

    private String extractOptionalModule(Row row)
    {
        String str = "";
        for(Cell cell : row)
        {
            str = str + cell.toString();
        }
        str = str.replace("Modules optionnels","");
        str = str.replace(":","");
        str = str.replace(" ","");
        return str;
    }

    public HashMap<String,String> extractOptionalModules(XSSFWorkbook workbook)
    {
        String str = null;
        int colGroupe = -1;
        String key = "";
        HashMap<String,String> optionalModules = new HashMap<String,String>();
        for(Sheet sheet : workbook)
        {
            System.out.println(sheet.getSheetName());
            for (Row row : sheet) {
                if (Util.getInstance().rowContains(row, "Modules optionnels")) {
                    str = extractOptionalModule(row);
                }
                if (Util.getInstance().existInRow(row, "NG")) {
                    colGroupe = Util.getInstance().column(row, "NG");
                    Row rw = sheet.getRow(row.getRowNum() + 1);
                    rw.getCell(colGroupe).setCellType(CellType.STRING);
                    key = rw.getCell(colGroupe).getStringCellValue();
                    optionalModules.put(key, str);

                }
            }
        }
            return optionalModules;
    }

    private void createStudentList(int indexOfEmailsSheet, String filePathOut, String optin, String level)  {
        int numRow = 1;
        Sheet sheet = getWorkbookIn().getSheetAt(0);

        generateHeader();

        ColumnsInformationBox box = new ColumnsInformationBox(sheet);
        box.extractInformationsFromFile();
        FileInformationExtractor extractor = new FileInformationExtractor(box,sheet,optin);
        ArrayList<Student> students = extractor.findStudents();
        this.studentHashMap =  extractor.createStudentsHashMap();
        EmailFinder emailFinder = new EmailFinder(indexOfEmailsSheet,getEmailsWorkbook(),this.studentHashMap);


        for(Student student : students)
        {
            student.setLevel(level);
            emailFinder.setStudent(student);
            emailFinder.getEmails();
            student.setStudentInformations();
            if(!student.hasEmail())
            {
                student.setPositionInWorkbookOut(numRow);
                this.listOfStudentsWithoutEmail.add(student);
            }
            generateRow(numRow,student);
            numRow++;
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
