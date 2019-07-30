package com.geometry.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.geometry.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void start(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void records(View view) {
        startActivity(new Intent(this, RecordActivity.class));
    }
}