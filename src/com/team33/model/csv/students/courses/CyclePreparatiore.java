package com.team33.model.csv.students.courses;

import java.io.Serializable;

/**
 * Created by hamza on 19/04/2017.
 */
public class CyclePreparatiore implements Serializable {
    private SingleOptionCourses CPI1 = new SingleOptionCourses();
    private SingleOptionCourses CPI2 = new SingleOptionCourses();

    public SingleOptionCourses getCPI1() {
        return CPI1;
    }

    public SingleOptionCourses getCPI2() {
        return CPI2;
    }
}
