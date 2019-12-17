package com.adanadhe.ainventory;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeInfo);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
    }
}
