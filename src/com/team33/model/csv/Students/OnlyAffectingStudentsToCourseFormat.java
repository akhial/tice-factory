package com.team33.model.csv.Students;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hamza on 15/04/2017.
 */
public class OnlyAffectingStudentsToCourseFormat extends AffectingStudentToCourseFormat {
    public OnlyAffectingStudentsToCourseFormat(String level, String optin, String filePathOut) {
        super(level, optin, filePathOut);
    }

    protected void createStudentList() throws IOException {
        int numRow = 1;

        FileInformationExtractor extractor = new FileInformationExtractor(getWorkbookIn(), getOptin());
        ExistingStudents existingStudents = new ExistingStudents(getLevel(),getOptin());
        existingStudents.loadExistingStudents();
        HashMap<String,Student> students = existingStudents.getStudents();

        HashMap<String,ArrayList<String>> optionalModules = null;
        if(getLevel().equals("2CS")) optionalModules = extractor.extractOptionalModules();
        generateHeader(maxNumberOfOptionalModules(optionalModules));
        for (Map.Entry<String,Student> entry : students.entrySet())
        {
            Student student = entry.getValue();
            student.allocateCourses(this.getCourseFormat(),optionalModules);
            if(!student.hasEmail())
            {
                student.setPositionInWorkbookOut(numRow);
                this.getListOfStudentsWithoutEmail().add(student);
            }
            generateRow(numRow,student);
            numRow++;
        }
        File file = new File(getFilePathOut());
        saveUsersList(file);
    }
}
