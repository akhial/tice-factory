package com.team33.model.assertions;
import java.io.File;


/**
 * Created by Mitchell on 28/02/2017.
 */

public interface ExcelFormat {
    boolean checkFormat(File f1);
    boolean checkDoublants(File f1);

}
