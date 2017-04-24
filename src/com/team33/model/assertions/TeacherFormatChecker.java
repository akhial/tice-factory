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
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 * Created by Mitchell on 13/02/2017.
 */
public class TeacherFormatChecker implements ExcelFormat {


    public boolean checkFormat(File f1) {

        XSSFWorkbook wB = null;
        try {
            wB = new XSSFWorkbook(new FileInputStream(f1));
        } catch (IOException e) {
            e.printStackTrace();
        }
        XSSFSheet sheet = wB.getSheetAt(0);
        Row row;
        Cell cell;
        Iterator<Row> rowIterator = sheet.iterator();
        try {


            row = rowIterator.next();

            while (rowIterator.hasNext()) {
                if (Util.getInstance().existInRow(row, "NOM") || Util.getInstance().existInRow(row, "PRENOM") ||
                        Util.getInstance().existInRow(row, "MATIERE") || Util.getInstance().existInRow(row, "COURS\nSEMESTRE1") ||
                        Util.getInstance().existInRow(row, "TD\nSEMESTRE1") || Util.getInstance().existInRow(row, "COURS\nSEMESTRE2") ||
                        Util.getInstance().existInRow(row, "TD\nSEMESTRE2") || Util.getInstance().existInRow(row, "Projet\nSEMESTRE2")
                        ) {

                    if (!Util.getInstance().existInRow(row, "NOM") || !Util.getInstance().existInRow(row, "PRENOM") ||
                            !Util.getInstance().existInRow(row, "MATIERE") || !Util.getInstance().existInRow(row, "COURS\nSEMESTRE1") ||
                            !Util.getInstance().existInRow(row, "TD\nSEMESTRE1") || !Util.getInstance().existInRow(row, "COURS\nSEMESTRE2") ||
                            !Util.getInstance().existInRow(row, "TD\nSEMESTRE2") || !Util.getInstance().existInRow(row, "Projet\nSEMESTRE2")
                            ) {
                        System.out.println("Missing one or more fields in the File, failed !");
                        if (!Util.getInstance().existInRow(row, "NOM"))
                            System.out.println("- Nom");
                        if (!Util.getInstance().existInRow(row, "PRENOM")) System.out.println("- PRENOM");
                        if (!Util.getInstance().existInRow(row, "MATIERE")) System.out.println("- MATIERE");
                        if (!Util.getInstance().existInRow(row, "COURS\nSEMESTRE1"))
                            System.out.println("- COURS SEMESTRE1");
                        if (!Util.getInstance().existInRow(row, "COURS\nSEMESTRE2"))
                            System.out.println("- COURS SEMESTRE2");
                        if (!Util.getInstance().existInRow(row, "TD\nSEMESTRE1")) System.out.println("- TD SEMESTRE1");
                        if (!Util.getInstance().existInRow(row, "TD\nSEMESTRE2")) System.out.println("- TD SEMESTRE2");
                        if (!Util.getInstance().existInRow(row, "Projet\nSEMESTRE2"))
                            System.out.println("- Projet SEMESTRE2");

                        return (false);

                    } else { System.out.println("File match !");
                        return (true);
                       }


                } else row = rowIterator.next();


            }


        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    return(false);
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
                        String str2 = row.getCell(0).getStringCellValue();
                        String str3 = row.getCell(1).getStringCellValue();
                        Row row2 = rowIterator2.next();
                        while (rowIterator2.hasNext()) {
                            if (row2.getCell(0).getStringCellValue().equals(str2) && ligne2<ligne1 && row2.getCell(1).getStringCellValue().equals(str3)) {
                                System.out.println("---------------------------------------------------------------------------------------------------------------");

                                System.out.println("Duplicate : " + str2 +" " +str3);
                                System.out.println("first : line  "+ ligne2 );
                                System.out.println("Second : line  "+ ligne1 );
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



    public static void main(String[] args) {
        boolean x,y;

        File f1 = new File("charges_enseignants_f__vrier2017_2016-2017__1_.xlsx");
        TeacherFormatChecker ssf=new TeacherFormatChecker();

        x=ssf.checkFormat(f1);
        y=ssf.checkDoublants(f1);
        System.out.println("that's it !");
    }
}