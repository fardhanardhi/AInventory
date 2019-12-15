package com.adanadhe.ainventory;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.annotations.Nullable;

import androidx.appcompat.app.AppCompatActivity;

public class FirebaseDBReadSingleActivity extends AppCompatActivity {

    private Button btSubmit;
    private EditText etNama;
    private EditText etMerk;
    private EditText etHarga;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences("SETTINGS", MODE_PRIVATE);
        boolean useDarkMode = preferences.getBoolean("DARK_MODE", false);

        if (useDarkMode) {
            setTheme(R.style.ActivityThemeDark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_create);

//        Window mWindow = getWindow();
//        mWindow.getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        
        etNama = (EditText) findViewById(R.id.et_namabarang);
        etMerk = (EditText) findViewById(R.id.et_merkbarang);
        etHarga = (EditText) findViewById(R.id.et_hargabarang);
        btSubmit = (Button) findViewById(R.id.bt_submit);

        etNama.setEnabled(false);
        etMerk.setEnabled(false);
        etHarga.setEnabled(false);
        btSubmit.setVisibility(View.GONE);

        Barang barang = (Barang) getIntent().getSerializableExtra("data");
        if(barang!=null){
            etNama.setText(barang.getNama());
            etMerk.setText(barang.getMerk());
            etHarga.setText(barang.getHarga());
        }
    }

    public static Intent getActIntent(Activity activity){
        return new Intent(activity, FirebaseDBReadSingleActivity.class);
    }
}