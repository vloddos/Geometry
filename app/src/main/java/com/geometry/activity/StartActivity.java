package com.geometry.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.geometry.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartActivity extends BaseActivity {

    private static final int LOSE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        hideSystemUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            if (requestCode == LOSE)
                startActivity(
                        new Intent(this, RecordActivity.class)
                                .putExtra(
                                        "record",
                                        data.getSerializableExtra("record")
                                )
                );
    }

    @OnClick(R.id.start)
    void start(View view) {
        startActivityForResult(
                new Intent(this, MainActivity.class),
                LOSE
        );
    }

    @OnClick(R.id.records)
    void records(View view) {
        startActivity(new Intent(this, RecordActivity.class));
    }
}