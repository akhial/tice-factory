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
public class TeacherFormatChecker implements ExcelFormat  {


    public boolean checkFormat(String f1) throws IOException, NoLineFoundException, MissingFieldsException {

        XSSFWorkbook wB = null;
        wB = new XSSFWorkbook(new FileInputStream(f1));
        if (wB.getSheetAt(0).getRow(wB.getSheetAt(0).getLastRowNum())==null)
            throw new NoSuchElementException("Fichier Vide !");
        XSSFSheet sheet = wB.getSheetAt(0);
        Row row;
        Cell cell;
        Iterator<Row> rowIterator = sheet.iterator();
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
                    String str1 = "Champ(s) Introuvable(s) : ";
                    if (!Util.getInstance().existInRow(row, "NOM"))
                        str1 = str1 + "\n- Nom";
                    if (!Util.getInstance().existInRow(row, "PRENOM")) str1 = str1 + "\n- Prenom";
                    if (!Util.getInstance().existInRow(row, "MATIERE")) str1 = str1 + "\n- Matière";
                    if (!Util.getInstance().existInRow(row, "COURS\nSEMESTRE1"))
                        str1 = str1 + "\n- Cours Semestre1";
                    if (!Util.getInstance().existInRow(row, "COURS\nSEMESTRE2"))
                        str1 = str1 + "\n- Cours Semestre2";
                    if (!Util.getInstance().existInRow(row, "TD\nSEMESTRE1")) str1 = str1 + "\n- TD Semestre1";
                    if (!Util.getInstance().existInRow(row, "TD\nSEMESTRE2")) str1 = str1 + "\n- TD Semestre1";
                    if (!Util.getInstance().existInRow(row, "Projet\nSEMESTRE2"))
                        str1 = str1 + "\n- Projet Semestre2";

                    throw new MissingFieldsException(str1);

                }


            } else row = rowIterator.next();
            return true;

        }
        throw new NoLineFoundException("Header de la table non-trouvé ! !");
    }

    @Override
    public boolean checkDoublants(String f1, int tab[]) throws IOException, DuplicatedException {
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
                        String str2 = row.getCell(0).getStringCellValue();
                        String str3 = row.getCell(1).getStringCellValue();
                        Row row2 = rowIterator2.next();
                        while (rowIterator2.hasNext()) {
                            if (row2.getCell(0).getStringCellValue().equals(str2) && ligne2<ligne1 && row2.getCell(1).getStringCellValue().equals(str3)) {

                                String s3="Un doublon trouvé : " + str2 +" " +str3;
                                tab[0]=0;
                                tab[1]=0;
                                tab[2]=ligne2-1;
                                tab[3]=ligne1-1;
                                s3=s3+"\nPremière : ligne  "+ ligne2;
                                s3=s3+"\nDeuxième : ligne  "+ ligne1;
                                s3=s3+"\nChoisissez l'élement que vous voulez supprimer : ";
                                s3=s3+"\n1- "+sheet.getRow(ligne2-1).getCell(0).getStringCellValue()+ " || Nom "+
                                        sheet.getRow(ligne2-1).getCell(1).getStringCellValue();
                                s3=s3+"\n2- "+ sheet.getRow(ligne1-1).getCell(0).getStringCellValue()+" || Prenom : "+
                                        sheet.getRow(ligne1-1).getCell(1).getStringCellValue();
                                throw new DuplicatedException(s3);
                                /*int a=input.nextInt();
                                if (a==1) DeleteDuplicate(f1,0,tab[2]);
                                else DeleteDuplicate(f1,0,tab[3]);
                                return false;
                                /*Row r=null;
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




    public static void copyRow(Row r1,Row r2)  {// copies the row1 in the row2

        for (int i = r1.getFirstCellNum(); i <= r1.getLastCellNum(); i++) {
            Cell c = r2.createCell(i);
            //System.out.println(r1.getCell(i).getStringCellValue()); // just for test
            if (r1 != null) if (r1.getCell(i) != null) {

                c.setCellValue(r1.getCell(i).getStringCellValue());
                c.setCellStyle(r1.getCell(i).getCellStyle());

            }
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

    public void checkDouble(String f1) throws IOException, DuplicatedException {
        boolean b=false;
        int[] tab=new int[4];
        for(int i=0;i<4;i++)
            tab[i]=0;

        while (b==false) {
            b=checkDoublants(f1,tab);
        }
    }


    public static void main(String[] args) throws NoLineFoundException, MissingFieldsException, IOException, DuplicatedException {
        boolean x,y;
        TeacherFormatChecker ssf=new TeacherFormatChecker();

       // x=ssf.checkFormat("charges_enseignants_f__vrier2017_2016-2017__1_.xlsx");
        ssf.checkDouble("charges_enseignants_f__vrier2017_2016-2017__1_.xlsx");
        System.out.println("that's it !");
    }
}