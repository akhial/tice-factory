package com.team33.model.assertions;

import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileNotFoundException;
import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by Amine on 14/02/2017.
 */
public class WebServiceFormatChecker implements ExcelFormat{



    public boolean checkSheet(String f1, String Niveau) throws IOException {


        XSSFWorkbook wB = new XSSFWorkbook(new FileInputStream(f1)) ;
            for (int i = 0; i < wB.getNumberOfSheets();i++) {
                if (Niveau.equals(wB.getSheetName(i))) {
                return (true);}
            }
        return(false);

        }

    public boolean checkSheetS(String f1) throws IOException, SheetsFoundException, MissingSheetsException {
        if (checkSheet(f1,"1cpi")&& checkSheet(f1,"2cpi") && checkSheet(f1,"1cs") &&
                checkSheet(f1,"2CSSIL")&& checkSheet(f1,"2CSSIT") && checkSheet(f1,"2CSSIQ") &&
                checkSheet(f1,"3CSSIT")&& checkSheet(f1,"3CSSIT") && checkSheet(f1,"3CSSIQ"))
            throw new SheetsFoundException("Tous les sheets sont trouvés !");
        else {
            String s2="Missing sheets in the file :";
            if (!checkSheet(f1,"1cpi")) s2=s2+" 1cpi,";
            if (!checkSheet(f1,"2cpi")) s2=s2+" 2cpi,";
            if (!checkSheet(f1,"1cs")) s2=s2+" 1cs,";
            if (!checkSheet(f1,"2CSSIL")) s2=s2+" 2CSSIL, ";
            if (!checkSheet(f1,"2CSSIT")) s2=s2+" 2CSSIT, ";
            if (!checkSheet(f1,"2CSSIQ")) s2=s2+" 2CSSIQ, ";
            if (!checkSheet(f1,"3CSSIL")) s2=s2+" 3CSSIL, ";
            if (!checkSheet(f1,"3CSSIT")) s2=s2+" 3CSSIT, ";
            if (!checkSheet(f1,"3CSSIQ")) s2=s2+" 3CSSIQ, ";

            throw new MissingSheetsException(s2);
        }

        }



    @Override
    public boolean checkFormat(String f1) throws IOException, MissingFieldsException, NoSuchElementException {
            int cpt=0;
            XSSFWorkbook wB = new XSSFWorkbook(new FileInputStream(f1));
           if (wB.getSheetAt(0).getRow(wB.getSheetAt(0).getLastRowNum())==null)
               throw new NoSuchElementException("Fichier vide !");

            // throw new NoSuchElementException("Empty file");
            for (int i = 0; i < wB.getNumberOfSheets(); i++) {
                if (cpt==100)
                {
                    return true;
                }
                XSSFSheet sheet = wB.getSheetAt(i);
                Row row;
                Iterator<Row> rowIterator = sheet.iterator();
                row = rowIterator.next();
                while (rowIterator.hasNext() && cpt<100) {
                    if ((row.getCell(2)) != null) {
                        if (row.getCell(2).getStringCellValue().contains("@esi.dz")) cpt++;
                    }
                    row = rowIterator.next();
                }
            }
        throw new MissingFieldsException("Champ E-mail vide !");
        }

