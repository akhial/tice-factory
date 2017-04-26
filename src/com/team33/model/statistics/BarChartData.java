package com.team33.model.statistics; /**
 * Created by dell on 24/04/2017.
 */

import org.apache.poi.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Created by dell on 24/04/2017.
 */
public class BarChartData {
    String nom;
    TreeSet<BaseData> datas = new TreeSet<BaseData>();

    public BarChartData(String nom) {
        this.nom = nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void readFromFile(String inputFile) throws Exception {
            datas=new TreeSet<>();
            DataFormatter formatter = new DataFormatter();
            XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(inputFile)));
            Sheet sheet = wb.getSheetAt(0);
            int nbRows = sheet.getLastRowNum();
            int i = sheet.getFirstRowNum();
            while (i <= nbRows) {
                Row row = sheet.getRow(i);
                if (row.getCell(0).getStringCellValue().toUpperCase().equals(nom.toUpperCase())) {
                    Cell cell= row.getCell(2);
                    BaseData baseData = new BaseData(row.getCell(1).getStringCellValue(),Integer.parseInt(formatter.formatCellValue(cell)));
                    addData(baseData);
                }
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