package org.siberteam.tumasov.test.services;

import org.siberteam.tumasov.test.workers.Consumer;
import org.siberteam.tumasov.test.workers.Producer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

public class AnagramService {
    public static int availableProcessors = Runtime.getRuntime().availableProcessors();
    private final String filePath;
    private final BlockingQueue<String> queueOfWords = new ArrayBlockingQueue<String>(10);
    private final ConcurrentMap<String, Set<String>> anagrams = new ConcurrentHashMap<>();
    private final ExecutorService readerService = Executors.newSingleThreadExecutor();
    private final ExecutorService anagramBuilderService = Executors.newFixedThreadPool(availableProcessors);

    public AnagramService(String filename) {
        this.filePath = filename;
    }

    public Map<String, Set<String>> start() throws Exception {
        File file = new File(filePath);
        if (!file.canRead()) {
            throw new IOException("Can't opening file for reading!");
        }

        final Producer producer = new Producer(queueOfWords, this.filePath);
        readerService.submit(producer);

        final List<Future> workerList = new ArrayList<Future>();
        for (int i = 0; i < availableProcessors; i++) {
            final Consumer consumer = new Consumer(this.queueOfWords, this.anagrams, producer);
            Future worker = anagramBuilderService.submit(consumer);
            workerList.add(worker);
        }

        // Wait a end of work
        for (Future worker: workerList) {
            worker.get(); // block current thread, while task is running
        }

        // Shutdown all thread's
        readerService.shutdown();
        anagramBuilderService.shutdown();

        return anagrams;
    }
}