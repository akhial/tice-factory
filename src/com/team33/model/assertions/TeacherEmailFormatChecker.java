package com.team33.model.assertions;

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
 * Created by Mitchell on 07/03/2017.
 */
public class TeacherEmailFormatChecker implements  ExcelFormat {
    @Override

    public boolean checkDoublants(File f1) {
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
                    String str2 = row.getCell(1).getStringCellValue();
                    Row row2 = rowIterator2.next();
                    while (rowIterator2.hasNext()) {
                        if (row2.getCell(1).getStringCellValue().equals(str2) && ligne2 < ligne1) {
                            System.out.println("---------------------------------------------------------------------------------------------------------------");
                            System.out.println("Duplicate : " + str2 );
                            System.out.println("first : line  "+ ligne2 + " , Nom du prof : " + sheet.getRow(ligne2-1).getCell(0).getStringCellValue());
                            System.out.println("Second : line  "+ ligne1 + " , Nom du prof : " + sheet.getRow(ligne1-1).getCell(0).getStringCellValue());
                            //System.out.println("Choisissez une operation : ");
                            //System.out.println("1- Supprimer un doublant" );
                            //System.out.println("2- Re-introduire le e-mail : ");
                        }
                        row2 = rowIterator2.next();
                        ligne2++;
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

    public boolean checkFormat(File f1) {

        XSSFWorkbook wB = null;
        int x=0;
        try {
            wB = new XSSFWorkbook(new FileInputStream(f1));
        } catch (IOException e) {
            e.printStackTrace();
        }
        XSSFSheet sheet = wB.getSheetAt(0);
        Row row;
        Iterator<Row> rowIterator = sheet.iterator();
        try {
            row = rowIterator.next();
            while (rowIterator.hasNext()) {

                String str2 = row.getCell(1).getStringCellValue();
                if (!str2.contains("@")) {
                    System.out.println("La ligne " + (x+1) + " ne contient pas un e-mail valide !");
                    return (false);
                }


                row = rowIterator.next();
                x++;

            }
        }

        catch (NullPointerException ex) {

            System.out.println("la ligne " + (x+1) +" contient des cases vides !!" );
            return(false);
        }
        catch (Exception ioe) {
            ioe.printStackTrace();
        }
        System.out.println("File match !");
        return(true);
    }

    public static void main(String[] args) {
        boolean x, y;
        File f1 = new File("emails_enseignants.xlsx");
        TeacherEmailFormatChecker ssf=new TeacherEmailFormatChecker();
        x=ssf.checkFormat(f1);
        y=ssf.checkDoublants(f1);
        System.out.println("over here !");
    }
}
