package org.siberteam.tumasov.test.workers;

import org.apache.log4j.Logger;
import org.siberteam.tumasov.test.interfaces.IDone;
import org.siberteam.tumasov.test.services.StringUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class Consumer implements Runnable {
    private static final Logger LOG = Logger.getLogger(Consumer.class);
    private final BlockingQueue<String> queueOfWords;
    private final ConcurrentMap<String, Set<String>> anagramsMap;
    private final IDone indicator;

    public Consumer(BlockingQueue<String> q, ConcurrentMap<String, Set<String>> anagrams, IDone indicator) {
        this.queueOfWords = q;
        this.anagramsMap = anagrams;
        this.indicator = indicator;
    }

    @Override
    public void run() {
        String word;
        try {
            while (!this.indicator.getIsDone()) {
                if ((word = queueOfWords.poll(10, TimeUnit.MILLISECONDS)) != null) {
                    String sortedWord = StringUtil.sortWordByAlphabet(word);
                    Set<String> anagram = anagramsMap.get(sortedWord);
                    if (anagram == null) {
                        Set<String> anagrams = new HashSet<>();
                        anagrams.add(word);
                        Set<String> existingAnagram = anagramsMap.putIfAbsent(sortedWord, anagrams);
                        if (existingAnagram != null) {
                            existingAnagram.add(word);
                        }
                    } else {
                        anagram.add(word);
                    }
                }
            }
        } catch (InterruptedException e) {
            LOG.error(e);
        }
    }
}
