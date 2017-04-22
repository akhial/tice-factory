package com.team33.model.assertions;
import java.io.File;


/**
 * Created by Mitchell on 28/02/2017.
 */

/*  if (Util.getInstance().existInRow(row,"NOM") && Util.getInstance().existInRow(row,"PRENOMmm") &&
                        Util.getInstance().existInRow(row,"MAITRE") && Util.getInstance().existInRow(row,"COURS SEMESTRE1") &&
                        Util.getInstance().existInRow(row,"TD SEMESTRE1") && Util.getInstance().existInRow(row,"COURS SEMESTRE1") &&
                        Util.getInstance().existInRow(row,"TD SEMESTRE2") && Util.getInstance().existInRow(row,"PROJET SEMESTRE2")
                        )*/
public interface ExcelFormat {
    public boolean checkFormat(File f1);
    public boolean checkDoublants(File f1);

}
