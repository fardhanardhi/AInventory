package com.adanadhe.ainventory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FirstActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences("SETTINGS", MODE_PRIVATE);
        boolean useDarkMode = preferences.getBoolean("DARK_MODE", false);
        boolean usePushNotification = preferences.getBoolean("PUSH_NOTIFICATION", true);

        if (useDarkMode) {
            setTheme(R.style.ActivityFirstThemeDark);
        }
        else {
            setTheme(R.style.ActivityFirstThemeLight);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        loadFragment(new HomeFragment());
        bottomNavigationView = findViewById(R.id.navigation);
        // beri listener pada saat item/menu bottomnavigation terpilih
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.action_menu:
                fragment = new HomeFragment();
                break;
            case R.id.action_setting:
                fragment = new SettingFragment();
                break;
        }
        return loadFragment(fragment);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.recreate();
        bottomNavigationView.setSelectedItemId(R.id.action_menu);
    }
}
