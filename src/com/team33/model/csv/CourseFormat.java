package com.team33.model.csv;

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

    public void setCoursesFile(String coursesFile) {
        this.coursesFile = coursesFile;
    }

    public String getCoursesFile() {
        return coursesFile;
    }


    public ArrayList<String> getListOfCoursesSemestre(String level,int semestre)
    {
        ArrayList<String> listOfCourses = new ArrayList<>();
        Sheet sheet = this.workbook.getSheet(level);
        for(int i = 1; i <= sheet.getLastRowNum();i++)
        {
            Row rw = sheet.getRow(i);
            if(rw.getCell(semestre-1) != null)
            listOfCourses.add(rw.getCell(semestre-1).toString());
        }
        return listOfCourses;
    }

    public ArrayList<String> getListOfCourses(String level)
    {
        ArrayList list = new ArrayList(getListOfCoursesSemestre(level,1));
        list.addAll(getListOfCoursesSemestre(level,2));
        return list;
    }

    public void addCourse(String courseShortName,String level,int semestre)
    {
        boolean found = false;
        Sheet sheet  = this.workbook.getSheet(level);
        Row rw = sheet.getRow(0);
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

    public int getNumberOfCourses(String level)
    {
        Sheet sheet  = this.workbook.getSheet(level);
        if(level.equals("2CS"))
        {
            return (getListOfCourses(level).size() + 1);
        }
        else
        {
            return getListOfCourses(level).size();
        }
    }

}
