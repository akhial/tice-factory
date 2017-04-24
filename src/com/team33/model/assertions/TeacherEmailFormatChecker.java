package com.team33.model.assertions;

import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by Mitchell on 07/03/2017.
 */
public class TeacherEmailFormatChecker implements  ExcelFormat {

    @Override
    public boolean checkDoublants(String f1, int tab[]) throws Exception {
        Scanner input=new Scanner(System.in);
            XSSFWorkbook wB = new XSSFWorkbook(new FileInputStream(f1));
            XSSFSheet sheet = wB.getSheetAt(0);
            Row row;
            Iterator<Row> rowIterator = sheet.iterator();
                int ligne1=1;

                while (rowIterator.hasNext()) {
                    row = rowIterator.next();
                    Iterator<Row> rowIterator2 = sheet.iterator();
                    int ligne2=1;
                    String str2 = row.getCell(1).getStringCellValue();
                    Row row2 = rowIterator2.next();
                    while (rowIterator2.hasNext()) {
                        if (row2.getCell(1).getStringCellValue().equals(str2) && ligne2 < ligne1) {

                            String s3="Un doublon trouvé : " + str2;
                            tab[0]=0;
                            tab[1]=0;
                            tab[2]=ligne2-1;
                            tab[3]=ligne1-1;
                            s3=s3+"\nfirst : ligne  "+ ligne2 + " , Nom du prof : " + sheet.getRow(ligne2-1).getCell(0).getStringCellValue();
                            s3=s3+"\nDeuxième : ligne  "+ ligne1 + " , Nom du prof : " + sheet.getRow(ligne1-1).getCell(0).getStringCellValue();
                            s3=s3+"\nChoisissez l'élement que vous voulez supprimer : ";
                            s3=s3+"\n1- "+sheet.getRow(ligne2-1).getCell(0).getStringCellValue();
                            s3=s3+"\n2- "+sheet.getRow(ligne1-1).getCell(0).getStringCellValue();
                            throw new DuplicatedException(s3);
                           /* int a=input.nextInt();
                            if(a==1) DeleteDuplicate(f1,tab[0],tab[2]);
                            else DeleteDuplicate(f1,tab[1],tab[3]);
                            return(false);
                           /* Row r=null;
                            Row r2=null;

                            if (a==1)  {r=sheet.getRow(ligne2-1);
                                r2=sheet.getRow(ligne2);
                                int ii=ligne2;
                                boolean b=false;
                                while (r2!=null || b==false  ) {
                                    if (r2==null){ b=true; sheet.removeRow(r);}
                                    else {
                                        copyRow(r2, r);
                                        r = sheet.getRow(ii);
                                        ii++;
                                        r2 = sheet.getRow(ii);
                                    }
                                }
                            }
                            else  {r=sheet.getRow(ligne1-1);
                                r2=sheet.getRow(ligne1);
                                int ii=ligne1;
                                boolean b=false;
                                while (r2!=null || b==false  ) {
                                    if (r2==null){ b=true; sheet.removeRow(r);}
                                    else {
                                        copyRow(r2, r);
                                        r = sheet.getRow(ii);
                                        ii++;
                                        r2 = sheet.getRow(ii);
                                    }
                                }}*/
                        }
                        row2 = rowIterator2.next();
                        ligne2++;
                    }
                    ligne1++;
                }
                return true;
            }


    public boolean checkFormat(String f1) throws IOException, NoSuchElementException, MissingFieldsException {
        XSSFWorkbook wB = null;
        int x=0;
        wB = new XSSFWorkbook(new FileInputStream(f1));

        XSSFSheet sheet = wB.getSheetAt(0);
        if (wB.getSheetAt(0).getRow(wB.getSheetAt(0).getLastRowNum())==null)
            throw new NoSuchElementException("Fichier Vide !");
        Row row;
        Iterator<Row> rowIterator = sheet.iterator();
        row = rowIterator.next();
        if (row.getCell(1)==null) throw new NullPointerException("la ligne " + (x+1) +" contient des cases vides !!" );
        while (rowIterator.hasNext()) {
            if (row.getCell(1)==null) throw new NullPointerException("la ligne " + (x+1) +" contient des cases vides !!" );
            String str2 = row.getCell(1).getStringCellValue();
            if (!str2.contains("@")) {
                throw new MissingFieldsException("La ligne " + (x+1) + " ne contient pas un e-mail valide !");
            }
            row = rowIterator.next();
            x++;

        }
        return true;
    }



    public static void copyRow(Row r1,Row r2) throws IOException {// copies the row1 in the row2

        for (int i = r1.getFirstCellNum(); i <= r1.getLastCellNum(); i++) {
            Cell c = r2.createCell(i);
            //System.out.println(r1.getCell(i).getStringCellValue()); // just for test
            if (r1 != null) if (r1.getCell(i) != null) {

                c.setCellValue(r1.getCell(i).getStringCellValue());
                c.setCellStyle(r1.getCell(i).getCellStyle());

            }
        }
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

    public void DeleteDuplicate(String f1, int nbSheet, int nbRow) throws IOException {
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

    public static void main(String[] args) throws Exception {
        boolean x, y;
        TeacherEmailFormatChecker ssf=new TeacherEmailFormatChecker();
        //x=ssf.checkFormat("emails_enseignants.xlsx");
        //y=ssf.checkDoublants("emails_enseignants.xlsx");
        ssf.checkDouble("emails_enseignants.xlsx");
        System.out.println("over here !");
    }
}
