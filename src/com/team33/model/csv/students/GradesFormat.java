package com.team33.model.csv.students;

import com.team33.model.Utilities.Util;
import com.team33.model.csv.CSVFormat;
import com.team33.model.csv.UserFormat;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Amine on 13/02/2017.
 */
public class GradesFormat extends UserFormat implements CSVFormat, StudentInterface {

    private String level;
    private String optin;
    private String filePathOut;
    private ArrayList<Student> listOfStudentsWithoutEmail;
    private HashMap<String, Student> students;

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

    private boolean isANumber(String str){
        boolean isNumber = true;
        if(str.equals("") || str == null) return false;
        str = str.replace(".","");
        str = str.replace(",","");
        char cell[] = str.toCharArray();
        int i = 0;
        for(char c : cell){
            if(!Character.isDigit(c)) return false;
        }
        return isNumber;
    }

    private void generateRow(Row rowIn,int numRow,Student student,ColumnsInformationBox box){
        Row rw = this.getWorkbookOut().getSheetAt(0).createRow(numRow);
        rw.createCell(0).setCellValue(student.getUsername());
        int i = 1;
        for(Cell cell : rowIn){
            cell.setCellType(CellType.STRING);
            String cellValue = cell.toString();
            if(cell.getColumnIndex() != box.getColMatrin() && cell.getColumnIndex() != box.getColNom() && cell.getColumnIndex() != box.getColPrenom() && cell.getColumnIndex() != box.getColOptin() && cell.getColumnIndex() != box.getColAnetin() && cell.getColumnIndex() != box.getColNS())
            {
            if (isANumber(cellValue)) rw.createCell(i).setCellValue(cell.toString());
            else rw.createCell(i).setCellValue("0");
            } else rw.createCell(i).setCellValue(cell.toString());
            i++;
        }
    }

    public void updateRow(int numRow,Student student)
    {
        getWorkbookOut().getSheetAt(0).getRow(numRow).createCell(0).setCellValue(student.getUsername());
    }

    protected String nameOfEmailSheet()
    {
        String sheetName = "";
        if(this.optin.equals("CPI") || this.optin.equals("SC"))
        {
            sheetName = this.level;
        }
        else sheetName = this.level + this.optin;

        for(Sheet sheet : getEmailsWorkbook())
        {
            if(sheetName.equalsIgnoreCase(sheet.getSheetName())) return sheet.getSheetName();
        }
        return null;
    }
    private void createStudentsList() throws IOException {

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
                    generateRow(row,numRow,student,box);
                    numRow++;
                }
            }
        }
        File file = new File(getFilePathOut());
        saveUsersList(file);
    }

    @Override
    public String buildCSV(ArrayList<String> workbooksPaths) throws IOException, InvalidFormatException {
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
