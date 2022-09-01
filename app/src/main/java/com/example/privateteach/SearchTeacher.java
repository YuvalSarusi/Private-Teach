package com.example.privateteach;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import Objects.ServerConnection;
import Objects.Teacher;
import Objects.TeacherItemListAdapter;
import Objects.TeacherSelectDialog;

public class SearchTeacher extends AppCompatActivity implements TeacherSelectDialog.PassInfoListener {



    private ServerConnection serverConnection;

    private List<Teacher> teacherList;

    private ListView listView;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_teacher);
        init();
    }

    private void init(){
        searchButton = findViewById(R.id.teacherSearchButton);
        TeacherSelectDialog teacherSelectDialog = new TeacherSelectDialog();
        serverConnection = new ServerConnection(this);
        listView = findViewById(R.id.filterTeacherList);
        serverConnection.getFilterTeacher("", "All", "", new ServerConnection.JSONArrayResponseListener() { //get no filtered list
            @Override
            public void onError(String message) {
                Log.e("Get Filtered Teacher (All)","Error, "+message);
            }
            @Override
            public void onResponse(JSONArray response) {
                teacherList = new ArrayList<>();
                for (int i=0;i<response.length(); i++){
                    try {
                        teacherList.add(Teacher.convertJSONObjectToTeacher(response.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                TeacherItemListAdapter listAdapter = new TeacherItemListAdapter(SearchTeacher.this,teacherList);
                listView.setAdapter(listAdapter);

            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teacherSelectDialog.show(getSupportFragmentManager(),"searchTeacher");
            }
        });
    }

    @Override
    public void applyInfo(String username, String subject, String price) {
        serverConnection.getFilterTeacher(username, subject, price, new ServerConnection.JSONArrayResponseListener() {
            @Override
            public void onError(String message) {
                Log.e("Get Filter Teacher", "Error, "+message);
            }
            @Override
            public void onResponse(JSONArray response) {
                teacherList = new ArrayList<>();
                for (int i=0; i<response.length();i++){
                    try {
                        teacherList.add(Teacher.convertJSONObjectToTeacher(response.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                TeacherItemListAdapter listAdapter = new TeacherItemListAdapter(SearchTeacher.this,teacherList);
                listView.setAdapter(listAdapter);
                listView.invalidate();
            }
        });
    }
}