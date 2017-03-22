package com.team33.model.csv.Students;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Amine on 13/02/2017.
 */
    public class CourseFormat  {

    private String coursesFile;
    private XSSFWorkbook workbook;
    private File file;

    public void openWrkbook()
    {
        try {
            this.workbook = new XSSFWorkbook(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveChanges()
    {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            this.workbook.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CourseFormat()
    {
        this.coursesFile = "courses.xlsx";
        this.file = new File(getCoursesFile());
    }


    public String getCoursesFile() {
        return coursesFile;
    }


    public ArrayList<String> getListOfCoursesSemestre(String sheetName,int semestre)
    {
        ArrayList<String> listOfCourses = new ArrayList<>();
        Sheet sheet = this.workbook.getSheet(sheetName);
        for(int i = 1; i <= sheet.getLastRowNum();i++)
        {
            Row rw = sheet.getRow(i);
            if(rw.getCell(semestre-1) != null)
            listOfCourses.add(rw.getCell(semestre-1).toString());
        }
        return listOfCourses;
    }

    public ArrayList<String> getListOfCourses(String sheetName)
    {
        ArrayList<String> listOfCourses = new ArrayList<>();
        Sheet sheet = this.workbook.getSheet(sheetName);
        for(Row row : sheet)
        {
            listOfCourses.add(row.getCell(0).toString());
        }
        return listOfCourses;
    }

    public ArrayList<String> getListOfCommonCourses()
    {
        return getListOfCourses("CoursCommun");
    }

    public ArrayList<String> getListOf3CSCourses()
    {
        return getListOfCourses("3CS");
    }

    public ArrayList<String> getListOfCourses(String level,String optin)
    {
        String sheetName = sheetName(level,optin);
        ArrayList list = new ArrayList();
        if(level.equals("3CS"))
        {
            list.addAll(getListOf3CSCourses());
        }
        else
        {
            list.addAll(getListOfCoursesSemestre(sheetName,1));
            list.addAll(getListOfCoursesSemestre(sheetName,2));
            if(level.equals("2CS"))
            {
                list.addAll(getListOfCommonCourses());
            }
        }

        return list;
    }

    private String sheetName(String level,String optin)
    {
        if(optin.equals("CPI"))
            return level;
        else if(level.equals("3CS"))
            return level;
        else if(level.equals("2CS"))
        {
            return level + "-" + optin;
        }
        return level;
    }
    /*
    * Add a given course for a given level (1CPI OR 2CPI) in a given semester
     */

    public void addCourse(String courseShortName,String level,String optin,int semestre)
    {
        boolean found = false;
        String sheetName = sheetName(level,optin);
        Sheet sheet  = this.workbook.getSheet(sheetName);
        Row rw;
        int i = 1;
        while (!found)
        {
            rw = sheet.getRow(i);
            if (rw == null)
            {
              rw = sheet.createRow(i);
            }
            if(rw.getCell(semestre-1) == null)
            {
                rw.createCell(semestre-1).setCellValue(courseShortName);
                found = true;
            }
            else i++;
        }
    }
    /*
    *Used to add a course for 3CS or a Common Course for 2CS
     */
    public void addCourse(String courseShortName,String sheetName)
    {
        Sheet sheet = this.workbook.getSheet(sheetName);
        boolean added = false;
        int i = 0;
        while (!added)
        {
            Row rw = sheet.getRow(i);
            if(rw == null)
            {
                rw = sheet.createRow(i);
                rw.createCell(0).setCellValue(courseShortName);
                added = true;
            }
        }
    }

    public int getNumberOfCourses(String level,String optin)
    {
        switch (level) {
            case "2CS":
                return (getListOfCourses(level, optin).size() + getListOfCommonCourses().size() + 2);
            case "3CS":
                return getListOf3CSCourses().size();
            default:
                return getListOfCourses(level, optin).size();
        }
    }

}
