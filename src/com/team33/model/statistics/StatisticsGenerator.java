package com.team33.model.statistics; /**
 * Created by dell on 24/04/2017.
 */
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeSet;

/**
 * Created by Amine on 18/02/2017.
 */
public class StatisticsGenerator {
    public final int EVENT_NAME = 5;
    public final int EVENT_CONTEXT = 3;

    public static void trierFichierExcel(String filePath, String outputPath, int key) throws Exception {
        int nbRows, firstCol, firstRow, lastCol;
        XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(filePath)));
        Sheet sheet = wb.getSheetAt(0);
        nbRows = sheet.getLastRowNum();

        firstRow = sheet.getFirstRowNum();
        Row row = sheet.getRow(firstRow);
        lastCol = row.getLastCellNum();
        //System.out.println("  " + nbRows + "  " + "   " + firstRow + "   " + lastCol);
        AsposeDataSort sorter = new AsposeDataSort();
        sorter.sortAWorksheet(filePath, outputPath, key, firstRow + 1, 0, nbRows, lastCol);// changed it to 3
        //filePath= "trié.xlsx";
        outputPath = deleteSheet(outputPath);

    }

    public static String deleteSheet(String filePath) throws Exception {
        FileInputStream fileStream = new FileInputStream(filePath);
        //      POIFSFileSystem fsPoi = new POIFSFileSystem(fileStream);
        XSSFWorkbook workbook = new XSSFWorkbook(fileStream);
        int index = 0;
        XSSFSheet sheet = workbook.getSheet("Evaluation Warning");
        if (sheet != null) {
            index = workbook.getSheetIndex(sheet);
            workbook.removeSheetAt(index);
        }
        FileOutputStream output = new FileOutputStream(filePath);
        workbook.write(output);
        output.close();
        return filePath;
    }

    public static String dateFormat(String date, int format) {
        if (date.equals("") || (date.length() == 0) || (!date.contains("/"))) {
            return date;
        }

        switch (format) {
            case 0:
                return date;

            case 1:
                StringBuffer retour = new StringBuffer("");
                retour.append(date.split("(/)|(,)")[0]);
                retour.append("/");
                retour.append(date.split("(/)|(,)")[1]);
                retour.append("/");
                retour.append(date.split("(/)|(,)")[2]);
                return retour.toString();

            case 2:
                retour = new StringBuffer("");
                retour.append(date.split("(/)|(,)")[1]);
                retour.append("/");
                retour.append(date.split("(/)|(,)")[2]);
                return retour.toString();

            default:
                return date;
        }
    }

    public static void selectDates(String dateRef1, String dateRef2, String inputFile, String outputFile) throws Exception {
        XSSFWorkbook wb = null;
        String searcher = "";
        String reference = "";
        int occurence = 0;
        //final String dateRef1="5/3/2017";
        //final String dateRef2="15/3/2017";
        //------------------------------
        String excelFileName = outputFile;//name of excel file
        String sheetName = "Sheet1";//name of sheet
        XSSFWorkbook w = new XSSFWorkbook();
        Sheet s = w.createSheet(sheetName);
        int index = 0;
        //---------------------------------
        wb = new XSSFWorkbook(new FileInputStream(new File(inputFile)));
        Sheet sheet = wb.getSheetAt(0);
            /*for (Sheet sh : wb) { */
        int i = sheet.getFirstRowNum();
        Row rw = sheet.getRow(i);
        Row r = s.createRow(index);
        copyRow(rw, r);
        index++;
        i = i + 1;
        String a = "";
        String b = "";

        while (i <= sheet.getLastRowNum()) {
            rw = sheet.getRow(i);
            a = rw.getCell(0).getStringCellValue();
            if ((compareDate((a.split(","))[0], dateRef1) == 1) && (compareDate((a.split(","))[0], dateRef2) == -1)) {
                //System.out.println("in");
                r = s.createRow(index);
                copyRow(rw, r);
                index++;
            }
            i++;
        }
        FileOutputStream fileOut = new FileOutputStream(excelFileName);
        w.write(fileOut);
        fileOut.flush();
        fileOut.close();

    }

    public static void copyRow(Row r1, Row r2) throws Exception {// copies the row1 in the row2

        for (int i = r1.getFirstCellNum(); i <= r1.getLastCellNum(); i++) {
            try {
            Cell c = r2.createCell(i);
            //System.out.println(r1.getCell(i).getStringCellValue()); // just for test
            c.setCellValue(r1.getCell(i).getStringCellValue());
        }catch (Exception e) {}
        }
    }

    public static int compareDate(String d1, String d2) throws Exception {
        SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yy");
        Date t1 = new Date();
        Date t2 = new Date();
        if (d1.equals("") || (d1.length() == 0) || (!d1.contains("/")) || d2.equals("") || (d2.length() == 0) || (!d2.contains("/"))) {
            return -2;
        } else if (d1.contains(d2)) {
            return (0);
        } else {
            //try {
            t1 = ft.parse(d1);
            t2 = ft.parse(d2);
            return t1.compareTo(t2);
            // } catch (Exception e) {e.printStackTrace();}
        }
        //return -2;
    }

    public static void coursStats(String inputFile, String outputFile, int formatChoisi) throws IOException {
        XSSFWorkbook wb = null;
        String searcher = "";
        String reference = "";
        int occurence = 0;
        //------------------------------
        String excelFileName = outputFile;//name of excel file
        String sheetName = "Sheet1";//name of sheet
        XSSFWorkbook w = new XSSFWorkbook();
        Sheet s = w.createSheet(sheetName);
        int index = 0;
        String compare;
        final int FORMATCHOISI = formatChoisi;
        //---------------------------------
        // try {
        wb = new XSSFWorkbook(new FileInputStream(new File(inputFile)));
        Sheet sheet = wb.getSheetAt(0);
            /*for (Sheet sh : wb) { */
        int i = sheet.getFirstRowNum() + 1;
        String a = "";
        String b = "";
        Row rw = sheet.getRow(i);
        boolean continu = true;
        while (i <= sheet.getLastRowNum() && continu) {
            rw = sheet.getRow(i);
            a = rw.getCell(3).getStringCellValue();
            b = dateFormat(rw.getCell(0).getStringCellValue(), FORMATCHOISI);
            //System.out.println(rw.getCell(3).getStringCellValue() + " before");
            if (a.contains("Course")) {
                occurence = 1;
                continu = false;
            }
            i++;
        }

        while (i <= sheet.getLastRowNum()) {
            rw = sheet.getRow(i);
            //System.out.println(rw.getCell(3).getStringCellValue());
            if (rw.getCell(3).getStringCellValue().contains("Course")) {
                if (rw.getCell(3).getStringCellValue().equals(a)) {//on voit la colonne F si elle correspond à notre reference
                    if(rw.getCell(0).getStringCellValue().contains(b)){// si oui on regarde la date
                        occurence++;
                    }
                }else{//si l'evenement est different on ecrit les anciennes donnees et on prend les nouvelles
                   // System.out.println(a+"    "+b);
                    // System.out.println(occurence);
                    Row r = s.createRow(index);
                    Cell d= r.createCell(0);
                    d.setCellValue(a);
                    Cell c = r.createCell(1);
                    c.setCellValue(occurence);
                    index++;
                    a=rw.getCell(3).getStringCellValue();
                    b=dateFormat(rw.getCell(0).getStringCellValue(),FORMATCHOISI);
                    occurence = 1;
                }}
            i++;
        }
        // ici c'est le cas du dernier élément
        Row r = s.createRow(index);
        Cell d = r.createCell(0);
        d.setCellValue(a);
        Cell c = r.createCell(1);
        c.setCellValue(occurence);
        index++;
        //a=rw.getCell(5).getStringCellValue();

        FileOutputStream fileOut = new FileOutputStream(excelFileName);
        w.write(fileOut);
        fileOut.flush();
        fileOut.close();

    }// catch (FileNotFoundException e) {
    // e.printStackTrace();
    //} catch (IOException e) {
    //      e.printStackTrace();
    //  } finally {
    //      try {
    //          if (wb != null) wb.close();
    //      } catch (IOException e) {
    //            e.printStackTrace();
    //       }
    //    }
    // }

    public static void generalStats(String inputFile, String outputFile, int formatChoisi) throws Exception {
        XSSFWorkbook wb = null;
        String searcher = "";
        String reference = "";
        int occurence;
        //------------------------------
        String excelFileName = outputFile;//name of excel file
        String sheetName = "Sheet1";//name of sheet
        XSSFWorkbook w = new XSSFWorkbook();
        Sheet s = w.createSheet(sheetName);
        int index = 0;
        String compare;
        final int FORMATCHOISI = formatChoisi;
        //---------------------------------
        //try {
        wb = new XSSFWorkbook(new FileInputStream(new File(inputFile)));
        Sheet sheet = wb.getSheetAt(0);
            /*for (Sheet sh : wb) { */
        boolean continu;
        int i = sheet.getFirstRowNum() + 1;
        String a, b, dCol;
        Row rw = sheet.getRow(i);
        a = rw.getCell(5).getStringCellValue();
        b = dateFormat(rw.getCell(0).getStringCellValue(), FORMATCHOISI);
        occurence = 1;
        dCol = rw.getCell(3).getStringCellValue();
        i++;
        while (i <= sheet.getLastRowNum()) {
            rw = sheet.getRow(i);
            //System.out.println(rw.getCell(5).getStringCellValue());
            if (rw.getCell(5).getStringCellValue().equals(a)) {

                if (rw.getCell(0).getStringCellValue().contains(b)) {

                    if (rw.getCell(3).getStringCellValue().equals(dCol)) {
                        occurence++;
                    } else {
                        //System.out.println(a + "    " + b);
                       //System.out.println(occurence);
                        Row r = s.createRow(index);
                        Cell d = r.createCell(0);
                        d.setCellValue(a);
                        Cell c = r.createCell(1);
                        //c.setCellValue(dateFormat(rw.getCell(0).getStringCellValue(),FORMATCHOISI));
                        c.setCellValue(dateFormat(b, FORMATCHOISI));
                        c = r.createCell(2);
                        c.setCellValue(occurence);
                        c = r.createCell(3);
                        c.setCellValue(dCol);
                        index++;
                        dCol = rw.getCell(3).getStringCellValue();
                        occurence = 1;
                    }
                } else {
                    //System.out.println(a + "    " + b);
                    //System.out.println(occurence);
                    Row r = s.createRow(index);
                    Cell d = r.createCell(0);
                    d.setCellValue(a);
                    Cell c = r.createCell(1);
                    //c.setCellValue(dateFormat(rw.getCell(0).getStringCellValue(),FORMATCHOISI));
                    c.setCellValue(dateFormat(b, FORMATCHOISI));
                    c = r.createCell(2);
                    c.setCellValue(occurence);
                    c = r.createCell(3);
                    c.setCellValue(dCol);
                    index++;
                    b = dateFormat(rw.getCell(0).getStringCellValue(), FORMATCHOISI);
                    dCol = rw.getCell(3).getStringCellValue();
                    occurence = 1;
                }
            } else {
                //System.out.println(a + "    " + b);
                //System.out.println(occurence);
                Row r = s.createRow(index);
                Cell d = r.createCell(0);
                d.setCellValue(a);
                Cell c = r.createCell(1);
                c.setCellValue(dateFormat(b, FORMATCHOISI));
                c = r.createCell(2);
                c.setCellValue(occurence);
                c = r.createCell(3);
                c.setCellValue(dCol);
                index++;
                a = rw.getCell(5).getStringCellValue();
                b = dateFormat(rw.getCell(0).getStringCellValue(), FORMATCHOISI);
                dCol = rw.getCell(3).getStringCellValue();
                occurence = 1;
            }
            i++;
        }

        Row r = s.createRow(index);
        Cell d = r.createCell(0);
        d.setCellValue(a);
        Cell c = r.createCell(1);
        c.setCellValue(dateFormat(rw.getCell(0).getStringCellValue(), FORMATCHOISI));
        c = r.createCell(2);
        c.setCellValue(occurence);
        c = r.createCell(3);
        c.setCellValue(dCol);
        index++;
//            a=rw.getCell(5).getStringCellValue();

        FileOutputStream fileOut = new FileOutputStream(excelFileName);
        w.write(fileOut);
        fileOut.flush();
        fileOut.close();

    }// catch (FileNotFoundException e) {
    //    e.printStackTrace();
    //  } catch (IOException e) {
    //      e.printStackTrace();
    //  } finally {
    //      try {
    ////          if (wb != null) wb.close();
    //       } catch (IOException e) {
    //           e.printStackTrace();
    //       }
    //   }
    //}

    public static void semiGeneralStats(String inputFile, String outputFile, int formatChoisi) throws Exception{
        XSSFWorkbook wb = null;
        String searcher = "";
        String reference = "";
        int occurence;
        //------------------------------
        String excelFileName = outputFile;//name of excel file
        String sheetName = "Sheet1";//name of sheet
        XSSFWorkbook w = new XSSFWorkbook();
        Sheet s = w.createSheet(sheetName);
        int index = 0;
        String compare;
        final int FORMATCHOISI = formatChoisi;
        //---------------------------------
        //try {
        wb = new XSSFWorkbook(new FileInputStream(new File(inputFile)));
        Sheet sheet = wb.getSheetAt(0);
            /*for (Sheet sh : wb) { */
        boolean continu;
        int i = sheet.getFirstRowNum() + 1;
        String a, b;
        Row rw = sheet.getRow(i);
        a = rw.getCell(5).getStringCellValue();
        b = dateFormat(rw.getCell(0).getStringCellValue(), FORMATCHOISI);
        occurence = 1;
        i++;
        while (i <= sheet.getLastRowNum()) {
            rw = sheet.getRow(i);
            //m.out.println(rw.getCell(5).getStringCellValue());
            if (rw.getCell(5).getStringCellValue().equals(a)) {

                if (rw.getCell(0).getStringCellValue().contains(b)) {
                        occurence++;

                } else {
                    //System.out.println(a + "    " + b);
                    //System.out.println(occurence);
                    Row r = s.createRow(index);
                    Cell d = r.createCell(0);
                    d.setCellValue(a);
                    Cell c = r.createCell(1);
                    //c.setCellValue(dateFormat(rw.getCell(0).getStringCellValue(),FORMATCHOISI));
                    c.setCellValue(dateFormat(b, FORMATCHOISI));
                    c = r.createCell(2);
                    c.setCellValue(occurence);
                    index++;
                    b = dateFormat(rw.getCell(0).getStringCellValue(), FORMATCHOISI);
                    occurence = 1;
                }
            } else {
                //System.out.println(a + "    " + b);
                //System.out.println(occurence);
                Row r = s.createRow(index);
                Cell d = r.createCell(0);
                d.setCellValue(a);
                Cell c = r.createCell(1);
                c.setCellValue(dateFormat(b, FORMATCHOISI));
                c = r.createCell(2);
                c.setCellValue(occurence);
                index++;
                a = rw.getCell(5).getStringCellValue();
                b = dateFormat(rw.getCell(0).getStringCellValue(), FORMATCHOISI);
                occurence = 1;
            }
            i++;
        }

        Row r = s.createRow(index);
        Cell d = r.createCell(0);
        d.setCellValue(a);
        Cell c = r.createCell(1);
        c.setCellValue(dateFormat(rw.getCell(0).getStringCellValue(), FORMATCHOISI));
        c = r.createCell(2);
        c.setCellValue(occurence);
        index++;
//            a=rw.getCell(5).getStringCellValue();

        FileOutputStream fileOut = new FileOutputStream(excelFileName);
        w.write(fileOut);
        fileOut.flush();
        fileOut.close();

    }

    public static TreeSet<String> getPropositions(String inputFile)throws Exception{
        TreeSet<String> propositions= new TreeSet<>();
        XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(inputFile)));
        Sheet sheet = wb.getSheetAt(0);
        int nbRows = sheet.getLastRowNum();
        int i = sheet.getFirstRowNum();
        while (i <= nbRows) {
            Row row = sheet.getRow(i);
            try{
                propositions.add(row.getCell(0).getStringCellValue());
            }catch(Exception e){}
            i++;
        }
        return propositions;
    }
}

