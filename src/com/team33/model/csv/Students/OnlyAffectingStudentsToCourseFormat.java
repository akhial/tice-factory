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
    private String csvFileName;
    public OnlyAffectingStudentsToCourseFormat(String level, String optin, String filePathOut,String csvFileName) {
        super(level, optin, filePathOut);
        this.csvFileName = csvFileName;
    }

    protected void createStudentList() throws IOException {
        int numRow = 1;

        FileInformationExtractor extractor = new FileInformationExtractor(getWorkbookIn(), getOptin());
        ExistingStudents existingStudents = new ExistingStudents(this.csvFileName);
        existingStudents.loadExistingStudents();
        HashMap<String,Student> existingStudent = existingStudents.getStudents();
        HashMap<String,Student> students = extractor.findStudents();

        HashMap<String,ArrayList<String>> optionalModules = null;
        if(getLevel().equals("2CS")) optionalModules = extractor.extractOptionalModules();
        generateHeader(maxNumberOfOptionalModules(optionalModules));
        for (Map.Entry<String,Student> entry : students.entrySet())
        {
            Student student = entry.getValue();
            Student exisiting = existingStudent.get(student.getIdnumber());
            student.setUsername(exisiting.getUsername());
            student.setPassword(exisiting.getPassword());
            student.setLastNameInMoodle(exisiting.getLastNameInMoodle());
            student.setFirstName(exisiting.getFirstName());
            student.setEmail(exisiting.getEmail());
            student.setLevel(getLevel());
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
