package com.team33.model.csv.Students;

import com.team33.model.Utilities.Util;
import com.team33.model.csv.CSVFormat;
import com.team33.model.csv.UserFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Amine on 13/02/2017.
 */
public abstract class GradesFormat extends UserFormat implements CSVFormat {

    private String level;
    private String optin;
    private String filePathOut;
    private ArrayList<Student> listOfStudentsWithoutEmail;

    public GradesFormat(String level, String optin,String filePathOut) {
        this.level = level;
        this.optin = optin;
        this.listOfStudentsWithoutEmail = new ArrayList<>();
        if(filePathOut == null || filePathOut.equals(""))
        {
            if(optin.equals("CPI") || level.equals("1CS"))
                filePathOut = level + "grades";
            else filePathOut = level + optin +"grades";
        }
        this.filePathOut = filePathOut+".xlsx";
    }

    public String getLevel() {
        return level;
    }

    public String getOptin() {
        return optin;
    }

    public ArrayList<Student> getListOfStudentsWithoutEmail() {
        return listOfStudentsWithoutEmail;
    }

    public String getFilePathOut() {
        return filePathOut;
    }

    public void generateHeader() {
        this.getHeader().createCell(0).setCellValue("username");
        boolean found = false;
        Iterator<Row> rowIterator = this.getWorkbookIn().getSheetAt(0).rowIterator();
        Row rw = rowIterator.next();
        while ((rowIterator.hasNext()) && (!found)) {
            if (Util.getInstance().existInRow(rw, "Matrin")) {
                found = true;
                int i = 1;
                for(Cell cell : rw)
                {
                    this.getHeader().createCell(i).setCellValue(cell.toString());
                    i++;
                }
            }else rw = rowIterator.next();
        }
    }

    protected void generateRow(Row rowIn,int numRow,Student student){
        Row rw = this.getWorkbookOut().getSheetAt(0).createRow(numRow);
        rw.createCell(0).setCellValue(student.getUsername());
        int i = 1;
        for(Cell cell : rowIn){
            cell.setCellType(CellType.STRING);
            rw.createCell(i).setCellValue(cell.toString());
            i++;
        }
    }

    public void updateRow(int numRow,Student student)
    {
        getWorkbookOut().getSheetAt(0).getRow(numRow).createCell(0).setCellValue(student.getUsername());
    }
    protected abstract void createStudentsList() throws IOException;
    @Override
    public String buildCSV(ArrayList<String> workbooksPaths) throws IOException {
        String type;
        FileInformationExtractor extractor = new FileInformationExtractor();
        for (String workbooksPath : workbooksPaths) {
            if(workbooksPath.contains(".docx"))
            {
                workbooksPath = extractor.ConvertWordTableToExcel(workbooksPath,this.optin);
            }
            File file = new File(workbooksPath);
            type = extractor.getFileType(file);
            if (type.equals("Solarite")) openWorkbookIn(workbooksPath);
            else openEmailWorkbook(workbooksPath);
        }
            createStudentsList();

        return filePathOut;
    }
}
