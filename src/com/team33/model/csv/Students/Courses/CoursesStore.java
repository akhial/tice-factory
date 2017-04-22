package com.team33.model.csv.Students.Courses;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by hamza on 18/04/2017.
 */
public class CoursesStore implements Serializable {

    private CyclePreparatiore cyclePreparatoire = new CyclePreparatiore();
    private CycleSuperieur cycleSuperieur = new CycleSuperieur();

    public CyclePreparatiore getCyclePreparatoire() {
        return cyclePreparatoire;
    }

    public CycleSuperieur getCycleSuperieur() {
        return cycleSuperieur;
    }

    public void saveChanges(){
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("courses")));
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (oos != null) try {
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void load(){
        try {
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream("courses")));
            CoursesStore coursesStore = (CoursesStore) ois.readObject();
            this.cyclePreparatoire = coursesStore.cyclePreparatoire;
            this.cycleSuperieur = coursesStore.cycleSuperieur;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getListOfCourses(String level,String optin){
        ArrayList<String> list = new ArrayList<>();
        switch (level){
            case  "1CPI" :
                for(Course course : cyclePreparatoire.getCPI1().getSemestre1()){
                    list.add(course.getShortName());
                }
                for (Course course : cyclePreparatoire.getCPI1().getSemestre2()){
                    list.add(course.getShortName());
            }
            return list;

            case "2CPI" :
                for(Course course : cyclePreparatoire.getCPI2().getSemestre1()){
                    list.add(course.getShortName());
                }
                for (Course course : cyclePreparatoire.getCPI2().getSemestre2()){
                    list.add(course.getShortName());
                }
                return list;
            case "1CS" :
                for(Course course : cycleSuperieur.getCS1().getSemestre1()){
                    list.add(course.getShortName());
                }
                for (Course course : cycleSuperieur.getCS1().getSemestre2()){
                    list.add(course.getShortName());
                }
                return list;
            case "2CS":
                for(Course course : cycleSuperieur.getCS2().getSemestre1(optin)){
                    list.add(course.getShortName());
                }
                for (Course course : cycleSuperieur.getCS2().getSemestre2(optin)){
                    list.add(course.getShortName());
                }
                for (Course course : cycleSuperieur.getCS2().getModulesCommuns()){
                    list.add(course.getShortName());
                }
                return list;
            case "3CS" :
                for(Course course : cycleSuperieur.getCS3courses()){
                    list.add(course.getShortName());
                }
                return list;
        }

        return list;
    }


    public int getNumberOfCourses(String level, String optin) {
        return getListOfCourses(level,optin).size();
    }
}
