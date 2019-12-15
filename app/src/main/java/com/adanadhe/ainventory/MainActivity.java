package com.adanadhe.ainventory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements AdapterBarangRecyclerView.FirebaseDataListener {
    EditText edtTitle;
    EditText edtMessage;

    FloatingActionButton btCreateDB;
    Button btViewDB;
    FloatingActionButton btnSettings;
    TextView txtTotalHarga;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAA3PRkq-U:APA91bFlh3Mac-doL9VuEtRtJtZSFXDmQGSFiiUrWhMd7lJ_2iEpZ1N9OkKUOxnj9Jgs_gHgLvauU1dUlPc_ujC_6BF9kbmQElJA0-YGlUPefrJwXqSxI-kb4-_WmaHZ8wn9X_rob928";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;

    private DatabaseReference database;
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Barang> daftarBarang;

    int totalHarga = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences("SETTINGS", MODE_PRIVATE);
        boolean useDarkMode = preferences.getBoolean("DARK_MODE", false);

        if (useDarkMode) {
            setTheme(R.style.ActivityThemeDark);
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


//        Window mWindow = getWindow();
//        mWindow.getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        edtTitle = findViewById(R.id.judulnya);
        edtMessage = findViewById(R.id.pesannya);
        final Button btnSend = findViewById(R.id.submit);

        btCreateDB = findViewById(R.id.fab_add);
        btViewDB = findViewById(R.id.bt_viewdata);
        btnSettings = findViewById(R.id.fab_settings);
        txtTotalHarga = findViewById(R.id.total_harga);

        rvView = (RecyclerView) findViewById(R.id.rv_main);
        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);


        database = FirebaseDatabase.getInstance().getReference();

        database.child("barang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                /**
                 * Saat ada data baru, masukkan datanya ke ArrayList
                 */
                daftarBarang = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    /**
                     * Mapping data pada DataSnapshot ke dalam object Barang
                     * Dan juga menyimpan primary key pada object Barang
                     * untuk keperluan Edit dan Delete data
                     */
                    Barang barang = noteDataSnapshot.getValue(Barang.class);
                    barang.setKey(noteDataSnapshot.getKey());

                    /**
                     * Menambahkan object Barang yang sudah dimapping
                     * ke dalam ArrayList
                     */
                    daftarBarang.add(barang);
                    totalHarga += Integer.parseInt(barang.getHarga());
                }

                /**
                 * Inisialisasi adapter dan data barang dalam bentuk ArrayList
                 * dan mengeset Adapter ke dalam RecyclerView
                 */
                adapter = new AdapterBarangRecyclerView(daftarBarang, MainActivity.this);
                rvView.setAdapter(adapter);
                txtTotalHarga.setText(String.valueOf(totalHarga));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                /**
                 * Kode ini akan dipanggil ketika ada error dan
                 * pengambilan data gagal dan memprint error nya
                 * ke LogCat
                 */
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            }
        });


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TOPIC = "/topics/userABC"; //topic must match with what the receiver subscribed to
                NOTIFICATION_TITLE = edtTitle.getText().toString();
                NOTIFICATION_MESSAGE = edtMessage.getText().toString();

                JSONObject notification = new JSONObject();
                JSONObject notifcationBody = new JSONObject();
                try {
                    notifcationBody.put("title", NOTIFICATION_TITLE);
                    notifcationBody.put("message", NOTIFICATION_MESSAGE);

                    notification.put("to", TOPIC);
                    notification.put("data", notifcationBody);
                } catch (JSONException e) {
                    Log.e(TAG, "onCreate: " + e.getMessage());
                }
                sendNotification(notification);
            }
        });

        btCreateDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // kelas yang akan dijalankan ketika tombol Create/Insert Data diklik
                startActivity(FirebaseDBCreateActivity.getActIntent(MainActivity.this));
            }
        });

        btViewDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // kode untuk tutorial selanjutnya
                startActivity(FirebaseDBReadActivity.getActIntent(MainActivity.this));
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(SettingsActivity.getActIntent(MainActivity.this));
            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();

        SharedPreferences preferences = getSharedPreferences("SETTINGS", MODE_PRIVATE);
        boolean useDarkMode = preferences.getBoolean("DARK_MODE", false);

        if (useDarkMode) {
            setTheme(R.style.ActivityThemeDark);
        }
        recreate();

    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                        edtTitle.setText("");
                        edtMessage.setText("");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onDeleteData(Barang barang, final int position) {
        /**
         * Kode ini akan dipanggil ketika method onDeleteData
         * dipanggil dari adapter lewat interface.
         * Yang kemudian akan mendelete data di Firebase Realtime DB
         * berdasarkan key barang.
         * Jika sukses akan memunculkan Toast
         */
        if(database!=null){            database.child("barang").child(barang.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this,"success delete", Toast.LENGTH_LONG).show();
            }
        });

        }
    }
}
