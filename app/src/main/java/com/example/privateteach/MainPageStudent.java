package com.example.privateteach;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import Objects.ServerConnection;

public class MainPageStudent extends AppCompatActivity {

    private SeekBar seekBar;
    private Spinner subjectSpinner;
    private String selectedSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_student);
        init();
    }

    public void init(){
        setSeekBar();
        subjectSpinner = findViewById(R.id.studentFilterSpinner);
        List<String> options = Arrays.asList(getResources().getStringArray(R.array.subjects));
        options.add("All");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,options);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(arrayAdapter);
        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSubject = arrayAdapter.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //there is a default value
                selectedSubject = "";
            }
        });
    }

    public void setSeekBar(){
        seekBar = findViewById(R.id.studentFilterSeekBar);
        seekBar.setProgress(10);
        ServerConnection serverConnection = new ServerConnection(this);
        serverConnection.getHighestPrice(new ServerConnection.StringResponseListener() {
            @Override
            public void onError(String message) {
                Log.e("Get Max Price", "Error, "+message);
            }

            @Override
            public void onResponse(String response) {
                try{
                    seekBar.setMax(Integer.parseInt(response));
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }
            }
        });
        serverConnection.getLowestPrice(new ServerConnection.StringResponseListener() {
            @Override
            public void onError(String message) {
                Log.e("Get Lowest Price", "Error, "+message);
            }

            @Override
            public void onResponse(String response) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    seekBar.setMin(Integer.parseInt(response));
                }
            }
        });
    }
}