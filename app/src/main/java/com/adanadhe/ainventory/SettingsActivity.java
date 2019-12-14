package com.adanadhe.ainventory;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {
    private Switch toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences("SETTINGS", MODE_PRIVATE);
        boolean useDarkMode = preferences.getBoolean("DARK_MODE", false);

        if(useDarkMode) {
            setTheme(R.style.ActivityThemeDark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        toggle = findViewById(R.id.switchCompat);

        toggle.setChecked(useDarkMode);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                toggleDarkMode(isChecked);
            }
        });
    }

    public static Intent getActIntent(Activity activity){
        return new Intent(activity, SettingsActivity.class);
    }

    private void toggleDarkMode(boolean darkMode) {
        SharedPreferences.Editor editor = getSharedPreferences("SETTINGS", MODE_PRIVATE).edit();
        editor.putBoolean("DARK_MODE", darkMode);
        editor.apply();

        Intent intent = getIntent();
        finish();

        startActivity(intent);
        this.overridePendingTransition(0,0);
    }
}