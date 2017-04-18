package com.team33.model;

import com.team33.model.csv.GradesFormat;

public class Test {

    public static void main(String[] args) {
        GradesFormat g=new GradesFormat();
        g.gradesToCsv("8910.xlsx","./");

    }
}
