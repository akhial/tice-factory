package com.team33.model.csv.Students;

import com.team33.model.Util;
import com.team33.model.csv.UserFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Amine on 13/02/2017.
 */
public class AffectingStudentToCourseFormat extends UserFormat {

    private CourseFormat courseFormat;
    private ArrayList<Student> listOfStudentsWithoutEmail;
    private XSSFWorkbook workbookSecondSemester;
    private String level;
    private String optin;
    private String filePathOut;
    /*
    * This constructor must be used in case the user creates list of 2CS level
     */
    public AffectingStudentToCourseFormat(String workbookPathSecondSemester,String level, String optin,String filePathOut) throws IOException {
        this.courseFormat = new CourseFormat();
        this.courseFormat.openWrkbook();
        this.listOfStudentsWithoutEmail = new ArrayList<>();
        this.workbookSecondSemester = new XSSFWorkbook(new FileInputStream(new File(workbookPathSecondSemester)));
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

    public AffectingStudentToCourseFormat(String level, String optin,String filePathOut)  {
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

    public ArrayList<Student> getListOfStudentsWithoutEmail() {
        return listOfStudentsWithoutEmail;
    }

    private void generateHeader(String level,String optin)// gener le header ie ecrire dans la première ligne (username,fistname,lastname,email) -> le format accepté par moodle
    {

        for (int i = 0; i < (5+courseFormat.getNumberOfCourses(level,optin));i++)
        {
            this.getHeader().createCell(i);
        }

        this.getHeader().getCell(0).setCellValue("username");
        this.getHeader().getCell(1).setCellValue("password");
        this.getHeader().getCell(2).setCellValue("firstname");
        this.getHeader().getCell(3).setCellValue("lastname");
        this.getHeader().getCell(4).setCellValue("email");
        for(int i = 0; i < courseFormat.getNumberOfCourses(this.level,this.optin); i++)
        {
            this.getHeader().getCell(i+5).setCellValue("course"+(i+1));
        }
    }

    public void generateRow(int numRow, Student student)// générer une ligne cde fichier résultat contenant les coordonné d'un étudiant
    {
        Row rw = this.getWorkbookOut().getSheetAt(0).createRow(numRow);
        for (int i = 0; i < (5+courseFormat.getNumberOfCourses(student.getLevel(),student.getOptin()));i++) {
            rw.createCell(i);
        }
        rw.getCell(0).setCellValue(student.getUsername());
        rw.getCell(1).setCellValue(student.getPassword());
        rw.getCell(2).setCellValue(student.getFirstName());
        rw.getCell(3).setCellValue(student.getLastNameInMoodle());
        rw.getCell(4).setCellValue(student.getEmail());
        for(int i = 0; i < courseFormat.getNumberOfCourses(student.getLevel(),student.getOptin()); i++)
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

    public HashMap<String,ArrayList<String>> extractOptionalModules()
    {
        String str = null;
        int colGroupe = -1;
        String key = "";
        ArrayList<XSSFWorkbook> workbooks = new ArrayList<>();
        workbooks.add(getWorkbookIn());
        workbooks.add(workbookSecondSemester);
        HashMap<String,ArrayList<String>> optionalModules = new HashMap<String,ArrayList<String>>();
        for(XSSFWorkbook workbook : workbooks)
        {
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
                        if(!optionalModules.containsKey(key))
                        {
                            optionalModules.put(key,new ArrayList<>());
                        }
                        optionalModules.get(key).add(str);

                    }
                }
            }
        }

            return optionalModules;
    }

    private String nameOfEmailSheet()
    {
        if(this.level.equals("1CPI")) return "1cpi";
        else if(this.level.equals("2CPI")) return "2cpi";
        else if(this.level.equals("3CS")) return "3cs";
        else return this.level+this.optin;
    }

    private void createStudentList()  {
        int numRow = 1;

        generateHeader(level,optin);

        FileInformationExtractor extractor = new FileInformationExtractor(getWorkbookIn(),optin);
        ArrayList<Student> students = extractor.findStudents();
        HashMap<Student,Integer> studentHashMap  =  extractor.createStudentsHashMap();
        EmailFinder emailFinder = new EmailFinder(nameOfEmailSheet(),getEmailsWorkbook(),studentHashMap);

        HashMap<String,ArrayList<String>> optionalModules = null;
        if(level.equals("2CS")) optionalModules = extractOptionalModules();

        for(Student student : students)
        {
            student.setLevel(level);
            emailFinder.setStudent(student);
            emailFinder.getEmails();
            student.setStudentInformations();
            student.allocateCourses(this.courseFormat,optionalModules);
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
