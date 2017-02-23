package com.team33.model.csv;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Amine on 13/02/2017.
 */

public class StudentFormat implements CSVFormat {

    private XSSFWorkbook workbookIn;
    private XSSFWorkbook workbookOut;
    private XSSFWorkbook workbookEmails;
    private ArrayList<String> filePathIn;
    private Row header = null;

    public StudentFormat() {
        this.workbookIn = new XSSFWorkbook();
        header = this.workbookIn.getSheetAt(0).getRow(0);
        this.workbookOut = new XSSFWorkbook();
        this.workbookOut.createSheet();
        this.workbookEmails = new XSSFWorkbook();
        this.filePathIn = new ArrayList<>();
    }

    private   void openWorkbookIn(String fiilePathIn)//charger un fichier excel dans le wbin
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

    private void generateHeader()// gener le header ie ecrire dans la première ligne (username,fistname,lastname,email) -> le format accepté par moodle
    {

        for (int i = 0; i < 4;i++)
        {
            this.header.createCell(i, CellType.STRING);
        }

        this.header.getCell(0).setCellValue("username");
        this.header.getCell(1).setCellValue("firstname");
        this.header.getCell(2).setCellValue("lastname");
        this.header.getCell(3).setCellValue("email");
    }

    private void saveStudentList(File file)//enregistrer les résultat obtenue dans wbout dans le disque dure
    {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            this.workbookOut.write(fos);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void poenEmailWorkbook(String emailFilePath)// ouvrire le fichier contenant les e-mails
    {
        try {
            this.workbookEmails = (XSSFWorkbook) WorkbookFactory.create(new File(emailFilePath));
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

    private boolean existInRow(Row rw, String str)//verrifier si'il y a une case dans la ligne rw dont sa valeur (pas just contenir) est la chaine str
    {
        boolean exist = false;
        for(Cell cell :rw )
        {
            if(str.equals(cell.toString())) exist = true;
        }
        return  exist;
    }


    private    int column(Row rw,String colName)// retourne l'indice de la colone de valeur colNom dans la ligne rw
    {
        boolean found = false;
        int colIndex = -1;

        for (Cell cell : rw) {
            if (colName.equals(cell.toString())) {
                colIndex = cell.getColumnIndex();
            }
        }
        return colIndex;
    }

    private void generateRow(int numRow,String username,String firstname,String lastname, String email)// générer une ligne cde fichier résultat contenant les coordonné d'un étudiant
    {
        Row rw = workbookOut.getSheetAt(0).createRow(numRow);
        for (int j = 0; j < 4; j++) {
            rw.createCell(j);
        }
        rw.getCell(0).setCellValue(username);
        rw.getCell(1).setCellValue(firstname);
        rw.getCell(2).setCellValue(lastname);
        rw.getCell(3).setCellValue(email);
    }

    private  String generateUser(Row rw,int indexOfNom,String level, int indexOfGroupe)// generer le nom de format moodle (a revoir en fonction des informations concernant le cycle superieur)
    {
        rw.getCell(indexOfGroupe).setCellType(CellType.STRING);
        return  rw.getCell(indexOfNom).toString()+" "+ level+rw.getCell(indexOfGroupe).toString();

    }



    private boolean rowContains(Row rw, String str)//verrifie si la ligne rw contient la chaine str
    {
        boolean contains = false;
        for(Cell cell : rw)
        {
            if(cell.toString().contains(str)) contains = true;
        }
        return  contains;
    }

    private int rangOfCellContaining(Row rw,String str) // retourne l'indice de la colonne contenat la chaine str
    {
        int rang = -1;
        boolean contains = false;
        Iterator<Cell> cellIterator = rw.iterator();
        Cell cell = cellIterator.next();
        while ((cell != null) && (contains == false))
        {
            if(cell.toString().contains(str)) {
                contains = true;
                rang = cell.getColumnIndex();
            }
            else cell = cellIterator.next();
        }
        return  rang;
    }
    /*------------------------------------------------------------------------------------------------------
     *                                          FINDING EMAILS                                             *
     *-----------------------------------------------------------------------------------------------------*/
    private String extractNameFromEmail(Row rw, int index)//extraire la partie du mail à utilisé comme référence pour comparaison
    {
        String name = new String();
        name = rw.getCell(index).toString();
        name = name.replace(String.valueOf(name.charAt(0))+name.charAt(1)+name.charAt(2),"");
        return name;

    }

    private String nameForEmail(Row rw, int colNom, String repalceSapaceWit)// générer une chaine de carractère pour utiliser en comparaison pour trouver l'email
    {
        String str = new String();

        str = rw.getCell(colNom).toString();
        str = str.replace(" ", repalceSapaceWit);
        str = str.toLowerCase();
        str = str + "@esi.dz";
        return str;

    }

    private ArrayList<String> findEmails(String namForEmail1,String namForEmail2,int indexOfEmailsSheet)// retourne un ArrayList contenant les emails douteux
    {
        ArrayList<String> listeEmails = new ArrayList<String>();
        Sheet sheet = this.workbookEmails.getSheetAt(indexOfEmailsSheet);
        for(Row rw:sheet)
        {
            if(rowContains(rw,namForEmail1))
            {
                if (namForEmail1.equals(extractNameFromEmail(rw,rangOfCellContaining(rw,namForEmail1))))
                    listeEmails.add(rw.getCell(rangOfCellContaining(rw,namForEmail1)).toString());
            }else if (rowContains(rw,namForEmail2))
            {
                if (namForEmail2.equals(extractNameFromEmail(rw,rangOfCellContaining(rw,namForEmail2))))
                    listeEmails.add(rw.getCell(rangOfCellContaining(rw,namForEmail2)).toString());
            }
        }
        return listeEmails;
    }

    private String getStudentInoformations(Row rw, int colNom, int colPrenom)// avoir les information d'un étudient
    {

        return rw.getCell(colPrenom).toString() +" "+ rw.getCell(colNom).toString();
    }


    private void showListOfEmails(ArrayList lsitEmails)//afficher la liste des emails
    {
        int j = 1;
        if(lsitEmails.isEmpty())
        {
            System.out.println("ERREUR");
        }
        else
        {
            for(int i = 0; i < lsitEmails.size();i++)
            {
                System.out.println(j+" : "+lsitEmails.get(i).toString());
                j++;
                System.out.println("______________________");
            }
        }
    }

    




    @Override
    public String buildCSV(ArrayList<String> workbooksPaths) {
        return null;
    }
}
