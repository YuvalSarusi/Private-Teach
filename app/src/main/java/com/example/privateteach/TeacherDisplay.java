package com.example.privateteach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Objects.Lesson;
import Objects.ServerConnection;
import Objects.Student;
import Objects.StudentLessonAdapter;
import Objects.Teacher;
import Objects.TeacherLessonAdapter;

public class TeacherDisplay extends AppCompatActivity {




    private TextView usernameTextView;
    private TextView fullNameTextView;
    private TextView phoneTextView;
    private TextView emailTextView;
    private TextView listTitleTextView;
    private TextView priceTextView;
    private TextView subjectTextView;
    private ListView listView;

    private Teacher teacher;
    private String token;

    private ServerConnection serverConnection;

    private List<Lesson> lessons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_display);
        init();
    }


    private void init(){
        SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(R.string.sharedPrefName),MODE_PRIVATE);
        token = sharedPreferences.getString("token","No User");
        serverConnection = new ServerConnection(this);
        String username = getIntent().getStringExtra("teacherUsername");
        serverConnection.getTeacherByUsername(username, new ServerConnection.JSONObjectResponseListener() {
            @Override
            public void onError(String message) {
                Log.e("Get Teacher By Username","Error, "+message);
            }

            @Override
            public void onResponse(JSONObject response) {
                teacher = Teacher.convertJSONObjectToTeacher(response);
                setTextViews();
                setListView();
            }
        });
    }



    private void setTextViews(){
        usernameTextView = findViewById(R.id.teacherDisplayUsername);
        fullNameTextView = findViewById(R.id.teacherDisplayFullName);
        phoneTextView = findViewById(R.id.teacherDisplayPhone);
        emailTextView = findViewById(R.id.teacherDisplayEmail);
        listTitleTextView = findViewById(R.id.teacherDisplayListTitle);
        priceTextView = findViewById(R.id.teacherDisplayPrice);
        subjectTextView = findViewById(R.id.teacherDisplaySubject);

        usernameTextView.setText(teacher.getUsername());
        fullNameTextView.setText(teacher.getFullName());
        phoneTextView.setText(teacher.getPhoneNumber());
        emailTextView.setText(teacher.getEmail());
        listTitleTextView.setText("Available Lessons of "+teacher.getUsername());
        priceTextView.setText(String.valueOf(teacher.getPrice()));
        subjectTextView.setText(teacher.getSubject());
    }

    private void setListView(){
        listView = findViewById(R.id.teacherDisplayList);
        serverConnection.getTeacherAvailableLessonsByUsername(teacher.getUsername(), new ServerConnection.JSONArrayResponseListener() {
            @Override
            public void onError(String message) {
                Log.e("Get Teacher Available Lessons By Username","Error, "+message);
            }
            @Override
            public void onResponse(JSONArray response) {
                lessons = new ArrayList<>();
                for (int i =0;i<response.length();i++){
                    try {
                        lessons.add(Lesson.convertJSONObjectToLesson(response.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                StudentLessonAdapter studentLessonAdapter = new StudentLessonAdapter(TeacherDisplay.this,lessons);
                listView.setAdapter(studentLessonAdapter);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Lesson lesson = lessons.get(position);
                Log.i("Lesson ID",String.valueOf(lesson.getId()));
                Intent intent = new Intent(TeacherDisplay.this,LessonDisplayStudent.class);
                intent.putExtra("lessonId",lesson.getId());
                startActivity(intent);
            }
        });
    }

}