package com.team33.model.csv.Students;

import com.team33.model.Utilities.Util;
import com.team33.model.csv.UserFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.lang.Math;
/**
 * Created by Amine on 13/02/2017.
 */
public abstract class AffectingStudentToCourseFormat extends UserFormat {

    private CourseFormat courseFormat;
    private ArrayList<Student> listOfStudentsWithoutEmail;
    private String level;
    private String optin;
    private String filePathOut;


    public AffectingStudentToCourseFormat(String level, String optin,String filePathOut) throws IOException {
        this.courseFormat = new CourseFormat();
        this.courseFormat.openWrkbook();
        this.listOfStudentsWithoutEmail = new ArrayList<>();
        this.level = level;
        this.optin = optin;
        if(filePathOut == null || filePathOut.equals(""))
        {
            if(optin.equals("CPI") || level.equals("1CS"))
                filePathOut = level + "courses";
            else filePathOut = level + optin +"courses";
        }
        this.filePathOut = filePathOut+".xlsx";
    }

    public CourseFormat getCourseFormat() {
        return courseFormat;
    }

    public String getLevel() {
        return level;
    }

    public String getOptin() {
        return optin;
    }

    public String getFilePathOut() {
        return filePathOut;
    }



    public ArrayList<Student> getListOfStudentsWithoutEmail() {
        return listOfStudentsWithoutEmail;
    }

    protected void generateHeader(int numberOfOptionalModules)// gener le header ie ecrire dans la première ligne (username,fistname,lastname,email) -> le format accepté par moodle
    {

        for (int i = 0; i < (6 + courseFormat.getNumberOfCourses(this.level,this.optin) + numberOfOptionalModules);i++)
        {
            this.getHeader().createCell(i);
        }

        this.getHeader().getCell(0).setCellValue("username");
        this.getHeader().getCell(1).setCellValue("password");
        this.getHeader().getCell(2).setCellValue("firstname");
        this.getHeader().getCell(3).setCellValue("lastname");
        this.getHeader().getCell(4).setCellValue("email");
        this.getHeader().createCell(5).setCellValue("idnumber");
        for(int i = 0; i < courseFormat.getNumberOfCourses(this.level,this.optin)+numberOfOptionalModules; i++)
        {
            this.getHeader().getCell(i+6).setCellValue("course"+(i+1));
        }
    }

    protected void generateRow(int numRow, Student student)// générer une ligne cde fichier résultat contenant les coordonné d'un étudiant
    {
        Row rw = this.getWorkbookOut().getSheetAt(0).createRow(numRow);
        for (int i = 0; i < 6;i++) {
            rw.createCell(i);
        }
        rw.getCell(0).setCellValue(student.getUsername());
        rw.getCell(1).setCellValue(student.getPassword());
        rw.getCell(2).setCellValue(student.getFirstName());
        rw.getCell(3).setCellValue(student.getLastNameInMoodle());
        rw.getCell(4).setCellValue(student.getEmail());
        rw.createCell(5).setCellValue(student.getIdnumber());
        for(int i = 0; i < student.numberOfCourses(); i++)
        {

            rw.createCell(i+6).setCellValue(student.getCourses().get(i));
        }
    }

    public void updateRow(int numRow,Student student)
    {
        getWorkbookOut().getSheetAt(0).getRow(numRow).getCell(0).setCellValue(student.getUsername());
        getWorkbookOut().getSheetAt(0).getRow(numRow).getCell(4).setCellValue(student.getEmail());
    }

    protected int maxNumberOfOptionalModules(HashMap<String, ArrayList<String>> optionalModules)
    {
        int max = 0;
        if(optionalModules!=null)
        {
            Set<String> keySet = optionalModules.keySet();
            for(String key : keySet)
            {
               max = java.lang.Math.max(max,optionalModules.get(key).size());
            }
        }
        return max;
    }

    protected String nameOfEmailSheet()
    {
        String sheetName = "";
        if(this.optin.equals("CPI"))
        {
            sheetName = this.level;
        }
        else sheetName = this.level + this.optin;

        for(Sheet sheet : getEmailsWorkbook())
        {
            if(sheetName.equalsIgnoreCase(sheet.getSheetName())) return sheet.getSheetName();
        }
        return null;
    }

    abstract protected void createStudentList() throws IOException;

    @Override
    public String buildCSV(ArrayList<String> workbooksPaths) throws IOException {
        // WorkbooksPaths should contain only list of first semester and list of e-mails

        String type;
        FileInformationExtractor extractor = new FileInformationExtractor();
        for (String workbooksPath : workbooksPaths) {
            if(workbooksPath.contains(".docx"))
            {

                workbooksPath = extractor.ConvertWordTableToExcel(workbooksPath,this.optin);
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
