package com.team33.model.csv.students;

import com.team33.model.Utilities.Util;
import org.apache.poi.ss.usermodel.Cell;
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
    private HashMap<String, ArrayList<String>> emailsHashMap;

    public EmailFinder(String nameSheet, XSSFWorkbook workbookEmails, HashMap<Student, Integer> studentIntegerHashMap)
    {
        this.nameSheet = nameSheet;
        this.workbookEmails = workbookEmails;
        this.studentIntegerHashMap = studentIntegerHashMap;
        generateEmailsHashMap();
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
        if(this.emailsHashMap.containsKey(student.getKey()))
        {
            ArrayList<String> listOfEmails = this.emailsHashMap.get(this.student.getKey());
            if(listOfEmails.size() == 1) this.student.setListOfEmails(listOfEmails);
            else if (!existOtherStudents()) {
                String email = findEmail(listOfEmails);
                if (!email.equals("")) {
                    this.student.getListOfEmails().add(email);
                }
            } else {
                this.student.setListOfEmails(listOfEmails);
            }
        }
    }


    private String generateKey(String email)
    {
        String name ;
        name = email;
        name = name.replace(String.valueOf(name.charAt(0))+name.charAt(1)+name.charAt(2),"");
        name = name.replace("@esi.dz","");
        name = name.replace("_","");
        return name;
    }

    private int findColumn()
    {
        Sheet sheet =  workbookEmails.getSheet(nameSheet);
        for(Row row : sheet)
        {
            if(Util.getInstance().rowContainsIgnoreCase(row,"Adresse e-mail"))
            {
                for(Cell cell : row)
                {
                    if(cell.toString().equalsIgnoreCase("Adresse e-mail")) return cell.getColumnIndex();
                }
            }
        }
        return -1;
    }

    private void generateEmailsHashMap()
    {
        this.emailsHashMap = new HashMap<>();
        int index = findColumn();
        for(Row row : workbookEmails.getSheet(nameSheet))
        {
            if(Util.getInstance().rowContains(row,"@esi.dz"))
            {
                String email = row.getCell(index).toString();
                String key = generateKey(email);
                if(!this.emailsHashMap.containsKey(key)) this.emailsHashMap.put(key,new ArrayList<>());
                this.emailsHashMap.get(key).add(email);
            }
        }

    }

    private String findEmail(ArrayList<String> list)
    {
        String email = "";
        String firstPossibility = nameForEmail("");
        for(String str : list)
        {
            if(str.contains(firstPossibility)) email = str;
        }

        if(email.equals(""))
        {
            String secondPossibility = nameForEmail("_");
            for(String str : list)
            {
                if(str.contains(secondPossibility)) email = str;
            }
        }
        return email;
    }

}