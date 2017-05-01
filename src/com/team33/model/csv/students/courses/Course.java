package com.team33.model.csv.students.courses;

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

        if (this.shortName != null ? !this.shortName.equals(course.shortName) : course.shortName != null) return false;
        return this.fullName != null ? this.fullName.equals(course.fullName) : course.fullName == null;
    }

    @Override
    public int hashCode() {
        int result = this.shortName != null ? this.shortName.hashCode() : 0;
        result = 31 * result + (this.fullName != null ? this.fullName.hashCode() : 0);
        return result;
    }
}
