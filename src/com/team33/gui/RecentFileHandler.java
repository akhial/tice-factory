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
            Scanner input = new Scanner(Paths.get("recent.txt"));
            StringBuilder old = new StringBuilder();
            while(input.hasNext()) {
                String s = input.nextLine();
                if(s.equals(filePath)) {
                    input.close();
                    return;
                }
                old.append(s)
                .append("\n");
            }
            input.close();
            Formatter formatter = new Formatter("recent.txt");
            formatter.format("%s%s", old.toString(), filePath);
            formatter.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> readFile() {
        ArrayList<String> recents = new ArrayList<>();
        try {
            Scanner input = new Scanner(Paths.get("recent.txt"));
            while(input.hasNext()) {
                String e = input.nextLine();
                if(!e.isEmpty())
                    recents.add(e);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return recents;
    }

    public static void clear() {
        try {
            Formatter formatter = new Formatter("recent.txt");
            formatter.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
