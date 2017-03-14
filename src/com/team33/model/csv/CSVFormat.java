package com.team33.model.csv;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Amine on 13/02/2017.
 */
public interface CSVFormat {
     String buildCSV(String... workbooksPaths) throws IOException;
}
