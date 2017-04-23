package com.team33.model.csv.Students;

import java.util.ArrayList;

public interface StudentInterface {
    ArrayList<Student> getListOfStudentsWithoutEmail();
    void updateRow(int position, Student student);
}
