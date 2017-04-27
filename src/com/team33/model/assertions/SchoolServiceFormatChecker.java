package com.team33.model.assertions;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by Mitchell on 13/02/2017.
 */
public class SchoolServiceFormatChecker implements ExcelFormat {

    public boolean checkFormat(String f1) throws IOException, MissingFieldsException, NoLineFoundException {
        XSSFWorkbook wB = new XSSFWorkbook(new FileInputStream(f1));

        for(Sheet sheet : wB) {
            if(wB.getSheetAt(0).getRow(wB.getSheetAt(0).getLastRowNum()) == null)
                throw new NoSuchElementException("fichier Vide !");
            Row row;
            int cpt = 1;
            Cell cell;
            boolean bb = false;
            Iterator<Row> rowIterator = sheet.iterator();
            row = rowIterator.next();
            while(rowIterator.hasNext()) {
                if(Util.getInstance().existInRow(row, "Nom") || Util.getInstance().existInRow(row, "Prenom") ||
                        Util.getInstance().existInRow(row, "Optin") || Util.getInstance().existInRow(row, "Moyenne")
                        || Util.getInstance().existInRow(row, "NG") || Util.getInstance().existInRow(row, "NS") ||
                        Util.getInstance().existInRow(row, "CI") || Util.getInstance().existInRow(row, "CF") ||
                        Util.getInstance().existInRow(row, "CC")
                        || Util.getInstance().existInRow(row, "TP") || Util.getInstance().existInRow(row, "Num") ||
                        Util.getInstance().existInRow(row, "Matrin") || Util.getInstance().existInRow(row, "Anetin")
                        ) {
                    bb = true;
                    if(!Util.getInstance().existInRow(row, "Nom") || !Util.getInstance().existInRow(row, "Prenom") ||
                            !Util.getInstance().existInRow(row, "Optin") || !Util.getInstance().existInRow(row, "NG")) {

                        String s1 = "Champ(s) Introuvable(s) : "+cpt+" :";
                        if(!Util.getInstance().existInRow(row, "Nom")) s1 = s1+"  Nom,";
                        if(!Util.getInstance().existInRow(row, "Prenom")) s1 = s1+"  Prenom,";
                        if(!Util.getInstance().existInRow(row, "NG")) s1 = s1+"  NG,";
                        if(!Util.getInstance().existInRow(row, "Anetin")) s1 = s1+"  Anetin,";
                        throw new MissingFieldsException(s1);
                    }
                }
                row = rowIterator.next();
                cpt++;


            }

            if(!bb) throw new NoLineFoundException("Header de la table non-trouvé !");
        }
        return true;
    }

