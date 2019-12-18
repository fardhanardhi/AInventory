package com.adanadhe.ainventory;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Context contextThemeWrapper;

        SharedPreferences preferences = this.getActivity().getSharedPreferences("SETTINGS", MODE_PRIVATE);
        boolean useDarkMode = preferences.getBoolean("DARK_MODE", false);

        if (useDarkMode) {
            contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.ActivityThemeDark);
        } else {
            contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.ActivityThemeLight);
        }

        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        View rootView = localInflater.inflate(R.layout.fragment_home, container, false);

        TextView text = (TextView) rootView.findViewById(R.id.tanggal);

        Date today = Calendar.getInstance().getTime();//getting date
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");//formating according to my need
        String date = formatter.format(today);
        text.setText("THIS IS " + date.toUpperCase());

        Button button = (Button) rootView.findViewById(R.id.btnKelola);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        Button button2 = (Button) rootView.findViewById(R.id.btnTest);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SendActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

}
