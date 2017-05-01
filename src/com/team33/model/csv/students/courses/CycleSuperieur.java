package com.team33.model.csv.students.courses;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Created by hamza on 19/04/2017.
 */
public class CycleSuperieur implements Serializable{
    private SingleOptionCourses CS1 = new SingleOptionCourses();
    private MultipleOptionsCourses CS2 = new MultipleOptionsCourses();

    private HashSet<Course> CS3courses = new HashSet<>();

    public SingleOptionCourses getCS1() {
        return CS1;
    }

    public MultipleOptionsCourses getCS2() {
        return CS2;
    }

    public HashSet<Course> getCS3courses() {
        return CS3courses;
    }

    public void ajouterCours3CS(String shortName, String fullName){
        Course course = new Course(shortName,fullName);
        if(this.CS3courses == null) this.CS3courses = new HashSet();
        this.CS3courses.add(course);
    }

    public void suprimerCours3CS(Course course){
        this.CS3courses.remove(course);
    }


}
