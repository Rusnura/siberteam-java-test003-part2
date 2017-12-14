package org.siberteam.tumasov.test.services;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

public class FileWriter {
    private static final Logger LOG = Logger.getLogger(FileWriter.class);

    public static void writeAnagramToFile(String fileName, List<Set<String>> anagrams) {
        try (PrintWriter writer = new PrintWriter(new java.io.FileWriter(fileName, true))) {
            for (Set<String> anagram: anagrams) {
                for (String w: anagram) {
                    writer.print(w + " ");
                }
                writer.println();
            }
        } catch (IOException e) {
            LOG.error(e);
        }
    }
}
