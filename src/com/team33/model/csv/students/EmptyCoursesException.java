package com.team33.model.csv.students;

/**
 * Created by hamza on 01/05/2017.
 */
public class EmptyCoursesException extends Exception {

    @Override
    public String getMessage() {
        return "La liste des cours est vide !\n"+
                "Veuillez la replir SVP";
    }
}
