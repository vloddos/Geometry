package com.geometry.activity;

import android.os.Bundle;
import android.util.Log;

import com.geometry.R;

import java.util.Optional;

public class RecordActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
    }

    @Override
    protected void onStart() {
        super.onStart();
        hideSystemUI();
        Optional
                .ofNullable(getIntent().getSerializableExtra("record"))
                .ifPresent(record -> Log.i("RecordActivity", record.toString()));
    }
}
