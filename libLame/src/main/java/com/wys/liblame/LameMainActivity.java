package com.wys.liblame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class LameMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lame_main);
        TextView tvVersion = findViewById(R.id.tv_version);
        tvVersion.setText(LameUtils.getLameVersion());
    }
}