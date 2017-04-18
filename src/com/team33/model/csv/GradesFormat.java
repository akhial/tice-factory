package com.team33.model.csv;


import com.team33.model.ToCSV;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Amine on 13/02/2017.
 */
public class GradesFormat implements CSVFormat {

    public void gradesToCsv(String source, String destination) {

        ToCSV t=new ToCSV();
        try {
            t.convertExcelToCSV(source,destination,",");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }


    }

    @Override
    public String buildCSV(ArrayList<String> workbooksPaths) {
        return null;
    }


   /* @Override
    public String buildCSV(ArrayList<String> workbooksPaths) {

    }*/
}
