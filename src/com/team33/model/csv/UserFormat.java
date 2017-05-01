package com.team33.model.csv;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Amine on 28/02/2017.
 */
public abstract class UserFormat implements CSVFormat {
    private XSSFWorkbook workbookIn;
    private XSSFWorkbook workbookOut = new XSSFWorkbook();
    private XSSFWorkbook EmailsWorkbook;
    private Row header = workbookOut.createSheet().createRow(0);
    private boolean generatedPassword;

    public Row getHeader() {
        return header;
    }

    public UserFormat() {
    }

    public void setGeneratedPassword(boolean generatedPassword) {
        this.generatedPassword = generatedPassword;
    }

    public boolean isGeneratedPassword() {
        return generatedPassword;
    }

    public UserFormat(boolean isGenerated) {
        this.workbookIn = new XSSFWorkbook();
        this.workbookOut = new XSSFWorkbook();
        this.workbookOut.createSheet();
        this.header = workbookOut.getSheetAt(0).createRow(0);
        this.EmailsWorkbook = new XSSFWorkbook();
        generatedPassword = isGenerated ;
    }

    public XSSFWorkbook getWorkbookIn() {
        return workbookIn;
    }

    public XSSFWorkbook getWorkbookOut() {
        return workbookOut;
    }

    public XSSFWorkbook getEmailsWorkbook() {
        return EmailsWorkbook;
    }

    public void setWorkbookOut(XSSFWorkbook workbookOut) {
        this.workbookOut = workbookOut;
        header = workbookOut.getSheetAt(0).getRow(0);
    }

    public void openWorkbookIn(String fiilePathIn) throws IOException, InvalidFormatException//charger un fichier excel dans le wbin
    {
        this.workbookIn = (XSSFWorkbook) WorkbookFactory.create(new File(fiilePathIn));
    }



    public void saveUsersList(File file) throws IOException//enregistrer les résultat obtenue dans wbout dans le disque dure
    {
        FileOutputStream fos = new FileOutputStream(file);
        workbookOut.write(fos);
        fos.close();
    }

    public void openEmailWorkbook(String emailFilePath) throws IOException, InvalidFormatException// ouvrire le fichier contenant les e-mails
    {
        this.EmailsWorkbook = (XSSFWorkbook) WorkbookFactory.create(new File(emailFilePath));
    }
    /*
     *Méthodes utilitaires
     */
    public void generateHeader()// gener le header ie ecrire dans la première ligne (username,fistname,lastname,email) -> le format accepté par moodle
    {

        for(int i = 0; i < 5; i++) {
            getHeader().createCell(i);
        }

        this.getHeader().getCell(0).setCellValue("username");
        this.getHeader().getCell(1).setCellValue("password");
        this.getHeader().getCell(2).setCellValue("firstname");
        this.getHeader().getCell(3).setCellValue("lastname");
        this.getHeader().getCell(4).setCellValue("email");
    }

    public boolean rowContains(Row rw, String str)//verrifie si la ligne rw contient la chaine str
    {
        boolean contains = false;
        for(Cell cell : rw)
        {
            if(cell.toString().contains(str)) contains = true;
        }
        return  contains;
    }

    @Override
    public String buildCSV(ArrayList<String> workbooks) throws IOException, InvalidFormatException {
        return null;
    }
}