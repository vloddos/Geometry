package com.geometry;

import org.junit.Test;

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
}