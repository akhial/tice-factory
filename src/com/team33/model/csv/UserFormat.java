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
    private XSSFWorkbook workbookOut;
    private XSSFWorkbook EmailsWorkbook;
    private Row header = null;

    public Row getHeader() {
        return header;
    }

    public UserFormat() {
        this.workbookIn = new XSSFWorkbook();
        this.workbookOut = new XSSFWorkbook();
        this.workbookOut.createSheet();
        this.header = workbookOut.getSheetAt(0).createRow(0);
        this.EmailsWorkbook = new XSSFWorkbook();
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

    public void openWorkbookIn(String fiilePathIn)//charger un fichier excel dans le wbin
    {

        try {
            this.workbookIn = (XSSFWorkbook) WorkbookFactory.create(new File(fiilePathIn));
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

    }



    public void saveUsersList(File file)//enregistrer les résultat obtenue dans wbout dans le disque dure
    {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            workbookOut.write(fos);
            fos.close();
            workbookIn.close();
            EmailsWorkbook.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void openEmailWorkbook(String emailFilePath)// ouvrire le fichier contenant les e-mails
    {
        try {
            this.EmailsWorkbook = (XSSFWorkbook) WorkbookFactory.create(new File(emailFilePath));
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }
    /*
     *Méthodes utilitaires
     */
    public void generateHeader()// gener le header ie ecrire dans la première ligne (username,fistname,lastname,email) -> le format accepté par moodle
    {

        for (int i = 0; i < 5;i++)
        {
            this.getHeader().createCell(i);
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

    public String getUserInformation(Row rw, int colNom, int colPrenom)// avoir les information d'un utilisateur
    {

        return rw.getCell(colPrenom).toString() +" "+ rw.getCell(colNom).toString();
    }

    public void showListOfEmails(ArrayList listEmails)//afficher la liste des emails
    {
        int j = 1;
        if(!listEmails.isEmpty()) {
            for(int i = 0; i < listEmails.size();i++)
            {
                System.out.println(j+" : "+listEmails.get(i).toString());
                j++;
                System.out.println("______________________");
            }
        }
    }





    @Override
    public String buildCSV(ArrayList<String> workbooks) throws IOException, InvalidFormatException {
        return null;
    }
}