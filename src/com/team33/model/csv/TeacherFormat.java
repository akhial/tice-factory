package com.team33.model.csv;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by Amine on 13/02/2017.
 */

public class TeacherFormat extends UserFormat implements CSVFormat {
    public TeacherFormat() {


    }

    /**Permet d'extraire la partie (nom + "@esi.dz") de l'adresse mail
     *
     * */
    public String extractNameFromEmail(Row rw, int index)//extraire la partie du mail à utilisé comme référence pour comparaison
    {
        String name ;
        name = rw.getCell(index).toString();
        name =  name.replace(name.substring(name.indexOf("@"),name.length()),"");
        name = name.substring(name.indexOf("_")+1,name.length());
        return name;

    }
    /**
     * Permet de générer la partie (nom + "@esi.dz") à partir d'un nom
     * */
    public String nameForEmail(String lastname)
    {
        String str ;

        str = lastname;
        str = str.replace(" ", "_");
        str = str.toLowerCase();
        return str;

    }
    /**
     * Permet d'écrire la liste de chaines dans la ligne N =° numRow du workbookOut
     * */
    public void generateRow(int numRow,ArrayList<String> arrayList)
    {
        Row rw = getWorkbookOut().getSheetAt(0).createRow(numRow);
        for (int j = 0; j < 4; j++) {
            rw.createCell(j);
            rw.getCell(j).setCellValue(arrayList.get(j));
        }
    }
    /**
     * Permet de lister toutes les adresse mail contenant un nom donné
     * */
    public ArrayList<String> ListEmails(String lastname){
        Sheet sheet = getEmailsWorkbook().getSheetAt(0);
        Row row ;
        int emailColumn = rangOfCellContaining(sheet.rowIterator().next(),"@esi.dz");
        ArrayList<String> arrayList = new ArrayList<>();
        Iterator<Row> rowIterator = sheet.rowIterator();

        while (rowIterator.hasNext()){
            row = rowIterator.next();
            if (row.getCell(emailColumn).toString().contains(lastname.toLowerCase().replace(" ","_"))){
                if (extractNameFromEmail(row,emailColumn).toString().compareTo(nameForEmail(lastname))== 0){
                    arrayList.add(row.getCell(emailColumn).toString());
                }
            }
        }
        return arrayList;
    }
    /**
     * Permet de choisir l'adresse mail adequate en éliminant les adresses déja traitées
     * */
    private String ChooseEmail(ArrayList<String> listEmails, Row rw, int colNom, int colPrenom,ArrayList<ArrayList<String>> unhandledEmails)
    {
        String email = null;
        for (ArrayList<String> as:unhandledEmails) {
            listEmails.removeAll(as);

        }
        if ((listEmails.size() > 0)) {
            if (listEmails.size() == 1){
                email = listEmails.get(0);
            }else{

                unhandledEmails.add(listEmails);
                /***System.out.println("Plusieurs emails peuvent correspendre à l'enseignant : "+getUserInformation(rw,colNom,colPrenom));
                showListOfEmails(listEmails);
                System.out.print("Veuillez coisir le bon mail svp : ");
                Scanner sc = new Scanner(System.in);
                int i = sc.nextInt();
                email = listEmails.get(i-1);**/
            }
        }
        return email;
    }
    /**
     * Retourne l'adresse mail corespondante au nom lastname
     * */
    public  String EmailAdress(Row row,String lastname,int lastNameColumn,int firstNameColumn,ArrayList<ArrayList<String>> unhandlededEmails ){

        String emailAdress;
        ArrayList<String> arrayList = ListEmails(lastname);
        emailAdress = ChooseEmail(arrayList,row,lastNameColumn,firstNameColumn,unhandlededEmails);

        return emailAdress;

    }


    @Override
    public String buildCSV(String... workbooks) {
        String workbookPath = workbooks[0];
        String emailWorkbookPath = workbooks[1];
        int firstNameColumn = 0 ;
        int lastNameColumn = 0;
        String email;
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<ArrayList<String>> unhandleddEmails = new ArrayList<>();
        openWorkbookIn(workbookPath);
        openEmailWorkbook(emailWorkbookPath);
        Sheet sheet = this.getWorkbookIn().getSheetAt(0);
        Row row ;
        Iterator<Row> iterator = sheet.iterator();
        row = iterator.next();
        generateHeader();
        boolean found = false ;
        while ((!found) && (iterator.hasNext())){
            if (rowContains(row,"NOM")) {
                found = true ;
                firstNameColumn = column(row,"PRENOM");
                lastNameColumn = column(row,"NOM");
            }
            else row = iterator.next();
        }
        int numRow = 1 ;
        while (iterator.hasNext()){
            row = iterator.next();
            email = EmailAdress(row,row.getCell(lastNameColumn).toString(),lastNameColumn,firstNameColumn,unhandleddEmails);
            if (email != null) arrayList.add( email.substring(0,email.indexOf("@"))) ;
            else  arrayList.add(null) ;
            arrayList.add(row.getCell(firstNameColumn).toString().toUpperCase()+" ENS:");
            arrayList.add(row.getCell(lastNameColumn).toString().toUpperCase());
            arrayList.add(email);
            generateRow(numRow, (ArrayList<String>) arrayList.clone());
            arrayList.clear();
            numRow++;
        }
        File file = new File("TeachersList.xlsx");
        saveUsersList(file);
        try {
            getEmailsWorkbook().close();
            getWorkbookIn().close();
            getWorkbookOut().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file.getPath();
    }
}
