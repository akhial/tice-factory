package com.team33.model.csv.Students.Courses;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hamza on 19/04/2017.
 */
public class CycleSuperieur implements Serializable{
    private SingleOptionCourses CS1 = new SingleOptionCourses();
    private MultipleOptionsCourses CS2 = new MultipleOptionsCourses();

    private ArrayList<Course> CS3courses;

    public SingleOptionCourses getCS1() {
        return CS1;
    }

    public MultipleOptionsCourses getCS2() {
        return CS2;
    }

    public ArrayList<Course> getCS3courses() {
        return CS3courses;
    }

    public void ajouterCours3CS(String shortName, String fullName){
        Course course = new Course(shortName,fullName);
        if(this.CS3courses == null) this.CS3courses = new ArrayList<>();
        this.CS3courses.add(course);
    }

    public void suprimerCours3CS(String shortName,String fullName){
        this.CS3courses.remove(new Course(shortName,fullName));
    }


}
