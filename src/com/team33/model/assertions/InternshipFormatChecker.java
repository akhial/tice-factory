package com.team33.model.assertions;

import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

/**
 * Created by Amine on 14/02/2017.
 */
public class InternshipFormatChecker implements ExcelFormat {

    public String ConvertWordTableToExcel(String wordPath) throws IOException{
        String type="Interniship";

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


    }
    @Override



     public boolean checkFormat(String f1) throws IOException, MissingFieldsException, NoLineFoundException {
        String s=f1;
        InternshipFormatChecker wrd=new InternshipFormatChecker();
        String s2=wrd.ConvertWordTableToExcel(s);
        File f2 = new File(s2);
            XSSFWorkbook wB = new XSSFWorkbook(new FileInputStream(f2));
        if (wB.getSheetAt(0).getRow(wB.getSheetAt(0).getLastRowNum())==null)
            throw new NoSuchElementException("Fichier Vide !");
            XSSFSheet sheet = wB.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            Row row;
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
                        String str1="Champ(s) Introuvable(s) : ";
                        if (!Util.getInstance().existInRow(row, "Nom"))
                            str1=str1+ "\n- Nom";
                        if (!Util.getInstance().existInRow(row, "Prénom")) str1=str1+ "\n- Prénom";
                        if (!Util.getInstance().existInRow(row, "Code PFE")) str1=str1+ "\n- Code PFE";
                        if (!Util.getInstance().existInRow(row, "Thème du sujet"))
                            str1=str1+ "\n- Thème du sujet";
                        if (!Util.getInstance().existInRow(row, "Promoteur"))
                            str1=str1+ "\n- Promoteur";
                        if (!Util.getInstance().existInRow(row, "Encadreur")) str1=str1+ "\n- Encadreur";
                        if (!Util.getInstance().existInRow(row, "Décision validation")) str1=str1+ "\n- Décision validation";
                        f2.delete();
                        throw new MissingFieldsException(str1);

                    } else {                        f2.delete();
                        return true;
                    }


                } else row = rowIterator.next();

            }

        throw new NoLineFoundException("Header de la table non-trouvé !");
    }

    @Override
    public boolean checkDoublants(String f1, int[] tab) throws FastInfosetException, IOException, Exception {
        return false;
    }

    @Override
    public void DeleteDuplicate(String fileName, int loc1, int loc2) throws IOException {

    }

    public static void main(String[] args) throws IOException, MissingFieldsException, NoLineFoundException {
                InternshipFormatChecker ssf = new InternshipFormatChecker();
       String y=ssf.ConvertWordTableToExcel("LISTEDESSUJETSPFE20162017SIL.docx");
       System.out.println(y);
                 boolean b=ssf.checkFormat("LISTEDESSUJETSPFE20162017SIL.docx");

    }
}
