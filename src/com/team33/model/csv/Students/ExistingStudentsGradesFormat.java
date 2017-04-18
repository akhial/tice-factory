package com.team33.model.csv.Students;

import com.team33.model.Utilities.Util;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by hamza on 15/04/2017.
 */
public class ExistingStudentsGradesFormat extends GradesFormat {


    private HashMap<String, Student> students;

    public ExistingStudentsGradesFormat(String level, String optin, String filePathOut) {
        super(level, optin, filePathOut);
    }

    @Override
    protected void createStudentsList() throws IOException {
        int numRow = 1;
        generateHeader();

        ExistingStudents existingStudents = new ExistingStudents(getLevel(),getOptin());
        existingStudents.loadExistingStudents();
        this.students = existingStudents.getStudents();

        for (Sheet sheet : getWorkbookIn()){
            ColumnsInformationBox box = new ColumnsInformationBox(sheet);
            box.extractInformationsFromFile();
            for(Row row : sheet)
            {
                if(Util.getInstance().existInRow(row,getOptin()))
                {
                    Student student = this.students.get(row.getCell(box.getColMatrin()).toString().replace("/",""));
                    if (!student.hasEmail()) {
                        student.setPositionInWorkbookOut(numRow);
                        this.getListOfStudentsWithoutEmail().add(student);
                    }
                    generateRow(row,numRow,student);
                    numRow++;
                }
            }
        }
        File file = new File(getFilePathOut());
        saveUsersList(file);

    }
}
