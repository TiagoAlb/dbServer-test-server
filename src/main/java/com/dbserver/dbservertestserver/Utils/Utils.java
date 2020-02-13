/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbserver.dbservertestserver.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 *
 * @author Tiago
 */
public class Utils {

    public String analyzeFile(String fileDirectory, String fileName) {
        StringBuilder sb = new StringBuilder();

        createFile(fileDirectory, fileName);

        fileDirectory = fileDirectory + fileName;

        try (Stream<String> lines = Files.lines(Paths.get(fileDirectory))) {
            lines.forEach((line) -> {
                sb.append(line.trim());
            });
            lines.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return sb.toString();
        }
    }

    public void createFile(String fileDirectory, String fileName) {
        try {
            File file = new File(fileDirectory);
            String[] files = file.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return file.isDirectory() && name.equals(fileName);
                }
            });

            if (!file.isDirectory()) {
                file.mkdirs();
            }

            if (files.length == 0) {
                if (new File(fileDirectory + fileName).createNewFile()) {
                    FileWriter writer = new FileWriter(fileDirectory + fileName);
                    writer.write("[]");
                    writer.close();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean alterFile(String jsonArray, String file) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(jsonArray);
            writer.close();

            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
