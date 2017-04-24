package com.team33.model.assertions;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by Mitchell on 13/02/2017.
 */
public class schoolServiceFormatChecker implements ExcelFormat {



    public boolean checkFormat(File f1) {

        try {

            XSSFWorkbook wB = new XSSFWorkbook(new FileInputStream(f1));
            XSSFSheet sheet = wB.getSheetAt(0);
            Row row;
            int cpt=1;
            Cell cell;
            Iterator<Row> rowIterator = sheet.iterator();
            row = rowIterator.next();
            while (rowIterator.hasNext()) {
                if (Util.getInstance().existInRow(row,"Nom") || Util.getInstance().existInRow(row,"Prenom") ||
                        Util.getInstance().existInRow(row,"Optin") || Util.getInstance().existInRow(row,"Moyenne")
                        || Util.getInstance().existInRow(row,"NG") || Util.getInstance().existInRow(row,"NS") ||
                        Util.getInstance().existInRow(row,"CI") || Util.getInstance().existInRow(row,"CF") ||
                        Util.getInstance().existInRow(row,"CC")
                        || Util.getInstance().existInRow(row,"TP") || Util.getInstance().existInRow(row,"Num") ||
                        Util.getInstance().existInRow(row,"Matrin") || Util.getInstance().existInRow(row,"Anetin")
                        )
                {
                    if (!Util.getInstance().existInRow(row,"Nom") || !Util.getInstance().existInRow(row,"Prenom") ||
                        !Util.getInstance().existInRow(row,"Optin") || !Util.getInstance().existInRow(row,"NG"))
                {
                    System.out.println("Missing Fields in line number :" + cpt);
                    if (!Util.getInstance().existInRow(row,"Nom")) System.out.println("- Nom ");
                    if (!Util.getInstance().existInRow(row,"Prenom")) System.out.println("- Prenom ");
                    if (!Util.getInstance().existInRow(row,"NG")) System.out.println("- NG ");
                    if (!Util.getInstance().existInRow(row,"Anetin")) System.out.println("- Anetin ");
                    return(false);
                }}
                row = rowIterator.next();
                cpt++;


            }
        }
        catch (Exception ioe) {
            ioe.printStackTrace();
        }
        System.out.println("File match !");
    return(true);
    }

    @Override
    public boolean checkDoublants(File f1) {
        Scanner input=new Scanner(System.in);
        try {
            XSSFWorkbook wB = new XSSFWorkbook(new FileInputStream(f1));
            XSSFSheet sheet = wB.getSheetAt(0);
            Row row;
            Iterator<Row> rowIterator = sheet.iterator();
            try {
                int ligne1=1;

                while (rowIterator.hasNext()) {
                    row = rowIterator.next();
                    Iterator<Row> rowIterator2 = sheet.iterator();
                    int ligne2=1;
                    if (row.getCell(1).getStringCellValue()!="") {
                        String str2 = row.getCell(1).getStringCellValue();
                        Row row2 = rowIterator2.next();
                        while (rowIterator2.hasNext()) {
                            if (row2.getCell(1).getStringCellValue().equals(str2) && ligne2 < ligne1 &&
                                    !str2.equals("Matière : ") && !str2.equals("Enseignant : ") && !str2.equals("1ère Année SC")
                                    && !str2.equals("Matrin") && !str2.equals("ESI : Ecole nationale Supérieure d'Informatique") && !str2.contains("Année")) {
                                System.out.println("---------------------------------------------------------------------------------------------------------------");

                                System.out.println("Duplicate : " + str2);
                                System.out.println("first : line  " + ligne2 + " , Nom : "
                                        + sheet.getRow(ligne2 - 1).getCell(2).getStringCellValue() + " Prenom : "+
                                        sheet.getRow(ligne2-1).getCell(3).getStringCellValue());
                                System.out.println("first : line  " + ligne1 + " , Nom : "
                                        + sheet.getRow(ligne1 - 1).getCell(2).getStringCellValue() + " Prenom : "+
                                        sheet.getRow(ligne1-1).getCell(3).getStringCellValue());
                                //System.out.println("Choisissez une operation : ");
                                //System.out.println("1- Supprimer un doublant" );
                                //System.out.println("2- Re-introduire le e-mail : ");
                            }
                            row2 = rowIterator2.next();
                            ligne2++;
                        }
                    }
                    ligne1++;
                }
            }
            catch (NoSuchElementException exe) {
                System.out.println("No such emelement " );
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return(true);
    }

    public static void main(String[] args) {
        boolean x,y;
        File f1 = new File("Liste_Etudiants_1CS_S1_2016-2017.xlsx");
        schoolServiceFormatChecker ssf=new schoolServiceFormatChecker();
        x=ssf.checkFormat(f1);
        y=ssf.checkDoublants(f1);
        System.out.println("that's it !");


    }
}