    @Override
        public boolean checkDoublants(String f1,int tab[]) throws Exception {
        Scanner input = new Scanner(System.in);

            XSSFWorkbook wB = new XSSFWorkbook(new FileInputStream(f1));
            int nbSheet = wB.getNumberOfSheets();
            for (int i = 0; i < nbSheet; i++){
                XSSFSheet sheet = wB.getSheetAt(i);
            Row row;

                int ligne1 = 1;

                for (ligne1=0;ligne1<sheet.getLastRowNum();ligne1++) {
                    row=sheet.getRow(ligne1);
                    //
                    for(int isheet=0;isheet<=i;isheet++) {
                        XSSFSheet sheet2 = wB.getSheetAt(isheet);
                        int ligne2 ;
                        if (row!=null) {
                            if (row.getCell(2) != null && row.getCell(1) != null) {
                                if (row.getCell(2).getStringCellValue() != "") {

                                    String str2 = row.getCell(2).getStringCellValue();
                                    for (ligne2 = 0; ligne2 < sheet.getLastRowNum(); ligne2++) {
                                        Row row2 = sheet2.getRow(ligne2);
                                        if (row2!=null) {
                                            if (row2.getCell(2) != null && row2.getCell(1) != null) {
                                                if (row2.getCell(2).getStringCellValue().equals(str2) && (isheet < i || (isheet == i && ligne2 < ligne1)) &&
                                                        !str2.equals("Adresse e-mail")) {

                                                    String s3="Un doublon trouvé : " + str2;
                                                    s3=s3+"\nPremière : ligne  " + (ligne2 + 1) + " sheet : " + wB.getSheetName(isheet) + " Nom d'utilisateur : "
                                                            + sheet2.getRow(ligne2).getCell(1).getStringCellValue();
                                                    s3=s3+"\nDeuxième : ligne  " + (ligne1 + 1) + " sheet : " + wB.getSheetName(i) + " Nom d'utilisateur : "
                                                            + sheet.getRow(ligne1).getCell(1).getStringCellValue();
                                                    tab[0]=i;
                                                    tab[1]=isheet;
                                                    tab[2]=ligne1;
                                                    tab[3]=ligne2;
                                                    s3=s3+"\nChoisissez l'element que vous voulez supprimer : ";
                                                    s3=s3+"\n1- "+sheet2.getRow(ligne2).getCell(1).getStringCellValue()+ " || E-mail : "+
                                                            sheet2.getRow(ligne2).getCell(2).getStringCellValue();
                                                    s3=s3+"\n2- "+ sheet.getRow(ligne1).getCell(1).getStringCellValue()+" || Email : "+
                                                            sheet.getRow(ligne1).getCell(2).getStringCellValue();
                                                    throw new DuplicatedException(s3);
                                                  /*  int a=input.nextInt();
                                                    if (a==1) DeleteDuplicate(f1,tab[1],tab[3]);
                                                    else DeleteDuplicate(f1,tab[0],tab[2]);
                                                    return false;
                                                   /* Row r=null;
                                                    Row r2=null;

                                                    if (a==1)  {r=sheet2.getRow(ligne2);
                                                        r2=sheet2.getRow(ligne2+1);
                                                   int ii=ligne2+1;
                                                    boolean b=false;
                                                        while (r2!=null || b==false  ) {
                                                            if (r2==null){ b=true; sheet2.removeRow(r);}
                                                            else {
                                                                copyRow(r2, r);
                                                                r = sheet2.getRow(ii);
                                                                ii++;
                                                                r2 = sheet2.getRow(ii);
                                                            }
                                                        }
                                                    }
                                                    else  {r=sheet.getRow(ligne1);
                                                        r2=sheet.getRow(ligne1+1);
                                                        int ii=ligne1+1;
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
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

        }
        return(true);
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

    public static void main(String[] args) throws Exception {
        boolean x,y;
        WebServiceFormatChecker ssf = new WebServiceFormatChecker();
   /*     try {
            ssf.checkSheetS("liste_email_tous_les_etudiants.xlsx");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SheetsFoundException e) {
            e.printStackTrace();
        } catch (MissingSheetsException e) {
            e.printStackTrace();
        }
        // x = ssf.checkSheetS("liste_email_tous_les_etudiants.xlsx");
        try {
            boolean b=ssf.checkFormat("t.xlsx");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FileAcceptedException e) {
            e.printStackTrace();
        } catch (MissingFieldsException e) {
            e.printStackTrace();
        }*/
        ssf.checkDouble("liste_email_tous_les_etudiants.xlsx");

    }
}