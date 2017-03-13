package com.team33.model.csv;

import com.team33.model.Util;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;

/**
 * Created by hamza on 09/03/2017.
 */
public class EmailFinder {
    private int colNom;
    private int colPrenom;
    private int indexOfEmailsSheet;
    private  XSSFWorkbook workbookEmails;
    private XSSFWorkbook workbookIn;
    private Student student;
    public void setIndexOfEmailsSheet(int indexOfEmailsSheet) {
        this.indexOfEmailsSheet = indexOfEmailsSheet;
    }

    public EmailFinder(int colNom, int colPrenom, int indexOfEmailsSheet, XSSFWorkbook workbookEmails, XSSFWorkbook workbookIn) {
        this.colNom = colNom;
        this.colPrenom = colPrenom;
        this.indexOfEmailsSheet = indexOfEmailsSheet;
        this.workbookEmails = workbookEmails;
        this.workbookIn = workbookIn;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setColNom(int colNom) {
        this.colNom = colNom;
    }

    public void setColPrenom(int colPrenom) {
        this.colPrenom = colPrenom;
    }

    private String nameForEmail(String repalceSapaceWit)// générer une chaine de carractère pour utiliser en comparaison pour trouver l'email
    {
        String str ;

        str = this.student.getFirstName();
        str = str.replace(" ", repalceSapaceWit);
        str = str.toLowerCase();
        str = this.student.getLastName().toLowerCase().charAt(0)+"_"+str + "@esi.dz";
        return str;

    }
    private void getStudentInoformations(Student student,Row rw)// avoir les information d'un étudient
    {

        student.setFirstName(rw.getCell(this.colNom).toString());
        student.setLastName(rw.getCell(this.colPrenom).toString());
        student.setPositionInWorkbookIn(rw.getRowNum());
    }

    private boolean existOtherStudents(String optin)
    {
        boolean exist = false;
        Sheet sheet = this.workbookIn.getSheetAt(0);

        int i = 0;
        Row row = sheet.getRow(0);
        while ((row != null)&&(!exist))
        {
            if (Util.getInstance().existInRow(row,optin))
            {
                Student student = new Student();
                getStudentInoformations(student,row);
                if((this.student.equals(student)))
                {
                    exist = true;
                }

            }
            i++;
            row = sheet.getRow(i);
        }
        return exist;
    }

    public void getEmails(String optin)// choisir un email de la liste
    {
        if (!existOtherStudents(optin))
        {
            String email = findEmail(nameForEmail("_"),nameForEmail(""));
            if(!email.equals("")) {
                this.student.getListOfEmails().add(email);
            }
        }
        else
        {
            this.student.setListOfEmails(findListEmails(scondNameForEmail( "_"),scondNameForEmail( "")));
        }
    }


    public void showListOfEmails(ArrayList lsitEmails)//afficher la liste des emails
    {
        int j = 1;
        if(lsitEmails.isEmpty())
        {
            System.out.println("ERREUR");
        }
        else
        {
            for (Object lsitEmail : lsitEmails) {
                System.out.println(j + " : " + lsitEmail.toString());
                j++;
                System.out.println("______________________");
            }
        }
    }

    private String findEmail(String namForEmail1,String namForEmail2)// retourne un ArrayList contenant les emails douteux
    {
        String email = "";
        Sheet sheet = this.workbookEmails.getSheetAt(this.indexOfEmailsSheet);
        for(Row rw:sheet)
        {
            if(Util.getInstance().rowContains(rw,namForEmail1))
            {
                if (namForEmail1.equals(extractNameFromEmail(rw,Util.getInstance().rangOfCellContaining(rw,namForEmail1))))
                    email = rw.getCell(Util.getInstance().rangOfCellContaining(rw,namForEmail1)).toString();
            }else if (Util.getInstance().rowContains(rw,namForEmail2))
            {
                if (namForEmail2.equals(extractNameFromEmail(rw,Util.getInstance().rangOfCellContaining(rw,namForEmail2))))
                    email = rw.getCell(Util.getInstance().rangOfCellContaining(rw,namForEmail2)).toString();
            }
        }
        return email;
    }

    private ArrayList<String> findListEmails(String namForEmail1,String namForEmail2)// retourne un ArrayList contenant les emails douteux
    {
        ArrayList<String> listeEmails = new ArrayList<String>();
        Sheet sheet = this.workbookEmails.getSheetAt(this.indexOfEmailsSheet);
        for(Row rw:sheet)
        {
            if(Util.getInstance().rowContains(rw,namForEmail1))
            {
                if (namForEmail1.equals(secondPossibility(rw,Util.getInstance().rangOfCellContaining(rw,namForEmail1))))
                    listeEmails.add(rw.getCell(Util.getInstance().rangOfCellContaining(rw,namForEmail1)).toString());
            }else if (Util.getInstance().rowContains(rw,namForEmail2))
            {
                if (namForEmail2.equals(secondPossibility(rw,Util.getInstance().rangOfCellContaining(rw,namForEmail2))))
                    listeEmails.add(rw.getCell(Util.getInstance().rangOfCellContaining(rw,namForEmail2)).toString());
            }
        }
        return listeEmails;
    }

    private String scondNameForEmail(String repalceSapaceWit)// générer une chaine de carractère pour utiliser en comparaison pour trouver l'email
    {
        String str ;

        str = this.student.getFirstName();
        str = str.replace(" ", repalceSapaceWit);
        str = str.toLowerCase();
        str = str + "@esi.dz";
        return str;

    }
    private String extractNameFromEmail(Row rw, int index)//extraire la partie du mail à utiliser comme référence pour comparaison
    {
        String name;
        name = rw.getCell(index).toString();
        name = name.replaceFirst(String.valueOf(name.charAt(0)),"");
        return name;

    }

    private String secondPossibility(Row rw, int index)//extraire la partie du mail à utilisé comme référence pour comparaison
    {
        String name ;
        name = rw.getCell(index).toString();
        name = name.replace(String.valueOf(name.charAt(0))+name.charAt(1)+name.charAt(2),"");
        return name;

    }
}
