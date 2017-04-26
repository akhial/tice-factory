package com.team33.model.statistics;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Created by dell on 25/04/2017.
 */
public class PieChartData {
    TreeSet<BaseData> datas = new TreeSet<BaseData>();

    public void readFromFile(String inputFile) throws Exception {
        datas=new TreeSet<>();
        DataFormatter formatter = new DataFormatter();
        XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(inputFile)));
        Sheet sheet = wb.getSheetAt(0);
        int nbRows = sheet.getLastRowNum();
        int i = sheet.getFirstRowNum();
        while (i <= nbRows) {
            Row row = sheet.getRow(i);
            Cell cell= row.getCell(1);
            BaseData baseData = new BaseData(row.getCell(0).getStringCellValue(),Integer.parseInt(formatter.formatCellValue(cell)));
            addData(baseData);
            //System.out.println(baseData.getDate()+"  "+baseData.getOccurrence()+"   "+i+ "  "+nbRows);////////////:
            i++;
        }
    }

    public void addData(BaseData data) {
        datas.add(data);
    }

    public void afficher() {// the test

        Iterator<BaseData> iter = datas.iterator();
        BaseData current;
        while (iter.hasNext()) {
            current = iter.next();
            System.out.println(current.getDate() + "," + current.getOccurrence());
        }
    }
}
