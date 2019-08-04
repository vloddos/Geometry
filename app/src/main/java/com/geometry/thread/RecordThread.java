package com.geometry.thread;

import com.geometry.Record;
import com.geometry.figure.Circle;
import com.geometry.figure.Figure;
import com.geometry.figure.Square;
import com.geometry.figure.Triangle;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.FutureTask;

public class RecordThread extends Thread {

    private Record record = new Record();
    private BlockingQueue<Figure> figures = new ArrayBlockingQueue<>(100);
    private FutureTask<Record> recordFutureTask = new FutureTask<>(() -> record);

    public FutureTask<Record> getRecordFutureTask() {
        return recordFutureTask;
    }

    @Override
    public void run() {
        while (true) {
            Figure figure = null;

            try {
                figure = figures.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (figure != null) {
                if (figure instanceof Circle)
                    ++record.circles;
                else if (figure instanceof Square)
                    ++record.squares;
                else if (figure instanceof Triangle)
                    ++record.triangles;
                else {
                    recordFutureTask.run();
                    return;
                }
                record.square += figure.getSquare();
                ++record.total;
            }
        }
    }

    public void count(Figure figure) {
        try {
            figures.put(figure);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
