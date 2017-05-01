package com.team33.model.csv.students;

import com.team33.model.csv.students.courses.CoursesStore;
import com.team33.model.csv.UserFormat;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Amine on 13/02/2017.
 */
public class AffectingStudentToCourseFormat extends UserFormat implements StudentInterface {

    private CoursesStore courseFormat;
    private ArrayList<Student> listOfStudentsWithoutEmail;
    private String level;
    private String optin;
    private String filePathOut;


    public AffectingStudentToCourseFormat(String level, String optin,String filePathOut) throws IOException {
        this.courseFormat = new CoursesStore();
        this.courseFormat.load();
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

    public CoursesStore getCourseFormat() {
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

        for (int i = 0; i < (5 + courseFormat.getNumberOfCourses(this.level,this.optin) + numberOfOptionalModules);i++)
        {
            this.getHeader().createCell(i);
        }

        this.getHeader().getCell(0).setCellValue("username");
        this.getHeader().getCell(1).setCellValue("password");
        this.getHeader().getCell(2).setCellValue("firstname");
        this.getHeader().getCell(3).setCellValue("lastname");
        this.getHeader().getCell(4).setCellValue("email");
        for(int i = 0; i < courseFormat.getNumberOfCourses(this.level,this.optin)+numberOfOptionalModules; i++)
        {
            this.getHeader().getCell(i+5).setCellValue("course"+(i+1));
        }
    }

    protected void generateRow(int numRow, Student student)// générer une ligne cde fichier résultat contenant les coordonné d'un étudiant
    {
        Row rw = this.getWorkbookOut().getSheetAt(0).createRow(numRow);
        for (int i = 0; i < 5;i++) {
            rw.createCell(i);
        }
        rw.getCell(0).setCellValue(student.getUsername());
        rw.getCell(1).setCellValue(student.getPassword());
        rw.getCell(2).setCellValue(student.getFirstName());
        rw.getCell(3).setCellValue(student.getLastNameInMoodle());
        rw.getCell(4).setCellValue(student.getEmail());
        for(int i = 0; i < student.numberOfCourses(); i++)
        {

            rw.createCell(i+5).setCellValue(student.getCourses().get(i));
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
        if(this.optin.equals("CPI") || this.optin.equals("SC"))
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

    protected void createStudentList() throws IOException {
        int numRow = 1;
        FileInformationExtractor extractor = new FileInformationExtractor(getWorkbookIn(), getOptin());
        HashMap<String,Student> students = extractor.findStudents();
        HashMap<Student,Integer> studentHashMap  =  extractor.createStudentsHashMap();
        EmailFinder emailFinder = new EmailFinder(nameOfEmailSheet(),getEmailsWorkbook(),studentHashMap);
        HashMap<String,ArrayList<String>> optionalModules = null;
        if(getLevel().equals("2CS")) optionalModules = extractor.extractOptionalModules();
        generateHeader(maxNumberOfOptionalModules(optionalModules));
        for (Map.Entry<String,Student> entry : students.entrySet())
        {
            Student student = entry.getValue();
            student.setLevel(getLevel());
            emailFinder.setStudent(student);
            emailFinder.getEmails();
            student.setStudentInformations();
            student.allocateCourses(this.getCourseFormat(),optionalModules);
            if(!student.hasEmail()) {
                student.setPositionInWorkbookOut(numRow);
                this.getListOfStudentsWithoutEmail().add(student);
            }
            generateRow(numRow,student);
            numRow++;
        }
        File file = new File(getFilePathOut());
        saveUsersList(file);
    }
    @Override
    public String buildCSV(ArrayList<String> workbooksPaths) throws IOException, InvalidFormatException {
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
