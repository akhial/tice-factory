package com.team33.model.csv.Students.Courses;

import java.io.Serializable;

/**
 * Created by hamza on 18/04/2017.
 */
public class Course implements Serializable{
    private String shortName;
    private String fullName;

    public Course(String shortName, String fullName) {
        this.shortName = shortName;
        this.fullName = fullName;
    }

    public String getShortName() {

        return shortName;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public String toString() {
        return this.shortName +" : "+this.fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;

        Course course = (Course) o;

        if (shortName != null ? !shortName.equals(course.shortName) : course.shortName != null) return false;
        return fullName != null ? fullName.equals(course.fullName) : course.fullName == null;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
