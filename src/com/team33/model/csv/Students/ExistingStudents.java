package com.team33.model.csv.Students;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hamza on 15/04/2017.
 */
public class ExistingStudents {
    private HashMap<String,Student> students;
    private String level;
    private String optin;

    public ExistingStudents(String level, String optin) {
        this.level = level;
        this.optin = optin;
    }

    public HashMap<String, Student> getStudents() {
        return students;
    }

    void loadExistingStudents() throws IOException {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(this.level+this.optin+".data")));
            this.students = (HashMap<String, Student>) ois.readObject();
            for (Map.Entry<String,Student> entry : students.entrySet())
            {
                Student student = entry.getValue(); System.out.println(student.getLastNameInMoodle());}
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        } finally {
            if(ois != null) ois.close();
        }
    }
}
