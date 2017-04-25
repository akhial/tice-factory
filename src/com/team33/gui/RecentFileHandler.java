package com.team33.gui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;

public class RecentFileHandler {

    public static void writeFile(String filePath) {
        try {
            Formatter formatter = new Formatter("recent.txt");
            formatter.format("%s\n", filePath);
            formatter.close();
            System.out.println("Wrote file");
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> readFile() {
        ArrayList<String> recents = new ArrayList<>();
        try {
            Scanner input = new Scanner(Paths.get("recent.txt"));
            while(input.hasNext()) {
                recents.add(input.nextLine());
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return recents;
    }
}
