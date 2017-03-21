package com.team33.model.csv.Students;

import com.team33.model.Util;
import com.team33.model.csv.CSVFormat;
import com.team33.model.csv.UserFormat;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.*;
import java.net.URI;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by Amine on 13/02/2017.
 */

public class StudentFormat extends UserFormat implements CSVFormat {


    private ArrayList<Student> listOfStudentsWithoutEmail;
    private  HashMap<Student,Integer> studentHashMap = new HashMap<>();
    private ArrayList<Student> listeEtudiants = new ArrayList<>();
    public ArrayList<Student> getListOfStudentsWithoutEmail() {
        return listOfStudentsWithoutEmail;
    }

    public StudentFormat() {
        this.listOfStudentsWithoutEmail = new ArrayList<Student>();
    }


    /*
     *Méthodes utilitaires
     */


    public void generateRow(int numRow, Student student)// générer une ligne cde fichier résultat contenant les coordonné d'un étudiant
    {
        Row rw = getWorkbookOut().getSheetAt(0).createRow(numRow);
        for (int j = 0; j < 4; j++) {
            rw.createCell(j);
        }
        rw.getCell(0).setCellValue(student.getUsername());
        rw.getCell(1).setCellValue(student.getFirstName());
        rw.getCell(2).setCellValue(student.getLastNameInMoodle());
        rw.getCell(3).setCellValue(student.getEmail());
    }

    private String getLevel( ){
        int i=0;
        String niveau ;
        if(Util.getInstance().existInRow(this.getWorkbookIn().getSheetAt(0).getRow(0),"3CS-SIL")) return "3CS-SIL";
        if(Util.getInstance().existInRow(this.getWorkbookIn().getSheetAt(0).getRow(0),"3CS-SIQ")) return "3CS-SIQ";
        if(Util.getInstance().existInRow(this.getWorkbookIn().getSheetAt(0).getRow(0),"3CS-SIT")) return "3CS-SIT";
        for (Sheet sh: this.getWorkbookIn()) {
            for (Row rw:sh) {
                for (Cell cell:rw) {
                    niveau = cell.toString();
                    if(niveau.toUpperCase().contains("1CPI")){
                        return "1CPI";
                    }
                    if(niveau.toUpperCase().contains("2CPI")){
                        return  "2CPI";
                    }
                    if(niveau.toUpperCase().contains("SC")||niveau.toLowerCase().contains("1ère")){
                        i=1;
                    }
                    if(niveau.toLowerCase().contains("2ème")){
                        i=2;
                    }
                    if(niveau.toUpperCase().contains("CPI")&& i==1){
                        i=3;
                    }
                    if(niveau.toUpperCase().contains("CPI")&& i==2){
                        i=4;
                    }
                    if(niveau.toUpperCase().contains("SIL")){
                        return  "2CS-SIL";
                    }
                    if(niveau.toUpperCase().contains("SIQ")){
                        return  "2CS-SIQ";
                    }
                    if(niveau.toUpperCase().contains("SIT")){
                        return  "2CS-SIT";
                    }
                }
            }
        }
        if (i==1){
            return "1CS";
        }
        if (i==2){
            return "2CS";
        }
        if(i==3){
            return "1CPI";
        }
        if(i==4){
            return "2CPI";
        }
        return "";
    }

    private String getFileType(File file)
    {


        try  {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();
            Row rw = rowIterator.next();
            while (rowIterator.hasNext())
            {
                if(Util.getInstance().existInRow(rw,"NG"))
                {
                    return  "Solarite";
                }
                else
                {
                    rw = rowIterator.next();
                }
            }
            return "web";

        }catch (IOException e){
            e.printStackTrace();
        }
        return "";
    }

