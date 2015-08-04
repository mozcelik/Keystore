package com.bt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by burhan on 01.08.2015.
 */
public class PasswordGenerator {
    private boolean canRepeat = false;
    private StringBuffer buffer = new StringBuffer();
    private static String[] words;

    public PasswordGenerator() {
        try {
            initWords();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initWords() throws Exception {
        System.out.println("reading words.txt");

        List<String> ws = new ArrayList<String>();
        String line = null;
        int count = 0;

        File f = new File("words.txt");
        FileInputStream fis = new FileInputStream(f);
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader reader = new BufferedReader(isr);

        while((line = reader.readLine()) != null) {
            ws.add(line.trim());
            count++;
        }

        reader.close();
        isr.close();
        fis.close();

        words = ws.toArray(new String[0]);
        System.out.println("counted " + count + " words.");
    }

    public synchronized List<Word> nextWords(int[] order, int n) {
        List<Word> list = new ArrayList<Word>();
        int[] ord = Arrays.copyOf(order, order.length);

        for(int i = 0; i < n; i++) {
            ord = nextOrder(ord);
            String nextWord = word(ord);

            Word word = new Word();
            word.setOrder(ord);
            word.setWord(nextWord);
            list.add(word);
        }

        return list;
    }

    private synchronized String word(int[] order) {
        buffer.setLength(0);

        for(int index = 0; index < order.length; index++) {
            buffer.append(words[order[index]]);
        }

        return buffer.toString();
    }

    private synchronized int[] nextOrder(int[] order) {
        for (int i = order.length - 1; i >= 0; i--) {
            if (order[i] == words.length - 1) {
                order[i] = 0;
                continue;
            } else {
                order[i] += 1;
                return order;
            }
        }

        int[] ordp = new int[order.length + 1];
        ordp[0] = 0;
        System.arraycopy(order, 0, ordp, 1, order.length);
        return ordp;
    }
}
