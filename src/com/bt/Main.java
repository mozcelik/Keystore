package com.bt;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static boolean found = false;

    private static long size = 0;
    private static String password;
    private static CountDownLatch latch;

    private static String[] sws = {"-keystore"};

    public static void main(String[] args) {
        String keystorePath = null;

        for (int i = 0; i < args.length; i += 2) {
            String arg = args[i];

            switch (arg) {
                case "-keystore":
                    keystorePath = args[i + 1];
                    break;
            }
        }

        System.out.println("keystore file : " + keystorePath);

        try {
            start(keystorePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void start(String keystorePath) throws Exception {
        int nthreads = Runtime.getRuntime().availableProcessors() * 2;
        PasswordGenerator pg = new PasswordGenerator();
        int[] order = new int[0];
        int count = 0;

        startObserverThread();

        while (!found) {
            latch = new CountDownLatch(nthreads);
            for (int i = 0; i < nthreads; i++) {
                List<Word> words = pg.nextWords(order, 1000);
                order = words.get(words.size() - 1).getOrder();

                PasswordChecker checker = new PasswordChecker(words, keystorePath, i);
                checker.start();
            }

            latch.await();
        }
    }

    private static void startObserverThread() {
        final long start = System.currentTimeMillis();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!found) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    final long now = System.currentTimeMillis();
                    double sec = (double) (now - start) / 1000;

                    if(sec == 0) {
                        continue;
                    }

                    long speed = Math.round(size * 100 / sec) / 100;
                    System.out.println("speed : " + speed + ", total : " + size + ", current pass : " + password);
                }

                System.out.println("finished.");
                countDownAll();
            }
        });

        t.start();
    }

    public synchronized static void increment(int count, String pwd) {
        size += count;
        password = pwd;
    }

    private static void countDownAll() {
        while(latch.getCount() > 0) {
            latch.countDown();
        }
    }

    public static synchronized void countDown(int id) {
        latch.countDown();
    }
}
