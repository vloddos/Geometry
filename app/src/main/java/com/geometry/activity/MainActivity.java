package com.geometry.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.geometry.CustomSurfaceView;
import com.geometry.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
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