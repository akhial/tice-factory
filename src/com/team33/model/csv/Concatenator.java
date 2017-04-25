package com.team33.model.csv;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Amine on 17/04/2017.
 */
public class Concatenator {

    public void Concatenate(String destination,String...workbooks) throws IOException, InvalidFormatException {
        ArrayList<XSSFWorkbook> xssfWorkbooks = new ArrayList<>();
        Workbook workbookOut = new XSSFWorkbook();
        for (String s:workbooks){
            xssfWorkbooks.add((XSSFWorkbook) WorkbookFactory.create(new File(s)));
        }
        Row row,row1;
        int i = 1 ;
        ArrayList<String> strings = new ArrayList<>();
        Sheet sheet = workbookOut.createSheet();
        for (Workbook w:xssfWorkbooks) {
            Iterator<Row> iterator = w.getSheetAt(0).rowIterator();
            row = iterator.next();
            int maxCol = 0 ;
            while (iterator.hasNext()){
                row = iterator.next();
                row1 = sheet.createRow(i);
                int j = 0;
                for (Cell c:row) {
                    row1.createCell(j).setCellValue(c.toString());
                    j++;
                }
                if (j > maxCol){
                    maxCol = j;
                    row = w.getSheetAt(0).getRow(0);
                    strings.clear();
                    for (Cell c:row) {
                        strings.add(c.toString());
                    }
                }
                i++;

            }

        }
        int j = 0;
        row1 = sheet.createRow(0);
        for (String s:strings){
            row1.createCell(j).setCellValue(s);
            j++;
        }

        FileOutputStream fos = new FileOutputStream(new File(destination));
        workbookOut.write(fos);
        fos.close();
        for (XSSFWorkbook xs:xssfWorkbooks) {
            xs.close();

        }

    }
}
