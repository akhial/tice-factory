package com.team33.model.csv;

import com.team33.model.csv.Students.Student;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Amine on 13/02/2017.
 */
public interface CSVFormat {
    String buildCSV(ArrayList<String> workbooksPaths) throws IOException;
}
