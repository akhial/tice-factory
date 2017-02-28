package com.team33.model.csv;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

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
        this.workbookOut = new XSSFWorkbook();
        this.workbookOut.createSheet();
        this.workbookOut.getSheetAt(0).createRow(0);
        this.header = workbookOut.getSheetAt(0).getRow(0);
        this.workbookEmails = new XSSFWorkbook();
        this.filePathIn = new ArrayList<>();
    }

    private   void openWorkbookIn(File fileIn)//charger un fichier excel dans le wbin
    {

        try {
            this.workbookIn = (XSSFWorkbook) WorkbookFactory.create(fileIn);
        }catch (IOException | InvalidFormatException e){
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
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void openEmailWorkbook(File emailFile)// ouvrire le fichier contenant les e-mails
    {
        try {
            this.workbookEmails = (XSSFWorkbook) WorkbookFactory.create(emailFile);
        }catch (IOException | InvalidFormatException e){
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
        String name ;
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
    private String nameForEmail(Row rw, int colNom,int colPrenom, String repalceSapaceWit)// générer une chaine de carractère pour utiliser en comparaison pour trouver l'email
    {
        String str ;

        str = rw.getCell(colNom).toString();
        str = str.replace(" ", repalceSapaceWit);
        str = str.toLowerCase();
        str = rw.getCell(colPrenom).toString().toLowerCase().charAt(0)+"_"+str + "@esi.dz";
        return str;

    }

    private String scondNameForEmail(Row rw, int colNom,int colPrenom, String repalceSapaceWit)// générer une chaine de carractère pour utiliser en comparaison pour trouver l'email
    {
        String str ;

        str = rw.getCell(colNom).toString();
        str = str.replace(" ", repalceSapaceWit);
        str = str.toLowerCase();
        str = str + "@esi.dz";
        return str;

    }

    private ArrayList<String> findListEmails(String namForEmail1,String namForEmail2,int indexOfEmailsSheet)// retourne un ArrayList contenant les emails douteux
    {
        ArrayList<String> listeEmails = new ArrayList<String>();
        Sheet sheet = this.workbookEmails.getSheetAt(indexOfEmailsSheet);
        for(Row rw:sheet)
        {
            if(rowContains(rw,namForEmail1))
            {
                if (namForEmail1.equals(secondPossibility(rw,rangOfCellContaining(rw,namForEmail1))))
                    listeEmails.add(rw.getCell(rangOfCellContaining(rw,namForEmail1)).toString());
            }else if (rowContains(rw,namForEmail2))
            {
                if (namForEmail2.equals(secondPossibility(rw,rangOfCellContaining(rw,namForEmail2))))
                    listeEmails.add(rw.getCell(rangOfCellContaining(rw,namForEmail2)).toString());
            }
        }
        return listeEmails;
    }

    private String findEmail(String namForEmail1,String namForEmail2,int indexOfEmailsSheet)// retourne un ArrayList contenant les emails douteux
    {
        String email = "";
        Sheet sheet = this.workbookEmails.getSheetAt(indexOfEmailsSheet);
        for(Row rw:sheet)
        {
            if(rowContains(rw,namForEmail1))
            {
                if (namForEmail1.equals(extractNameFromEmail(rw,rangOfCellContaining(rw,namForEmail1))))
                    email = rw.getCell(rangOfCellContaining(rw,namForEmail1)).toString();
            }else if (rowContains(rw,namForEmail2))
            {
                if (namForEmail2.equals(extractNameFromEmail(rw,rangOfCellContaining(rw,namForEmail2))))
                    email = rw.getCell(rangOfCellContaining(rw,namForEmail2)).toString();
            }
        }
        return email;
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
            for (Object lsitEmail : lsitEmails) {
                System.out.println(j + " : " + lsitEmail.toString());
                j++;
                System.out.println("______________________");
            }
        }
    }

    private String chooseEmail( Row rw, int colNom, int colPrenom,int indexOfEmailsSheet,String optin)// choisir un email de la liste
    {
        String email = "";
        Scanner sc = new Scanner(System.in);
        if (!existOtherStudents(rw.getCell(colPrenom).toString().toLowerCase().charAt(0),rw.getCell(colNom).toString(),colNom,colPrenom,rw.getRowNum(),optin))
        {
            email = findEmail(nameForEmail(rw,colNom,colPrenom,"_"),nameForEmail(rw,colNom,colPrenom,""),indexOfEmailsSheet);
        }
        else
        {
            ArrayList<String> listOfEmails = new ArrayList();
            System.out.println("Plusieurs emails peuvent correspendre à l'étudiant : "+getStudentInoformations(rw,colNom,colPrenom));
            listOfEmails = findListEmails(scondNameForEmail(rw,colNom,colPrenom,"_"),scondNameForEmail(rw,colNom,colPrenom,""),indexOfEmailsSheet);
            showListOfEmails(listOfEmails);
            System.out.print("Veuillez coisir le bon mail svp : ");
            int i = sc.nextInt();
            email = listOfEmails.get(i-1);
        }
        if (email.equals(""))
        {
            System.out.println("Nous n'avons pas trouvé l'e-mail correspondant à l'étudinat : "+getStudentInoformations(rw,colNom,colPrenom));
            System.out.print("Veuillez le saisir manuellement svp : ");
            email = sc.nextLine();
        }
        return email;
    }

    private boolean existOtherStudents(char firstLetter,String name,int colNom, int colPrenom,int begin,String optin)
    {
        boolean exist = false;
        Sheet sheet = this.workbookIn.getSheetAt(0);
        String student = new String();

        Row row = sheet.getRow(begin+1);
        int i = begin+1;
        while ((row != null)&&(!exist))
        {
            if (existInRow(row,optin))
            {
                student = getStudentInoformations(row,colNom,colPrenom);
                if((student.toLowerCase().charAt(0) == firstLetter) && (row.getCell(colNom).toString().toLowerCase().equals(name.toLowerCase())))
                {
                    exist = true;
                }

            }
            i++;
            row = sheet.getRow(i);
        }
        return exist;
    }

    private String generateUsename(String emai)
    {
        String username;

        username = emai.replaceFirst(String.valueOf(emai.charAt(0)),"");
        username = username.replace("@esi.dz","");
        return username;
    }


    private String getLevel( ){
        int i=0;
        String niveau ;
        for (Sheet sh:this.workbookIn) {
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
                if(existInRow(rw,"NG"))
                {
                    return  "Solarite";
                }
                else
                {
                    rw = rowIterator.next();
                }
                return "WEB";
            }


        }catch (IOException e){
            e.printStackTrace();
        }
        return "";
    }


    public void createStudentList(int indexOfEmailsSheet,String filePathOut, String optin,String level)  {
        int colNom = -1;
        int colPrenom = -1;
        int colGroupe = -1;
        int numRow = 1;
        ArrayList<String> listEmails = new ArrayList<String>();
        Sheet sheet = workbookIn.getSheetAt(0);
        Sheet sheetOut = workbookOut.createSheet();
        Row rwOut = sheetOut.createRow(0);


        generateHeader();

        boolean found = false;
        Iterator<Row> rowIterator = sheet.rowIterator();
        Row rw = rowIterator.next();
        while ((!found) && (rowIterator.hasNext()))
        {
            if(existInRow(rw,"Nom"))
            {
                found = true;
                colNom = column(rw,"Nom");
                colPrenom = column(rw,"Prenom");
                colGroupe = column(rw,"NG");
            }
            else
            {
                rw = rowIterator.next();
            }
        }

        while (rowIterator.hasNext())
        {
            if (existInRow(rw,optin))
            {
                String email = chooseEmail(rw,colNom,colPrenom,indexOfEmailsSheet,optin);
                String usernam = generateUsename(email);

                String firtstname = rw.getCell(colPrenom).toString();
                String lastname = generateUser(rw,colNom,level,colGroupe);

                generateRow(numRow,usernam,firtstname,lastname,email);
                numRow++;

            }
            rw = rowIterator.next();
        }
        File file = new File(filePathOut);
        saveStudentList(file);
    }


    @Override
    public String buildCSV(ArrayList<String> workbooksPaths)  {
        for(int i = 0; i < workbooksPaths.size();i++)
        {
            File file = new File(workbooksPaths.get(i));
            String  type = getFileType(file);
            if(type.equals("Solarite")) openWorkbookIn(file); else openEmailWorkbook(file);
        }
        String level = getLevel();
        switch (level)
        {
            case "1CPI" :
                createStudentList(0,"temp1CPI.xlsx","CPI","1CPI");
                return "temp1CPI.xlsx";
            case "2CPI" :
                createStudentList(1,"temp2CPI.xlsx","CPI","2CPI");
                return "temp2CPI.xlsx";
            case "1CS" :
                createStudentList(2,"tempCS.xlsx","SC","1CS");
                return "tempCS.xlsx";
            case "2CS-SIL" :
                createStudentList(2,"temp2CS-SIL.xlsx","SIL","2CS-SIL");
                return "temp2CS-SIL.xlsx";
            case "2CS-SIT" :
                createStudentList(2,"temp2CS-SIL.xlsx","SIT","2CS-SIT");
                return "temp2CS-SIL.xlsx";
            case "2CS-SIQ" :
                createStudentList(2,"temp2CS-SIL.xlsx","SIQ","2CS-SIQ");
        }

        return null;
    }
}
