package com.team33.model.statistics;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Created by dell on 24/04/2017.
 */
public class MainStats {
    public static void main(String[] args) {
        program2();
    }

    static void program1(){
        /*try {
            BarChartData chart = new BarChartData("Category Created");
            chart.readFromFile("C:\\Users\\dell\\Desktop\\module projet\\test\\resultSerious.xlsx");
            chart.afficher();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        try {
            TreeSet<String> propositions;
            StatisticsGenerator.trierFichierExcel("C:\\Users\\dell\\Downloads\\logs_20170324-2244.xlsx", "temp1.xlsx", 5);
            StatisticsGenerator.selectDates("1/1/2016","12/5/2017","temp1.xlsx","temp1.xlsx");
            StatisticsGenerator.semiGeneralStats("temp1.xlsx","temp1.xlsx",1);
            propositions=StatisticsGenerator.getPropositions("temp1.xlsx");
            BarChartData chart = new BarChartData("");
            Iterator<String> iter = propositions.iterator();
            String current;
            while (iter.hasNext()) {
                current = iter.next();
                chart.setNom(current);
                System.out.println(current);
                chart.readFromFile("temp1.xlsx");
                chart.afficher();
                System.out.println();
            }

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    static void program2(){
        try {
            StatisticsGenerator.trierFichierExcel("C:\\Users\\dell\\Downloads\\logs_20170324-2244.xlsx", "temp1.xlsx", 3);
            StatisticsGenerator.selectDates("1/1/2016","12/5/2017","temp1.xlsx","temp1.xlsx");
            StatisticsGenerator.coursStats("temp1.xlsx","temp2.xlsx",1);
            PieChartData pie=new PieChartData();
            pie.readFromFile("temp2.xlsx");
            pie.afficher();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }//*/
    }

}