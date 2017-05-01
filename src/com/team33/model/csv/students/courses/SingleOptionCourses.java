package com.team33.model.csv.students.courses;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Created by hamza on 19/04/2017.
 */
public class SingleOptionCourses implements Serializable{
    private HashSet<Course> semestre1;
    private HashSet<Course> semestre2;

    public SingleOptionCourses(){
        this.semestre1 = new HashSet<>();
        this.semestre2 = new HashSet<>();
    }

    public HashSet<Course> getSemestre1() {
        return semestre1;
    }

    public HashSet<Course> getSemestre2() {
        return semestre2;
    }

    public void ajouterCoursSemsetre1(String shortName, String fullName){
        Course course = new Course(shortName,fullName);
        this.semestre1.add(course);
    }

    public void ajouterCoursSemstre2(String shortName,String fullName){
        Course course = new Course(shortName,fullName);
        this.semestre2.add(course);
    }

    public void supprimerCoursSemestre1(String shortName,String fullName)
    {
        this.semestre1.remove(new Course(shortName,fullName));
    }

    public void supprimerCoursSemestre2(String shortName,String fullName)
    {
        this.semestre2.remove(new Course(shortName,fullName));
    }


}
