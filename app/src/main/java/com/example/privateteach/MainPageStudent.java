package com.example.privateteach;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Objects.Lesson;
import Objects.ServerConnection;
import Objects.StudentLessonAdapter;

public class MainPageStudent extends AppCompatActivity {

    private ServerConnection serverConnection;
    private StudentLessonAdapter studentLessonAdapter;

    private Spinner subjectSpinner;
    private Button filter;
    private EditText maxPriceEditText;
    private ListView filteredListView;

    private String selectedSubject;
    private String maxPrice;
    private List<Lesson> filteredList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_student);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.student_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.studentMenuLogOut:
                //log out - delete from shared preference
                SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(R.string.sharedPrefName),MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.teacherMenuSettings:
                //go to settings
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init() {
        serverConnection = new ServerConnection(this);
        filteredList = new ArrayList<>();
        filteredListView = findViewById(R.id.studentFilterListView);
        getAllAvailableLessons();
        setSpinner();
        filter = findViewById(R.id.filterButton);
        maxPriceEditText = findViewById(R.id.filterEditText);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filteredList = new ArrayList<>();
                maxPrice = maxPriceEditText.getText().toString();
                serverConnection.getFilteredLesson(selectedSubject, maxPrice, new ServerConnection.JSONArrayResponseListener() {
                    @Override
                    public void onError(String message) {
                        Log.e("Get Filtered Lessons", "Error, "+message);
                    }
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i=0; i<response.length(); i++){
                            try {
                                filteredList.add(Lesson.convertJSONObjectToLesson(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        StudentLessonAdapter studentLessonAdapter = new StudentLessonAdapter(MainPageStudent.this,filteredList);
                        filteredListView.setAdapter(studentLessonAdapter);
                        filteredListView.invalidate();
                    }
                });

            }
        });
    }
    private void setSpinner(){
        subjectSpinner = findViewById(R.id.studentFilterSpinner);
        List<String> options = new ArrayList<>();
        options.add("All");
        options.addAll(Arrays.asList(getResources().getStringArray(R.array.subjects)));
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
    private void getAllAvailableLessons(){
        serverConnection.getFilteredLesson("All", "", new ServerConnection.JSONArrayResponseListener() {
            @Override
            public void onError(String message) {
                Log.e("Get ALl Available Lessons", "Error, "+message);
            }
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i<response.length(); i++){
                    try {
                        filteredList.add(Lesson.convertJSONObjectToLesson(response.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                studentLessonAdapter = new StudentLessonAdapter(MainPageStudent.this,filteredList);
                filteredListView.setAdapter(studentLessonAdapter);
            }
        });
    }

}