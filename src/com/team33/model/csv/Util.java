package com.team33.model.csv;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Mitchell on 28/02/2017.
 */
public class Util {
    public XSSFWorkbook workbookIn;
    public XSSFWorkbook workbookOut;
    public XSSFWorkbook workbookEmails;
    public ArrayList<String> filePathIn;
    public Row header = null;
    private static Util instance = new Util();

    private Util()
    {

    }

    public static Util getInstance() {
            return(instance);}

    /*
     *Méthodes utilitaires
     */

    public boolean existInRow(Row rw, String str)//verifier si'il y a une case dans la ligne rw dont sa valeur (pas just contenir) est la chaine str
    {
        boolean exist = false;
        for(Cell cell :rw )
        {
            if(str.equals(cell.toString())) exist = true;
        }
        return  exist;
    }


    public    int column(Row rw,String colName)// retourne l'indice de la colone de valeur colNom dans la ligne rw
    {
        boolean found = false;
        int colIndex = -1;

        for (Cell cell : rw) {
            if (colName.equals(cell.toString())) {
                colIndex = cell.getColumnIndex();
            }
        }
        return colIndex;
    }

    public void generateRow(int numRow, Student student,XSSFWorkbook workbook)// générer une ligne cde fichier résultat contenant les coordonné d'un étudiant
    {
        Row rw = workbook.getSheetAt(0).createRow(numRow);
        for (int j = 0; j < 4; j++) {
            rw.createCell(j);
        }
        rw.getCell(0).setCellValue(student.getUsername());
        rw.getCell(1).setCellValue(student.getFirstName());
        rw.getCell(2).setCellValue(student.getLastNameInMoodle());
        rw.getCell(3).setCellValue(student.getEmail());
    }


    public boolean rowContains(Row rw, String str)//verrifie si la ligne rw contient la chaine str
    {
        boolean contains = false;
        for(Cell cell : rw)
        {
            if(cell.toString().contains(str)) contains = true;
        }
        return  contains;
    }

    public int rangOfCellContaining(Row rw,String str) // retourne l'indice de la colonne contenat la chaine str
    {
        int rang = -1;
        boolean contains = false;
        Iterator<Cell> cellIterator = rw.iterator();
        Cell cell = cellIterator.next();
        while ((cell != null) && (contains == false))
        {
            if(cell.toString().contains(str)) {
                contains = true;
                rang = cell.getColumnIndex();
            }
            else cell = cellIterator.next();
        }
        return  rang;
    }





}