    private String ConvertWordTableToExcel(String wordPath){
        String type;
        try {
            FileInputStream fileInputStream = new FileInputStream(wordPath);
            XWPFDocument xwpfDocument = new XWPFDocument(fileInputStream);
            XWPFWordExtractor we = new XWPFWordExtractor(xwpfDocument);
            if(we.getText().contains("LISTE DES SUJETS DE PFE OPTION SIQ"))
            {
                type = "3CS-SIQ";
            }else if(we.getText().contains("LISTE DES SUJETS DE PFE OPTION SIT"))
            {
                type = "3CS-SIT";
            }else
            {
                type = "3CS-SIL";
            }
            String excelName = type + ".xlsx";
            FileOutputStream fileOutputStream = new FileOutputStream(excelName);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
            Sheet sheet = xssfWorkbook.createSheet();
            sheet.createRow(0).createCell(0).setCellValue(type);
            int i = 0 ;
            int j = 1 ;
            int nbColumns = 0;
            Row excelRow = sheet.createRow(j);
            for (XWPFTable table:xwpfDocument.getTables()) {
                for (XWPFTableRow row:table.getRows()) {

                    for (XWPFTableCell cell:row.getTableCells()) {
                        excelRow.createCell(i).setCellValue(cell.getText());
                        i++;
                    }
                    excelRow.createCell(i).setCellValue(type);
                    nbColumns = i;
                    i = 0 ;
                    j++;
                    excelRow = sheet.createRow(j);
                }
                sheet.getRow(j-table.getNumberOfRows()).getCell(nbColumns).setCellValue("NG");
            }
            xssfWorkbook.write(fileOutputStream);
            fileInputStream.close();
            return excelName;
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";

    }


    private void createStudentList(int indexOfEmailsSheet, String filePathOut, String optin, String level)  {
        int numRow = 1;
        Sheet sheet = getWorkbookIn().getSheetAt(0);

        generateHeader();

        ColumnsInformationBox box = new ColumnsInformationBox(sheet);
        box.extractInformationsFromFile();
        FileInformationExtractor extractor = new FileInformationExtractor(box,sheet,optin);
        ArrayList<Student> students = extractor.findStudents();
        this.studentHashMap =  extractor.createStudentsHashMap();
        EmailFinder emailFinder = new EmailFinder(indexOfEmailsSheet,getEmailsWorkbook(),this.studentHashMap);


        for(Student student : students)
        {
            student.setLevel(level);
            emailFinder.setStudent(student);
            emailFinder.getEmails();
            student.setStudentInformations();
            if(!student.hasEmail())
            {
                student.setPositionInWorkbookOut(numRow);
                this.listOfStudentsWithoutEmail.add(student);
            }
            generateRow(numRow,student);
            numRow++;
        }
        File file = new File(filePathOut);
        saveUsersList(file);

    }


    @Override
    public String buildCSV(ArrayList<String> workbooksPaths)  {//
        String type;
        for (String workbooksPath : workbooksPaths) {
            if(workbooksPath.contains(".docx"))
            {
                workbooksPath = ConvertWordTableToExcel(workbooksPath);
            }
            File file = new File(workbooksPath);
            type = getFileType(file);
            if (type.equals("Solarite")) openWorkbookIn(workbooksPath);
            else openEmailWorkbook(workbooksPath);
        }
        String level = getLevel();
        if (level.equals("1CPI")) {
            createStudentList(0, "temp1CPI.xlsx", "CPI", "1CPI");
            return "temp1CPI.xlsx";
        } else if (level.equals("2CPI")) {
            createStudentList(1, "temp2CPI.xlsx", "CPI", "2CPI");
            return "temp2CPI.xlsx";
        } else if (level.equals("1CS")) {
            createStudentList(2, "tempCS.xlsx", "SC", "1CS");
            return "tempCS.xlsx";
        } else if (level.equals("2CS-SIL")) {
            createStudentList(3, "temp2CS-SIL.xlsx", "SIL", "2CS-SIL");
            return "temp2CS-SIL.xlsx";
        } else if (level.equals("2CS-SIT")) {
            createStudentList(4, "temp2CS-SIL.xlsx", "SIT", "2CS-SIT");
            return "temp2CS-SIL.xlsx";
        } else if (level.equals("2CS-SIQ")) {
            createStudentList(5, "temp2CS-SIL.xlsx", "SIQ", "2CS-SIQ");
            return "temp2CS-SIQ.xlsx";
        } else if (level.equals("3CS-SIL")) {
            createStudentList(6, "temp3CS-SIL.xlsx", "3CS-SIL", "");
            return "temp3CS-SIL.xlsx";
        } else if (level.equals("3CS-SIT")) {
            createStudentList(7, "temp3CS-SIT.xlsx", "3CS-SIT", "");
            return "temp3CS-SIT.xlsx";
        } else if (level.equals("3CS-SIQ")) {
            createStudentList(8, "temp3CS-SIQ.xlsx", "3CS-SIQ", "");
            return "temp3CS-SIQ.xlsx";
        }

        return null;
    }

}
