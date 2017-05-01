package com.team33.model.csv;

import com.team33.model.Utilities.Util;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * Created by Amine on 13/02/2017.
 */

public class TeacherFormat extends UserFormat {
    private HashMap<String,ArrayList<String>> unHandledEmails;
    private final String tempName = "TeachersList.xlsx";
    public TeacherFormat() {
        unHandledEmails = new HashMap<>();
    }

    public HashMap<String, ArrayList<String>> getUnHandledEmails() {
        return unHandledEmails;
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
    public String generatePassWord(int length)
    {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder pass = new StringBuilder();
        for(int x = 0; x < length; x++)   {
            int i = (int)Math.floor(Math.random() * (chars.length() -1));
            pass.append(chars.charAt(i));
        }
        return pass.toString();
    }
    /**
     * Permet d'écrire la liste de chaines dans la ligne N =° numRow du workbookOut
     * */
    public void generateRow(int numRow,ArrayList<String> arrayList)
    {
        Row rw = getWorkbookOut().getSheetAt(0).createRow(numRow);
        for (int j = 0; j < arrayList.size(); j++) {
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
        int emailColumn = Util.getInstance().rangOfCellContaining(sheet.rowIterator().next(),"@esi.dz");
        ArrayList<String> arrayList = new ArrayList<>();
        Iterator<Row> rowIterator = sheet.rowIterator();
        if (!unHandledEmails.containsKey(lastname.toLowerCase())){
            while (rowIterator.hasNext()){
                row = rowIterator.next();
                if (row.getCell(emailColumn).toString().contains(lastname.toLowerCase().replace(" ","_"))){
                    if (extractNameFromEmail(row,emailColumn).toString().compareTo(nameForEmail(lastname))== 0){
                        arrayList.add(row.getCell(emailColumn).toString());
                    }
                }
            }
        }

        return arrayList;
    }
    /**
     * Permet de choisir l'adresse mail adequate en éliminant les adresses déja traitées
     * */
    private String ChooseEmail(ArrayList<String> listEmails, Row rw, int colNom, int colPrenom)
    {
        String email = null;
        if (listEmails.size() == 1){
            email = listEmails.get(0);
        }else{
            unHandledEmails.put(rw.getCell(colNom).toString().toLowerCase()+"*"+rw.getCell(colPrenom).toString().toLowerCase(),listEmails);
        }

        return email;
    }
    /**
     * Retourne l'adresse mail corespondante au nom lastname
     * */
    public  String EmailAdress(Row row,String lastname,int lastNameColumn,int firstNameColumn){

        String emailAdress;
        ArrayList<String> arrayList = ListEmails(lastname);
        emailAdress = ChooseEmail(arrayList,row,lastNameColumn,firstNameColumn);

        return emailAdress;

    }


    @Override
    public String buildCSV(ArrayList<String> workbooks) throws IOException, InvalidFormatException {
        String workbookPath = workbooks.get(0);
        String emailWorkbookPath = workbooks.get(1);
        int firstNameColumn = 0 ;
        int lastNameColumn = 0;
        String email;
        ArrayList<String> arrayList = new ArrayList<>();
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
                firstNameColumn = Util.getInstance().column(row,"PRENOM");
                lastNameColumn = Util.getInstance().column(row,"NOM");
            }
            else row = iterator.next();
        }
        int numRow = 1 ;
        while (iterator.hasNext()){
            row = iterator.next();
            email = EmailAdress(row,row.getCell(lastNameColumn).toString(),lastNameColumn,firstNameColumn);
            if (email != null) arrayList.add( email.substring(0,email.indexOf("@"))) ;
            else  arrayList.add(null) ;
            arrayList.add((this.isGeneratedPassword()) ? generatePassWord(8) : row.getCell(firstNameColumn).toString().toLowerCase());
            arrayList.add(row.getCell(lastNameColumn).toString().toUpperCase()+" ENS:");
            arrayList.add(row.getCell(firstNameColumn).toString().toUpperCase());
            arrayList.add(email);
            generateRow(numRow, (ArrayList<String>) arrayList.clone());
            arrayList.clear();
            numRow++;
        }
        File file = new File(tempName);
        saveUsersList(file);
        return file.getPath();
    }

    public void AddingMissingEmails(HashMap<String,String> finalEmails) throws IOException {
        this.setWorkbookOut(new XSSFWorkbook(tempName));
        for (HashMap.Entry e:finalEmails.entrySet()) {
            StringTokenizer stringTokenizer = new StringTokenizer((String) e.getKey(),"*");
            String lastName = stringTokenizer.nextToken();
            String firstName = stringTokenizer.nextToken();
            String email = (String) e.getValue();
            Row row = getWorkbookOut().getSheetAt(0).getRow(1);
            boolean found = false;
            int i = 1 ;
            while (!found && i < getWorkbookOut().getSheetAt(0).getLastRowNum()+1){
                row = getWorkbookOut().getSheetAt(0).getRow(i);
                if (rowContains(row,firstName.toUpperCase()+" ENS:") && rowContains(row,lastName.toUpperCase())){
                    row.getCell(0).setCellValue(email.substring(0,email.indexOf("@")));
                    row.getCell(4).setCellValue(email);
                    found = true;


                }
                i++;


            }
        }
        saveUsersList(new File("TeacherList.xlsx"));
        new File(tempName).delete();
        new File("TeacherList.xlsx").renameTo(new File(tempName));

    }
}
