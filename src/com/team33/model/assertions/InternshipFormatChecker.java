package com.team33.model.assertions;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Iterator;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

/**
 * Created by Amine on 14/02/2017.
 */
public class InternshipFormatChecker implements ExcelFormat {
    public String ConvertWordTableToExcel(String wordPath){
        String type="Interniship";
        try {
            FileInputStream fileInputStream = new FileInputStream(wordPath);
            XWPFDocument xwpfDocument = new XWPFDocument(fileInputStream);
            XWPFWordExtractor we = new XWPFWordExtractor(xwpfDocument);

            String excelName = type + ".xlsx";
            FileOutputStream fileOutputStream = new FileOutputStream(excelName);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
            Sheet sheet = xssfWorkbook.createSheet();

            int i = 0 ;
            int j = 1 ;
            int nbColumns = 0;
            Row excelRow = sheet.createRow(j);
            for (XWPFTable table:xwpfDocument.getTables()) {
                for (XWPFTableRow row:table.getRows()) {

                    for (XWPFTableCell cell:row.getTableCells()) {
                        excelRow.createCell(i).setCellValue(cell.getText());
                        i++;
                    }

                    nbColumns = i;
                    i = 0 ;
                    j++;
                    excelRow = sheet.createRow(j);
                }

            }
            xssfWorkbook.write(fileOutputStream);
            fileInputStream.close();
            return excelName;
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";

    }
    @Override



     public boolean checkFormat(File f1){
        String s=f1.getName();
        InternshipFormatChecker wrd=new InternshipFormatChecker();
        String s2=wrd.ConvertWordTableToExcel(s);
        File f2 = new File(s2);



        try {

            XSSFWorkbook wB = new XSSFWorkbook(new FileInputStream(f2));
            XSSFSheet sheet = wB.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            Row row;
            Cell cell;


            row = rowIterator.next();

            while (rowIterator.hasNext()) {
                if (Util.getInstance().existInRow(row, "Nom") || Util.getInstance().existInRow(row, "Prénom") ||
                        Util.getInstance().existInRow(row, "Code PFE") || Util.getInstance().existInRow(row, "Thème du sujet") ||
                        Util.getInstance().existInRow(row, "Promoteur") || Util.getInstance().existInRow(row, "Encadreur") ||
                        Util.getInstance().existInRow(row, "Décision validation") ) {

                    if (!Util.getInstance().existInRow(row, "Nom") || !Util.getInstance().existInRow(row, "Prénom") ||
                            !Util.getInstance().existInRow(row, "Code PFE") || !Util.getInstance().existInRow(row, "Thème du sujet") ||
                            !Util.getInstance().existInRow(row, "Promoteur") || !Util.getInstance().existInRow(row, "Encadreur") ||
                            !Util.getInstance().existInRow(row, "Décision validation") ) {
                        System.out.println("Missing one or more fields in the File, failed !");
                        if (!Util.getInstance().existInRow(row, "Nom"))
                            System.out.println("- Nom");
                        if (!Util.getInstance().existInRow(row, "Prénom")) System.out.println("- Prénom");
                        if (!Util.getInstance().existInRow(row, "Code PFE")) System.out.println("- Code PFE");
                        if (!Util.getInstance().existInRow(row, "Thème du sujet"))
                            System.out.println("- Thème du sujet");
                        if (!Util.getInstance().existInRow(row, "Promoteur"))
                            System.out.println("- Promoteur");
                        if (!Util.getInstance().existInRow(row, "Encadreur")) System.out.println("- Encadreur");
                        if (!Util.getInstance().existInRow(row, "Décision validation")) System.out.println("- Décision validation");
                        f2.delete();
                        return (false);

                    } else { System.out.println("File match !");
                        f2.delete();
                        return (true);
                    }


                } else row = rowIterator.next();


            }


        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
        System.out.println("Couldn't find target line !");
        return(false);
    }

    @Override
    public boolean checkDoublants(File f1) {
        return false;
    }

    ;

    public static void main(String[] args) {
        File f1 = new File("LISTEDESSUJETSPFE20162017SIL.docx");
                InternshipFormatChecker ssf = new InternshipFormatChecker();
       String y=ssf.ConvertWordTableToExcel("LISTEDESSUJETSPFE20162017SIL.docx");
       System.out.println(y);
                 boolean b=ssf.checkFormat(f1);

    }
}
