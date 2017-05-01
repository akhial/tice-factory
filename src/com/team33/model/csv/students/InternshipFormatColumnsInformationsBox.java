package com.team33.model.csv.students;

import com.team33.model.Utilities.Util;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.Iterator;

/**
 * Created by hamza on 23/04/2017.
 */
public class InternshipFormatColumnsInformationsBox {
    private int colCodePFE;
    private int colNom;
    private int colPrenom;
    private XSSFSheet sheet;

    public InternshipFormatColumnsInformationsBox(XSSFSheet sheet) {
        this.sheet = sheet;
    }

    public int getColCodePFE() {
        return this.colCodePFE;
    }

    public int getColNom() {
        return this.colNom;
    }

    public int getColPrenom() {
        return this.colPrenom;
    }

    public void extractInformationsFromFile()
    {
        boolean found = false;
        Iterator<Row> rowIterator = this.sheet.rowIterator();
        Row rw = rowIterator.next();
        while ((!found) && (rowIterator.hasNext())) {
            if (Util.getInstance().existInRow(rw, "Prenom")) {
                found = true;
                this.colNom = Util.getInstance().column(rw, "Nom");
                this.colPrenom = Util.getInstance().column(rw, "Prenom");
                this.colCodePFE = Util.getInstance().column(rw,"Code PFE");
            } else if (Util.getInstance().existInRow(rw, "Prénom")) {
                found = true;
                this.colNom = Util.getInstance().column(rw, "Nom");
                this.colPrenom = Util.getInstance().column(rw, "Prénom");
                this.colCodePFE = Util.getInstance().column(rw,"Code PFE");
            } else {
                rw = rowIterator.next();
            }
        }
    }
}