    @Override
      public boolean checkDoublants(String f1, int tab[]) throws IOException, DuplicatedException {

        Scanner input = new Scanner(System.in);
        XSSFWorkbook wB = new XSSFWorkbook(new FileInputStream(f1));
        XSSFSheet sheet = wB.getSheetAt(0);
        Row row;
        Iterator<Row> rowIterator = sheet.iterator();
        int ligne1 = 1;

        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            Iterator<Row> rowIterator2 = sheet.iterator();
            int ligne2 = 1;
            if (row.getCell(1).getStringCellValue() != "") {
                String str2 = row.getCell(1).getStringCellValue();
                Row row2 = rowIterator2.next();
                while (rowIterator2.hasNext()) {
                    if (row2.getCell(1).getStringCellValue().equals(str2) && ligne2 < ligne1 &&
                            !str2.equals("Matière : ") && !str2.equals("Enseignant : ") && !str2.equals("1ère Année SC")
                            && !str2.equals("Matrin") && !str2.equals("ESI : Ecole nationale Supérieure d'Informatique") && !str2.contains("Année")) {

                        String s3="\nUn Doublon trouvé :"+str2;
                        s3=s3+"\nPremière : ligne  " + ligne2 + " , Nom : "
                                + sheet.getRow(ligne2 - 1).getCell(2).getStringCellValue() + " Prénom : " +
                                sheet.getRow(ligne2 - 1).getCell(3).getStringCellValue();
                        s3=s3+"\nDeuxieme : ligne  " + ligne1 + " , Nom : "
                                + sheet.getRow(ligne1 - 1).getCell(2).getStringCellValue() + " Prénom : " +
                                sheet.getRow(ligne1 - 1).getCell(3).getStringCellValue();


                        s3=s3+"\nChoisissez l'élément que vous voulez supprimer : ";
                        tab[0]=0;
                        tab[1]=0;
                        tab[2]=ligne2-1;
                        tab[3]=ligne1-1;
                        s3=s3+"\n1- Nom : " + sheet.getRow(ligne2 - 1).getCell(2).getStringCellValue() + " || Prénom : " +
                                sheet.getRow(ligne2 - 1).getCell(3).getStringCellValue();
                        s3=s3+"\n2- Nom : " + sheet.getRow(ligne1 - 1).getCell(2).getStringCellValue() + " || Prénom : " +
                                sheet.getRow(ligne1 - 1).getCell(3).getStringCellValue();
                        throw new DuplicatedException(s3);
                        //System.out.println(s3);

                        //int a = input.nextInt();
                        //if (a==1) DeleteDuplicate(f1,tab[0],tab[2]);
                        //else DeleteDuplicate(f1,tab[1],tab[3]);
                        //return false;
                        /*Row r = null;
                        Row r2 = null;

                        if (a == 1) {
                            r = sheet.getRow(ligne2 - 1);
                            r2 = sheet.getRow(ligne2);
                            int ii = ligne2;
                            boolean b = false;
                            while (r2 != null || b == false) {
                                if (r2 == null) {
                                    b = true;
                                    sheet.removeRow(r);
                                } else {
                                    copyRow(r2, r);
                                    r = sheet.getRow(ii);
                                    ii++;
                                    r2 = sheet.getRow(ii);
                                }
                            }
                        } else {
                            r = sheet.getRow(ligne1 - 1);
                            r2 = sheet.getRow(ligne1);
                            int ii = ligne1;
                            boolean b = false;
                            while (r2 != null || b == false) {
                                if (r2 == null) {
                                    b = true;
                                    sheet.removeRow(r);
                                } else {
                                    copyRow(r2, r);
                                    r = sheet.getRow(ii);
                                    ii++;
                                    r2 = sheet.getRow(ii);
                                }
                            }
                        }


                        FileOutputStream outFile = new FileOutputStream(f1);
                        wB.write(outFile);
                        return (false);*/
                    }
                    row2 = rowIterator2.next();
                    ligne2++;
                }
            }
            ligne1++;

        }
        return (true);
    }



    public static void copyRow(Row r1,Row r2){// copies the row1 in the row2

        for(int i=r1.getFirstCellNum();i<=r1.getLastCellNum();i++) {

            Cell c = r2.createCell(i);
            if (r1 != null) if (r1.getCell(i) != null) {
                if (r1.getCell(i).getCellType() == XSSFCell.CELL_TYPE_NUMERIC)
                    c.setCellValue(r1.getCell(i).getNumericCellValue());
                else c.setCellValue(r1.getCell(i).getStringCellValue());
                c.setCellStyle(r1.getCell(i).getCellStyle());
            }
        }
    }

    public void DeleteDuplicate(String f1, int nbSheet, int nbRow) throws IOException{
        XSSFWorkbook wB = new XSSFWorkbook(new FileInputStream(f1));
        XSSFSheet sheet = wB.getSheetAt(nbSheet);
        Row r = sheet.getRow(nbRow);
        Row r2 = sheet.getRow(nbRow+1);
        int ii = nbRow+1;
        boolean b = false;
        while (r2 != null || b == false) {
            if (r2 == null) {
                b = true;
                sheet.removeRow(r);
            } else {
                copyRow(r2, r);
                r = sheet.getRow(ii);
                ii++;
                r2 = sheet.getRow(ii);
            }
        }

        FileOutputStream outFile = new FileOutputStream(f1);
        wB.write(outFile);
    }

    public void checkDouble(String f1) throws Exception {
        boolean b=false;
        int[] tab=new int[4];
        for(int i=0;i<4;i++)
            tab[i]=0;

        while (b==false) {
            b=checkDoublants(f1,tab);
        }
    }

    public boolean checkOption(String f1, String option) throws IOException, WrongOptionException, RightOptionException {
        XSSFWorkbook wB = new XSSFWorkbook(new FileInputStream(f1)) ;
        int cpt=0;
        for(int i=0;i< wB.getSheetAt(0).getLastRowNum();i++){
            Row r=wB.getSheetAt(0).getRow(i);
            if (r!=null)
            if(Util.getInstance().existInRow(r,option)) cpt++;
            else cpt=0;
            else cpt=0;
            if (cpt==5) throw new RightOptionException("Option found !");
        }
        throw new WrongOptionException("Cannot find the option in  this file");
    }

    public static void main(String[] args) throws Exception {
        boolean x,y;
        SchoolServiceFormatChecker ssf=new SchoolServiceFormatChecker();
       /* try {
            try {
                x=ssf.checkFormat("t.xlsx");
            } catch (NoLineFoundException e) {
                e.printStackTrace();
            }
            // Liste_Etudiants_1CS_S1_2016-2017.xlsx
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MissingFieldsException exception1) {
            exception1.printStackTrace();
        }*/

           ssf.checkDouble("Listes-Etudiants_2CPI_S1_2016-2017(1) (2).xlsx");
            ssf.checkOption("Listes-Etudiants_2CPI_S1_2016-2017(1) (2).xlsx","SCc");
        System.out.println("that's it !");


    }
}