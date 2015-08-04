package com.bt;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.security.KeyStore;
import java.util.List;

/**
 * Created by burhan on 01.08.2015.
 */
public class PasswordChecker extends Thread {
    private ByteArrayInputStream bais;
    private KeyStore ks;

    private List<Word> words;
    private int id;

    PasswordChecker(List<Word> words, String keystorePath, int id) {
        this.words = words;
        this.id = id;

        try {
            init(keystorePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        KeyStore ks = null;

        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!Main.found) {
            for (Word word : words) {
                try {
                    ks.load(bais, word.getWord().toCharArray());
                    Main.found = true;
                    persist(word.getWord());
                    System.out.println("found password : " + word.getWord());
                    break;
                } catch (Exception e) {
                    bais.reset();
                }
            }

            Main.increment(words.size(), words.get(words.size()-1).getWord());
            Main.countDown(id);
            words = null;
        }
    }

    private void persist(String word) throws Exception {
        File f = new File("result.txt");
        if(f.exists()) {
            f.delete();
        }

        PrintStream ps = new PrintStream(f);
        ps.println(word);
        ps.close();
    }

    private void init(String keystorePath) throws Exception {
        File ks = new File(keystorePath);
        FileInputStream fis = new FileInputStream(ks);
        byte[] ba = new byte[(int) ks.length()];
        fis.read(ba);
        bais = new ByteArrayInputStream(ba);
        fis.close();
    }
}
