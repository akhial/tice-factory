package com.team33.model.assertions;
import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException;

import java.io.IOException;


/**
 * Created by Mitchell on 28/02/2017.
 */

/*  if (Util.getInstance().existInRow(row,"NOM") && Util.getInstance().existInRow(row,"PRENOMmm") &&
                        Util.getInstance().existInRow(row,"MAITRE") && Util.getInstance().existInRow(row,"COURS SEMESTRE1") &&
                        Util.getInstance().existInRow(row,"TD SEMESTRE1") && Util.getInstance().existInRow(row,"COURS SEMESTRE1") &&
                        Util.getInstance().existInRow(row,"TD SEMESTRE2") && Util.getInstance().existInRow(row,"PROJET SEMESTRE2")
                        )*/
public interface ExcelFormat {
    public boolean checkFormat(String f1) throws IOException, MissingFieldsException, NoLineFoundException;
    public boolean checkDoublants(String f1, int tab[]) throws FastInfosetException,IOException,Exception;
    void DeleteDuplicate(String fileName, int loc1, int loc2) throws IOException;

}
