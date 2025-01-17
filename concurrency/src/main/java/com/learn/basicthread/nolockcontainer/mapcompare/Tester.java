package com.learn.basicthread.nolockcontainer.mapcompare;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hechao on 2017/5/16.
 */
public abstract class Tester<C> {
    static int testReps = 1;
    static int readOrWriterCycles = 1000;
    static int containerSize = 1000;
    abstract C containerInitializer();
    abstract void startReadersAndWriters();
    C testContainer;
    String testId;
    int nReaders;
    int nWriters;
    volatile long readResult = 0;
    volatile long readTime = 0;
    volatile long writeTime = 0;
    CountDownLatch endLatch;
    static ExecutorService exec = Executors.newCachedThreadPool();
    Integer[] writeData = new Integer[containerSize];
    Tester(String testId, int nReaders, int nWriters) {
        this.testId = testId + " " + nReaders + "r " + nWriters + "w";
        this.nReaders = nReaders;
        this.nWriters = nWriters;
        Random rand = new Random(47);
        for (int i = 0; i < containerSize; i++) {
            writeData[i] = rand.nextInt();
        }

        for (int i = 0; i < testReps; i++) {
            runTest();
            readTime = 0;
            writeTime = 0;
        }


    }

    void runTest() {
        endLatch = new CountDownLatch(nReaders + nWriters);
        testContainer = containerInitializer();
        startReadersAndWriters();
        try {
            endLatch.await();
        } catch (InterruptedException e) {
            System.out.println("endLatch interrupted");
        }

        System.out.printf("%-27s %14d %14d\n",testId,readTime,writeTime);
        if (readTime != 0 && writeTime != 0) {
            System.out.printf("%-27s %14d\n","readTime + writeTime = ",readTime + writeTime);
        }
    }

    abstract class TestTask implements Runnable {
        abstract void test();
        abstract void putResults();
        long duration;

        @Override
        public void run() {
            long startTime = System.nanoTime();
            test();
            duration = System.nanoTime() - startTime;
            synchronized (Tester.this) {
                putResults();
            }
            endLatch.countDown();
        }
    }

    public static void initMain() {
//        testReps = 10;
//        readOrWriterCycles = 5;
//        containerSize = 10;
        System.out.printf("%-27s %14s %14s\n","Type","Read time","Write time");

    }


}
