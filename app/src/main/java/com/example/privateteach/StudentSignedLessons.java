package com.example.privateteach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import Objects.Lesson;
import Objects.ServerConnection;
import Objects.StudentLessonAdapter;

public class StudentSignedLessons extends AppCompatActivity {

    private ServerConnection serverConnection;

    private String studentToken;

    private List<Lesson> signedPastLessons;
    private List<Lesson> signedFutureLessons;

    private StudentLessonAdapter studentPastLessonAdapter;
    private StudentLessonAdapter studentFutureLessonAdapter;

    private ListView pastListView;
    private ListView futureListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signed_lessons);
        init();
    }

    private void init(){
        serverConnection = new ServerConnection(this);
        SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(R.string.sharedPrefName),MODE_PRIVATE);
        studentToken = sharedPreferences.getString("token","No User");
        setLists();
    }

    private void getPastSignedLessons(){
        serverConnection.getStudentPastLessons(studentToken, new ServerConnection.JSONArrayResponseListener() {
            @Override
            public void onError(String message) {
                Log.e("Get Student Signed Past Lessons", "Error, "+message);
            }

            @Override
            public void onResponse(JSONArray response) {
                signedPastLessons = new ArrayList<>();
                for (int i=0;i<response.length();i++){
                    try {
                        signedPastLessons.add(Lesson.convertJSONObjectToLesson(response.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                studentPastLessonAdapter = new StudentLessonAdapter(StudentSignedLessons.this,signedPastLessons);
                pastListView.setAdapter(studentPastLessonAdapter);
            }
        });
    }
    private void getFutureSignedLessons(){
        serverConnection.getStudentFutureLessons(studentToken, new ServerConnection.JSONArrayResponseListener() {
            @Override
            public void onError(String message) {
                Log.e("Get Student Signed Future Lessons", "Error, "+message);
            }

            @Override
            public void onResponse(JSONArray response) {
                signedFutureLessons = new ArrayList<>();
                for (int i=0;i<response.length();i++){
                    try {
                        signedFutureLessons.add(Lesson.convertJSONObjectToLesson(response.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                studentFutureLessonAdapter = new StudentLessonAdapter(StudentSignedLessons.this,signedFutureLessons);
                futureListView.setAdapter(studentFutureLessonAdapter);
            }
        });
    }
    private void setLists(){
        pastListView = findViewById(R.id.studentSignedPastLessons);
        futureListView = findViewById(R.id.studentSignedFutureLessons);
        getPastSignedLessons();
        getFutureSignedLessons();

    }
}