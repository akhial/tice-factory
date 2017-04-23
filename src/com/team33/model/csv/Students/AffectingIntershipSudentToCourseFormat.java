package com.team33.model.csv.Students;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hamza on 23/04/2017.
 */
public class AffectingIntershipSudentToCourseFormat extends AffectingStudentToCourseFormat {

    public AffectingIntershipSudentToCourseFormat(String level, String optin, String filePathOut) throws IOException {
        super(level, optin, filePathOut);
    }

    protected void createStudentList()  {

        Long startTime = System.currentTimeMillis();
        System.out.println("Création de la liste ***");
        int numRow = 1;

        generateHeader(0);

        ColumnsInformationBox box = new ColumnsInformationBox(getWorkbookIn().getSheetAt(0));
        box.extractInformationsFromFile();
        FileInformationExtractor extractor = new FileInformationExtractor(getWorkbookIn(),getOptin());
        HashMap<String,Student[]> students = extractor.findIntershipStudents();
        HashMap<Student, Integer> studentHashMap = extractor.createStudentsHashMap();
        EmailFinder emailFinder = new EmailFinder(nameOfEmailSheet(),getEmailsWorkbook(),studentHashMap);

        for(Map.Entry<String,Student[]> entry : students.entrySet()) {
            Student[] binom = entry.getValue();
            for(int i = 0; i < 2; i++){
                binom[i].setLevel(getLevel());
                emailFinder.setStudent(binom[i]);
                emailFinder.getEmails();
                binom[i].setStudentInformations();
                binom[i].allocateCourses(getCourseFormat(),null);
                if(!binom[i].hasEmail())
                {
                    binom[i].setPositionInWorkbookOut(numRow);
                    this.getListOfStudentsWithoutEmail().add(binom[i]);
                }
                generateRow(numRow,binom[i]);
                numRow++;
            }

        }
        File file = new File(getFilePathOut());
        saveUsersList(file);

        System.out.println("Temps d'execution : " + (System.currentTimeMillis()-startTime));


    }
}
