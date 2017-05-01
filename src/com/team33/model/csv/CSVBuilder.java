package com.team33.model.csv;


import com.team33.model.Utilities.ToCSV;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Amine on 13/02/2017.
 */
public class CSVBuilder  {
    private ArrayList<String> workbooksPaths ;
    private CSVFormat format;
    private String destination;
    private String tempPath;

    public CSVBuilder(ArrayList<String> workbooksPaths, CSVFormat format, String destination) {
        this.workbooksPaths = workbooksPaths;
        this.format = format;
        this.destination = destination;
    }

    public CSVBuilder(ArrayList<String> workbooksPaths, CSVFormat format) {
        this.workbooksPaths = workbooksPaths;
        this.format = format;
    }

    public CSVBuilder(CSVFormat format) {
        
        this.format = format;
    }

    public String getTempPath() {
        return tempPath;
    }

    public void buildCSV() throws IOException, InvalidFormatException {
        tempPath = format.buildCSV(workbooksPaths);
    }

    public void convertToCSV(){
        ToCSV toCSV = new ToCSV();
        try {
            toCSV.convertExcelToCSV(tempPath,destination);
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }
}
