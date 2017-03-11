package com.team33.model.csv;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Amine on 13/02/2017.
 */
public class AssigningTeacherToCourseFormat extends UserFormat implements CSVFormat {

    public void generateHeader()
    {
        int j = 1;
        for (int i = 4; i < 12;i += 2)
        {
            this.getHeader().createCell(i);
            this.getHeader().createCell(i+1);
            this.getHeader().getCell(i).setCellValue("course"+j);
            this.getHeader().getCell(i+1).setCellValue("type"+j);
            j++;
        }
    }


    @Override
    public String buildCSV(String... workbooksPaths) {
        String workbookPath = workbooksPaths[0];
        String emailWorkbookPath = workbooksPaths[1];
        TeacherFormat teacherFormat = new TeacherFormat();
        String tempFile = teacherFormat.buildCSV(workbookPath,emailWorkbookPath);
        openWorkbookIn(tempFile);
        File file = new File("Assigning Teachers.xlsx");
        try {
            setWorkbookOut(new XSSFWorkbook(new File(tempFile)));
            generateHeader();
            saveUsersList(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }



        return file.getPath() ;
    }
}
