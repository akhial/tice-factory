package com.team33.model;

import com.team33.model.csv.Student;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Mitchell on 28/02/2017.
 */
public class Util {
    public XSSFWorkbook workbookIn;
    public XSSFWorkbook workbookOut;
    public XSSFWorkbook workbookEmails;
    public ArrayList<String> filePathIn;
    public Row header = null;
    private static Util instance = new Util();

    private Util()
    {

    }

    public static Util getInstance() {
            return(instance);}

    /*
     *Méthodes utilitaires
     */

    public boolean existInRow(Row rw, String str)//verifier si'il y a une case dans la ligne rw dont sa valeur (pas just contenir) est la chaine str
    {
        boolean exist = false;
        for(Cell cell :rw )
        {
            if(str.equals(cell.toString())) exist = true;
        }
        return  exist;
    }


    public    int column(Row rw,String colName)// retourne l'indice de la colone de valeur colNom dans la ligne rw
    {
        boolean found = false;
        int colIndex = -1;

        for (Cell cell : rw) {
            if (colName.equals(cell.toString())) {
                colIndex = cell.getColumnIndex();
            }
        }
        return colIndex;
    }

    public void generateRow(int numRow, Student student, XSSFWorkbook workbook)// générer une ligne cde fichier résultat contenant les coordonné d'un étudiant
    {
        Row rw = workbook.getSheetAt(0).createRow(numRow);
        for (int j = 0; j < 4; j++) {
            rw.createCell(j);
        }
        rw.getCell(0).setCellValue(student.getUsername());
        rw.getCell(1).setCellValue(student.getFirstName());
        rw.getCell(2).setCellValue(student.getLastNameInMoodle());
        rw.getCell(3).setCellValue(student.getEmail());
    }


    public boolean rowContains(Row rw, String str)//verrifie si la ligne rw contient la chaine str
    {
        boolean contains = false;
        for(Cell cell : rw)
        {
            if(cell.toString().contains(str)) contains = true;
        }
        return  contains;
    }

    public int rangOfCellContaining(Row rw,String str) // retourne l'indice de la colonne contenat la chaine str
    {
        Iterator<Cell> cellIterator = rw.iterator();
        Cell cell = cellIterator.next();
        while ((cell != null))
        {
            if(cell.toString().contains(str)) {
                return cell.getColumnIndex();
            }
            else cell = cellIterator.next();
        }
        return -1;
    }


    public String ConvertWordTableToExcel(String wordPath){
        String type;
        try {
            FileInputStream fileInputStream = new FileInputStream(wordPath);
            XWPFDocument xwpfDocument = new XWPFDocument(fileInputStream);
            XWPFWordExtractor we = new XWPFWordExtractor(xwpfDocument);
            if(we.getText().contains("LISTE DES SUJETS DE PFE OPTION SIQ"))
            {
                type = "3CS-SIQ";
            }else if(we.getText().contains("LISTE DES SUJETS DE PFE OPTION SIT"))
            {
                type = "3CS-SIT";
            }else
            {
                type = "3CS-SIL";
            }
            String excelName = type + ".xlsx";
            FileOutputStream fileOutputStream = new FileOutputStream(excelName);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
            Sheet sheet = xssfWorkbook.createSheet();
            sheet.createRow(0).createCell(0).setCellValue(type);
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
                    excelRow.createCell(i).setCellValue(type);
                    nbColumns = i;
                    i = 0 ;
                    j++;
                    excelRow = sheet.createRow(j);
                }
                sheet.getRow(j-table.getNumberOfRows()).getCell(nbColumns).setCellValue("NG");
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

    public String getFileType(File file)
    {


        try  {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();
            Row rw = rowIterator.next();
            while (rowIterator.hasNext())
            {
                if(Util.getInstance().existInRow(rw,"NG"))
                {
                    return  "Solarite";
                }
                else
                {
                    rw = rowIterator.next();
                }
            }
            return "web";

        }catch (IOException e){
            e.printStackTrace();
        }
        return "";
    }

    public String getLevel(XSSFWorkbook workbook ){
        int i=0;
        String niveau ;
        if(Util.getInstance().existInRow(workbook.getSheetAt(0).getRow(0),"3CS-SIL")) return "3CS-SIL";
        if(Util.getInstance().existInRow(workbook.getSheetAt(0).getRow(0),"3CS-SIQ")) return "3CS-SIQ";
        if(Util.getInstance().existInRow(workbook.getSheetAt(0).getRow(0),"3CS-SIT")) return "3CS-SIT";
        for (Sheet sh: workbook) {
            for (Row rw:sh) {
                for (Cell cell:rw) {
                    niveau = cell.toString();
                    if(niveau.toUpperCase().contains("1CPI")){
                        return "1CPI";
                    }
                    if(niveau.toUpperCase().contains("2CPI")){
                        return  "2CPI";
                    }
                    if(niveau.toUpperCase().contains("SC")||niveau.toLowerCase().contains("1ère")){
                        i=1;
                    }
                    if(niveau.toLowerCase().contains("2ème")){
                        i=2;
                    }
                    if(niveau.toUpperCase().contains("CPI")&& i==1){
                        i=3;
                    }
                    if(niveau.toUpperCase().contains("CPI")&& i==2){
                        i=4;
                    }
                    if(niveau.toUpperCase().contains("SIL")){
                        return  "2CS-SIL";
                    }
                    if(niveau.toUpperCase().contains("SIQ")){
                        return  "2CS-SIQ";
                    }
                    if(niveau.toUpperCase().contains("SIT")){
                        return  "2CS-SIT";
                    }
                }
            }
        }
        if (i==1){
            return "1CS";
        }
        if (i==2){
            return "2CS";
        }
        if(i==3){
            return "1CPI";
        }
        if(i==4){
            return "2CPI";
        }
        return "";
    }


}
