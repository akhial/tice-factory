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
            Scanner input = new Scanner(Paths.get("recent.txt"));
            StringBuilder old = new StringBuilder();
            while(input.hasNext()) {
                old.append(input.nextLine());
            }
            // TODO check if works
            formatter.format("%s%s\n", old.toString(), filePath);
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
}
