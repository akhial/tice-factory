package com.team33.model.csv.Students;

import com.team33.model.Utilities.Util;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hamza on 15/04/2017.
 */
public class NonExistingStudentsGradesFormat extends GradesFormat {
    private HashMap<String, Student> students;

    public NonExistingStudentsGradesFormat(String level, String optin, String filePathOut) {
        super(level, optin, filePathOut);
    }

    private String nameOfEmailSheet()
    {
        String sheetName = "";
        if(this.getOptin().equals("CPI"))
        {
            sheetName = this.getLevel();
        }
        else sheetName = this.getLevel() + this.getOptin();

        for(Sheet sheet : getEmailsWorkbook())
        {
            if(sheetName.equalsIgnoreCase(sheet.getSheetName())) return sheet.getSheetName();
        }
        return null;
    }

    @Override
    protected void createStudentsList() throws IOException {

        int numRow = 1;
        generateHeader();

        FileInformationExtractor extractor = new FileInformationExtractor(getWorkbookIn(), getOptin());
        this.students = extractor.findStudents();

        HashMap<Student, Integer> studentHashMap = extractor.createStudentsHashMap();
        EmailFinder emailFinder = new EmailFinder(nameOfEmailSheet(), getEmailsWorkbook(), studentHashMap);

        for (Sheet sheet : getWorkbookIn()){
            ColumnsInformationBox box = new ColumnsInformationBox(sheet);
            box.extractInformationsFromFile();
            for(Row row : sheet)
            {
                if(Util.getInstance().existInRow(row,getOptin()))
                {
                    Student student = this.students.get(row.getCell(box.getColMatrin()).toString().replace("/",""));
                    student.setLevel(getLevel());
                    emailFinder.setStudent(student);
                    emailFinder.getEmails();
                    student.setStudentInformations();
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
