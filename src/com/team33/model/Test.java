package com.team33.model;

import com.team33.model.csv.CSVBuilder;
import com.team33.model.csv.Students.*;
import com.team33.model.csv.Students.Courses.CoursesStore;
import com.team33.model.csv.Students.Courses.UnExistingOptionException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Test {

    static ArrayList<String> listOfUsedEmails = new ArrayList<String>();
    public static void main(String[] args) throws IOException {

       ArrayList<String> workbooksPaths = new ArrayList<String>();

        workbooksPaths.add("liste email tous les etudiants.xlsx");
        workbooksPaths.add("Listes-Etudiants_2CPI_S1_2016-2017(1) (2).xlsx");
        GroupFormat studentFormat = new GroupFormat("2CPI","CPI","");
        CSVBuilder csvBuilder = new CSVBuilder(workbooksPaths,studentFormat,"C:/Users/hamza/IdeaProjects/team-33");
        csvBuilder.buildCSV();
        String email = null;
        for(Student student : studentFormat.getListOfStudentsWithoutEmail())
        {
            System.out.print(student);

            if(student.getListOfEmails().isEmpty())
            { //poopup 1
                System.out.println(" : ");
                Scanner scanner = new Scanner(System.in);
                email = scanner.nextLine();
                student.setEmail(email);

            }else
            {
                deleteUsedEmails(student);
                System.out.println(student.getListOfEmails().size());
                student.tryToSetEmail();
                if(!student.hasEmail())
                {
                    System.out.println(student.getListOfEmails());
                    Scanner scanner = new Scanner(System.in);
                    int i = scanner.nextInt();
                    email =(String) student.getListOfEmails().get(i);
                    student.setEmail(email);
                }
            }
            student.generateUsename();
            studentFormat.updateRow(student.getPositionInWorkbookOut(),student);
            listOfUsedEmails.add(email);
        }
                studentFormat.saveUsersList(new File(csvBuilder.getTempPath()));
        csvBuilder.convertToCSV();
       /* CoursesStore coursesStore = new CoursesStore();
        //coursesStore.saveChanges();
        /*coursesStore.load();
        coursesStore.getCycleSuperieur().getCS2().ajouterModuleCommun("ALOG","Arcihetctures logicielles");
        coursesStore.getCycleSuperieur().getCS2().ajouterModuleCommun("COM","Compilation");
        coursesStore.saveChanges();
        System.out.println(coursesStore.getCycleSuperieur().getCS2().getSemestre1("SIT"));*/
    }
    public static void deleteUsedEmails(Student student)
    {
        for(int i = 0; i < student.getListOfEmails().size();i++)
            if (listOfUsedEmails.contains((String) student.getListOfEmails().get(i)))
            {
                student.getListOfEmails().remove(student.getListOfEmails().get(i));
            }
    }
}
