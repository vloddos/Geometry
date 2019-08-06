package com.geometry.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.geometry.R;
import com.geometry.Record;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordActivity extends BaseActivity {

    //fixme access modificator???
    @BindView(R.id.lastSquare)
    TextView lastSquare;
    @BindView(R.id.lastCircles)
    TextView lastCircles;
    @BindView(R.id.lastSquares)
    TextView lastSquares;
    @BindView(R.id.lastTriangles)
    TextView lastTriangles;
    @BindView(R.id.lastTotal)
    TextView lastTotal;

    @BindView(R.id.maxSquare)
    TextView maxSquare;
    @BindView(R.id.maxCircles)
    TextView maxCircles;
    @BindView(R.id.maxSquares)
    TextView maxSquares;
    @BindView(R.id.maxTriangles)
    TextView maxTriangles;
    @BindView(R.id.maxTotal)
    TextView maxTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ButterKnife.bind(this);
    }

    private final String RECORD_FILE_NAME = "records";

    private void writeRecord(Record last, Record max) {
        try (
                ObjectOutputStream oout = new ObjectOutputStream(
                        openFileOutput(RECORD_FILE_NAME, MODE_PRIVATE)
                )
        ) {
            oout.writeObject(last);
            oout.writeObject(max);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeRecords() {
        if (!new File(getFilesDir(), RECORD_FILE_NAME).exists())
            writeRecord(new Record(), new Record());
    }

    private void initViews(Record last, Record max) {
        lastSquare.setText("" + last.square);
        lastCircles.setText("" + last.circles);
        lastSquares.setText("" + last.squares);
        lastTriangles.setText("" + last.triangles);
        lastTotal.setText("" + last.total);

        maxSquare.setText("" + max.square);
        maxCircles.setText("" + max.circles);
        maxSquares.setText("" + max.squares);
        maxTriangles.setText("" + max.triangles);
        maxTotal.setText("" + max.total);
    }

    @Override
    protected void onStart() {
        super.onStart();

        initializeRecords();

        Record last, max;
        try (
                ObjectInputStream oin = new ObjectInputStream(
                        openFileInput(RECORD_FILE_NAME)
                )
        ) {
            last = (Record) oin.readObject();
            max = (Record) oin.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        Record record = (Record) getIntent().getSerializableExtra("record");
        if (record != null) {
            last = record;
            if (record.square > max.square) max.square = record.square;
            if (record.circles > max.circles) max.circles = record.circles;
            if (record.squares > max.squares) max.squares = record.squares;
            if (record.triangles > max.triangles) max.triangles = record.triangles;
            if (record.total > max.total) max.total = record.total;
            writeRecord(last, max);
        }

        initViews(last, max);

        hideSystemUI();
    }
}
