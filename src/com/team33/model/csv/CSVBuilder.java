package com.team33.model.csv;

import com.team33.model.ToCSV;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Amine on 13/02/2017.
 */

public class CSVBuilder  {
    private ArrayList<String> workbookPaths;
    private CSVFormat format;
    private String destination;
    private String tempPath;

    public CSVBuilder(CSVFormat format, String destination, String... workbookPaths) {
        this.workbookPaths.addAll(Arrays.asList(workbookPaths));
        this.format = format;
        this.destination = destination;
    }

    public void buildCSV(){
        tempPath = format.buildCSV((String[]) workbookPaths.toArray());
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
