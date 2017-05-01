package com.team33.model.csv.students;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public interface StudentInterface {
    ArrayList<Student> getListOfStudentsWithoutEmail();
    void updateRow(int position, Student student);
    void saveUsersList(File file) throws IOException;
}
