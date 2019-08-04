package com.geometry.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.geometry.CustomSurfaceView;
import com.geometry.R;

public class MainActivity extends BaseActivity {

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.linearLayout);
    }

    @Override
    protected void onStart() {
        super.onStart();
        hideSystemUI();
        CustomSurfaceView customSurfaceView = new CustomSurfaceView(getApplicationContext());
        customSurfaceView.setOnGameOver(
                record -> {
                    setResult(RESULT_OK, new Intent().putExtra("record", record));
                    finish();
                }
        );
        linearLayout.addView(customSurfaceView);
        customSurfaceView.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );
    }
}