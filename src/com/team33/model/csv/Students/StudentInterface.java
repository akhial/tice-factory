package com.team33.model.csv.Students;

import java.io.File;
import java.util.ArrayList;

public interface StudentInterface {
    ArrayList<Student> getListOfStudentsWithoutEmail();
    void updateRow(int position, Student student);
    void saveUsersList(File file);
}
