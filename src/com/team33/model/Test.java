package com.team33.model;

import com.team33.model.csv.CSVBuilder;
import com.team33.model.csv.TeacherFormat;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) {
        TeacherFormat teacherFormat = new TeacherFormat();
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("C:\\Users\\Amine\\IdeaProjects\\team-33\\CHARGES_enseignants_Février2017_2016-2017 (1).xlsx");
        arrayList.add("C:\\Users\\Amine\\IdeaProjects\\team-33\\Emails enseignants.xlsx");
        CSVBuilder csvBuilder = new CSVBuilder(arrayList,teacherFormat,".");
        try {
            csvBuilder.buildCSV();
            HashMap<String,ArrayList<String>> hashMap = teacherFormat.getUnHandledEmails();
            HashMap<String,String> hashMap1 = new HashMap<>();
            for (HashMap.Entry e:hashMap.entrySet()) {
                System.out.println("Plusieurs emails peuvent coresspondre a "+e.getKey());
                for (int i = 0; i < ((ArrayList)e.getValue()).size();i++){
                    System.out.println(i+" "+((ArrayList)e.getValue()).get(i));
                }
                hashMap1.put((String) e.getKey(), (String) ((ArrayList)e.getValue()).get(new Scanner(System.in).nextInt()));

            }
            teacherFormat.AddingMissingEmails(hashMap1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }
}
