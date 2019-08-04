package com.geometry;

import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void switchTest() {
        int a = 3;
        switch (a) {
            default:
                System.out.println("default");
            case 2:
                System.out.println("down to 2");
                System.out.println(2);
            case 3:
                System.out.println("down to 3");
                System.out.println(3);
        }
    }

    @Test
    public void floatAssignmentToDouble() {
        System.out.println(2.0 * Float.MAX_VALUE * Float.MAX_VALUE);
    }

    @Test
    public void futureTest1() {
        //тут future которое возвращает holder
        FutureTask<Integer> future = new FutureTask<>(() -> 5);

        new Thread(//тут выполняется surfaceCreated
                () -> {
                    System.out.println("surfaceCreated thread starts future");
                    future.run();
                }
        ).start();

        new Thread(//draw thread
                () -> {
                    try {
                        System.out.println("draw thread get holder " + future.get());
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //...
                }
        ).start();
    }

    @Test
    public void futureTest2() {
        //тут future которое возвращает handler
        FutureTask<Object> future = new FutureTask<>(Object::new);

        new Thread(//animation thread
                () -> {
                    /*
                    Looper.prepare();
                    looper = Looper.myLooper();
                    handler = new Handler();
                     */
                    System.out.println("animation thread starts future");
                    future.run();
                }
        ).start();

        new Thread(//entity thread
                () -> {
                    try {
                        System.out.println("entity thread get handler " + future.get());
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //...
                }
        ).start();

        new Thread(//intersection thread
                () -> {
                    try {
                        System.out.println("intersection thread get handler " + future.get());
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //...
                }
        ).start();

        //todo test record thread
    }

    @Test
    public void threadSleepTest() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("awaken");
    }
}