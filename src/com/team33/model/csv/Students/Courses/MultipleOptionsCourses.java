package com.team33.model.csv.Students.Courses;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hamza on 19/04/2017.
 */
public class MultipleOptionsCourses implements Serializable{
    private HashMap<String,ArrayList<Course>> semestre1;
    private HashMap<String,ArrayList<Course>> semestre2;
    private ArrayList<String> options;
    private ArrayList<Course> modulesCommuns;


    public void ajouterCoursSemestre1(String option,String shortName,String fullName) throws UnExistingOptionException {
        if(this.semestre1 == null) this.semestre1 = new HashMap<>();
        if(options.contains(option))
        {
        Course course = new Course(shortName,fullName);
        if(!this.semestre1.containsKey(option)) this.semestre1.put(option,new ArrayList<>());
        this.semestre1.get(option).add(course);
        }else {
            throw new UnExistingOptionException("Cette option n'existe pas");
        }
    }

    public void ajouterCoursSemestre2(String option,String shortName,String fullName) throws UnExistingOptionException{
        if(this.semestre2 == null) this.semestre2 = new HashMap<>();
        if(options.contains(option))
        {
            Course course = new Course(shortName,fullName);
            if(!this.semestre2.containsKey(option)) this.semestre2.put(option,new ArrayList<>());
            this.semestre2.get(option).add(course);
        }else {
            throw new UnExistingOptionException("Cette option n'existe pas");
        }
    }

    public void supprimerCoursSemestre1(String option,String shortName,String fullName){
        this.semestre1.get(option).remove(new Course(shortName,fullName));
    }

    public void supprimerCoursSemestre2(String option,String shortName,String fullName){
        this.semestre2.get(option).remove(new Course(shortName,fullName));
    }

    public void ajouterModuleCommun(String shortName, String fullName){
        Course course = new Course(shortName,fullName);
        if(this.modulesCommuns == null) this.modulesCommuns = new ArrayList<>();
        this.modulesCommuns.add(course);
    }

    public void suprimerModulesCommun(String shortName,String fullName){
        this.modulesCommuns.remove(new Course(shortName,fullName));
    }

    public ArrayList<Course> getModulesCommuns() {
        return modulesCommuns;
    }

    public ArrayList<Course> getSemestre1(String option){
        if(semestre1.containsKey(option)) return semestre1.get(option);
        else return new ArrayList<>();
    }

    public ArrayList<Course> getSemestre2(String option){
        if(semestre2.containsKey(option)) return semestre2.get(option);
        else return new ArrayList<>();
    }


    public void ajouterOption(String option){
        if(this.options == null) this.options = new ArrayList<>();
        this.options.add(option);
    }



}
