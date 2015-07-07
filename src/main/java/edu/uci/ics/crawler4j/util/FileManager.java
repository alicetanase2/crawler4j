package edu.uci.ics.crawler4j.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    public FileManager() {
    }

    public void clearDirectory(String directoryName) {
        File folder = new File(directoryName);
        clearDirectory(folder);
    }

    public void clearDirectory(File folder) {

        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                clearDirectory(file);
            }
            file.delete();
        }
    }

    public List<String> extractURLs(String filePath) throws IOException {
        List<String> seedURLs = new ArrayList<String>();
        String line;

        BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
        line = br.readLine();

        while (line != null) {
            seedURLs.add(line);
            line = br.readLine();
        }

        return seedURLs;
    }

    public static void mergeFiles(File[] files, File mergedFile) throws IOException {

        FileWriter fstream = null;
        BufferedWriter out = null;
        try {
            fstream = new FileWriter(mergedFile, true);
            out = new BufferedWriter(fstream);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        for (File f : files) {
            FileInputStream fis;
            try {
                fis = new FileInputStream(f);
                BufferedReader in = new BufferedReader(new InputStreamReader(fis));

                String aLine;
                while ((aLine = in.readLine()) != null) {
                    out.write(aLine);
                    out.newLine();
                }

                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        out.close();
    }

    public static File[] getAllLogFiles(String filePath) {
        File folder = new File(filePath);
        File[] files = folder.listFiles();

        return files;
    }
}
