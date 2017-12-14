package org.siberteam.tumasov.test.workers;

import org.apache.log4j.Logger;
import org.siberteam.tumasov.test.interfaces.IDone;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable, IDone {
    private static final Logger LOG = Logger.getLogger(Producer.class);
    private final BlockingQueue<String> queueOfWords;
    private final String filePath;
    private BufferedReader bufferedReader;
    private FileReader fileReader;
    private boolean isDone = false;

    public Producer(BlockingQueue<String> q, String filePath) {
        this.queueOfWords = q;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        try {
            fileReader = new FileReader(filePath);
            bufferedReader = new BufferedReader(fileReader);
            StringTokenizer tokenizer;
            String line;
            while ((line = bufferedReader.readLine()) != null ) {
                tokenizer = new StringTokenizer(line);
                while (tokenizer.hasMoreTokens()) {
                    queueOfWords.put(tokenizer.nextToken());
                }
            }
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            try {
                bufferedReader.close();
                fileReader.close();
            } catch (Exception e) {
                LOG.error(e);
            }
            isDone = true;
        }
    }

    @Override
    public boolean getIsDone() {
        return isDone;
    }
}