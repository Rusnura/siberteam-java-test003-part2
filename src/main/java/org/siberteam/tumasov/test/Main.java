package org.siberteam.tumasov.test;

import org.apache.log4j.Logger;
import org.siberteam.tumasov.test.services.AnagramService;
import org.siberteam.tumasov.test.services.FileWriter;

import java.util.*;

public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        if (args.length == 0 || args.length > 2) {
            System.err.println("Usage: test.jar DictionaryFile AnagramOutputFileName");
            return;
        } else if (args.length == 1) {
            System.err.println("Please, define an output file!\nUsage: test.jar DictionaryFile AnagramOutputFileName");
            return;
        }

        try {
            AnagramService anagramService = new AnagramService(args[0]);
            Map<String, Set<String>> anagrams = anagramService.start();

            long start = System.currentTimeMillis();

            // Sorting anagrams
            List<Set<String>> sortedAnagrams = new ArrayList<>();
            for (Set<String> anagram: anagrams.values()) {
                if (anagram.size() > 1) {
                    sortedAnagrams.add(anagram);
                }
            }
            Collections.sort(sortedAnagrams, new Comparator<Set<String>>() {
                @Override
                public int compare(Set<String> o1, Set<String> o2) {
                    return Integer.compare(((String)o1.toArray()[0]).length(),
                                            ((String)o2.toArray()[0]).length());
                }
            });
            FileWriter.writeAnagramToFile(args[1], sortedAnagrams);

            long timeSpent = System.currentTimeMillis() - start;
            System.out.println("Программа выполнялась " + timeSpent + " миллисекунд");
        } catch (Exception e) {
            LOG.error(e);
        }
    }
}
