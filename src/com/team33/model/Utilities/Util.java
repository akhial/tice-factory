package com.team33.model.Utilities;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.Iterator;

/**
 * Created by Mitchell on 28/02/2017.
 */
public class Util {
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


    public int column(Row rw,String colName)// retourne l'indice de la colone de valeur colNom dans la ligne rw
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


    public boolean rowContains(Row rw, String str)//verrifie si la ligne rw contient la chaine str
    {
        boolean contains = false;
        for(Cell cell : rw)
        {
            if(cell.toString().contains(str)) contains = true;
        }
        return  contains;
    }

    public boolean rowContainsIgnoreCase(Row rw, String str)
    {
        str = str.toLowerCase();
        String row = null;
        for(Cell cell : rw)
        {
            row = row + cell.toString();

        }
        if(row != null) {
            row = row.toLowerCase();
            return row.contains(str);
        } else return false  ;


    }

    public int rangOfCellContaining(Row rw,String str) // retourne l'indice de la colonne contenat la chaine str
    {
        Iterator<Cell> cellIterator = rw.iterator();
        Cell cell = cellIterator.next();
        while ((cell != null))
        {
            if(cell.toString().contains(str)) {
                return cell.getColumnIndex();
            }
            else cell = cellIterator.next();
        }
        return -1;
    }




}
