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
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import Objects.Lesson;
import Objects.ServerConnection;
import Objects.Student;
import Objects.TeacherLessonAdapter;

public class StudentDisplay extends AppCompatActivity {

    private TextView usernameTextView;
    private TextView fullNameTextView;
    private TextView phoneTextView;
    private TextView emailTextView;
    private TextView listTitleTextView;
    private ListView listView;

    private Student student;
    private String token;

    private ServerConnection  serverConnection;

    private List<Lesson> lessons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_display);
        init();
    }

    private void init(){
        SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(R.string.sharedPrefName),MODE_PRIVATE);
        token = sharedPreferences.getString("token","No User");
        serverConnection = new ServerConnection(this);
        String username = getIntent().getStringExtra("studentUsername");
        serverConnection.getStudentByUsername(username, new ServerConnection.JSONObjectResponseListener() {
            @Override
            public void onError(String message) {
                Log.e("Get Student By Username","Error, "+message);
            }

            @Override
            public void onResponse(JSONObject response) {
                student = Student.convertJSONObjectToStudent(response);
                setTextViews();
                setListView();
            }
        });
    }

    private void setTextViews(){
        usernameTextView = findViewById(R.id.studentDisplayUsername);
        fullNameTextView = findViewById(R.id.studentDisplayFullName);
        phoneTextView = findViewById(R.id.studentDisplayPhone);
        emailTextView = findViewById(R.id.studentDisplayEmail);
        listTitleTextView = findViewById(R.id.studentDisplayListTitle);

        usernameTextView.setText(student.getUsername());
        fullNameTextView.setText(student.getFullName());
        phoneTextView.setText(student.getPhoneNumber());
        emailTextView.setText(student.getEmail());
        listTitleTextView.setText("Lessons With "+student.getUsername());
    }

    private void setListView(){
        listView = findViewById(R.id.studentDisplayList);
        serverConnection.getStudentAndTeacherLessons(student.getUsername(), token, new ServerConnection.JSONArrayResponseListener() {
            @Override
            public void onError(String message) {
                Log.e("Get Student And Teacher Lessons","Error, "+message);
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
                TeacherLessonAdapter teacherLessonAdapter = new TeacherLessonAdapter(StudentDisplay.this,lessons);
                listView.setAdapter(teacherLessonAdapter);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Lesson lesson = lessons.get(position);
                Log.i("Lesson ID",String.valueOf(lesson.getId()));
                Intent intent = new Intent(StudentDisplay.this,LessonDisplayTeacher.class);
                intent.putExtra("lessonId",lesson.getId());
                startActivity(intent);
            }
        });
    }
}