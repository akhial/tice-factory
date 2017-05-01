package com.team33.model.csv.students;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hamza on 23/04/2017.
 */
public class AffectingIntershipStudentToCourseFormat extends AffectingStudentToCourseFormat {

    public AffectingIntershipStudentToCourseFormat(String level, String optin, String filePathOut) throws IOException {
        super(level, optin, filePathOut);
    }

    protected void createStudentList() throws IOException {

        Long startTime = System.currentTimeMillis();
        System.out.println("Création de la liste ***");
        int numRow = 1;

        generateHeader(0);

        ColumnsInformationBox box = new ColumnsInformationBox(getWorkbookIn().getSheetAt(0));
        box.extractInformationsFromFile();
        FileInformationExtractor extractor = new FileInformationExtractor(getWorkbookIn(),getOptin());
        HashMap<String,ArrayList<Student>> students = extractor.findIntershipStudents();
        HashMap<Student, Integer> studentHashMap = extractor.createStudentsHashMap();
        EmailFinder emailFinder = new EmailFinder(nameOfEmailSheet(),getEmailsWorkbook(),studentHashMap);

        for(Map.Entry<String,ArrayList<Student>> entry : students.entrySet()) {
            ArrayList<Student> binom = entry.getValue();
            for(Student student : binom){
                student.setLevel(getLevel());
                emailFinder.setStudent(student);
                emailFinder.getEmails();
                student.setStudentInformations();
                student.allocateCourses(getCourseFormat(),null);
                if(!student.hasEmail())
                {
                    student.setPositionInWorkbookOut(numRow);
                    this.getListOfStudentsWithoutEmail().add(student);
                }
                generateRow(numRow,student);
                numRow++;
            }

        }
        File file = new File(getFilePathOut());
        saveUsersList(file);

        System.out.println("Temps d'execution : " + (System.currentTimeMillis()-startTime));


    }
}
