package com.team33.model.csv.Students;

import com.team33.model.Util;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hamza on 09/03/2017.
 */
    class EmailFinder  {
    private String nameSheet;
    private  XSSFWorkbook workbookEmails;
    private Student student;
    private HashMap<Student,Integer> studentIntegerHashMap;

    public EmailFinder(String nameSheet, XSSFWorkbook workbookEmails, HashMap<Student, Integer> studentIntegerHashMap)
    {
        this.nameSheet = nameSheet;
        this.workbookEmails = workbookEmails;
        this.studentIntegerHashMap = studentIntegerHashMap;
    }

    public void setStudent(Student student) {

        this.student = student;
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

    private boolean existOtherStudents()
    {
        int i = studentIntegerHashMap.get(this.student);
        if(i == 0)
        {
            System.out.println("ERROR");
            return false;
        }

        else if(i > 1) return true;
        else  return false;
    }

    public void getEmails()// choisir un email de la liste
    {
        if (!existOtherStudents())
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

    private String findEmail(String namForEmail1,String namForEmail2)// retourne un ArrayList contenant les emails douteux
    {
        String email = "";
        Sheet sheet = this.workbookEmails.getSheet(this.nameSheet);
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
        Sheet sheet = this.workbookEmails.getSheet(this.nameSheet);
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