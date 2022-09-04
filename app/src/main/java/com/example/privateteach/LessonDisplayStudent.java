package com.example.privateteach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.nio.file.Files;
import java.text.SimpleDateFormat;

import Objects.Lesson;
import Objects.ServerConnection;

public class LessonDisplayStudent extends AppCompatActivity {


    private TextView teacherTextView;
    private TextView usernameTextView;
    private TextView priceTextView;
    private TextView startTextView;
    private TextView endTextView;
    private TextView subjectTextView;
    private Button signLessonButton;

    private Lesson lesson;

    ServerConnection serverConnection;

    private int lessonId;
    private String studentToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_display_student);
        init();
    }

    private void init(){
        lessonId = getIntent().getIntExtra("lessonId",0);
        SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(R.string.sharedPrefName),MODE_PRIVATE);
        studentToken = sharedPreferences.getString("token","No User");
        serverConnection = new ServerConnection(this);
        serverConnection.getLessonById(lessonId, new ServerConnection.JSONObjectResponseListener() {
            @Override
            public void onError(String message) {
                Log.e("Get Lesson by Id","Error, "+message);
            }

            @Override
            public void onResponse(JSONObject response) {
                lesson = Lesson.convertJSONObjectToLesson(response);
                setTextViews();
            }
        });
        setSignLessonButton();
    }

    private void setTextViews(){
        endTextView = findViewById(R.id.lessonDisplayStudentEnd);
        startTextView = findViewById(R.id.lessonDisplayStudentStart);
        teacherTextView = findViewById(R.id.lessonDisplayStudentTeacher);
        priceTextView = findViewById(R.id.lessonDisplayStudentPrice);
        subjectTextView = findViewById(R.id.lessonDisplayStudentSubject);
        usernameTextView = findViewById(R.id.lessonDisplayStudentUsername);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        endTextView.setText(format.format(lesson.getEndDate()));
        startTextView.setText(format.format(lesson.getStartDate()));
        priceTextView.setText(String.valueOf(lesson.getTeacher().getPrice()));
        subjectTextView.setText(lesson.getTeacher().getSubject());
        usernameTextView.setText(lesson.getTeacher().getUsername());
    }

    private void setSignLessonButton(){
        signLessonButton = findViewById(R.id.lessonDisplayStudentButton);
        signLessonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverConnection.signIntoLesson(studentToken,lessonId, new ServerConnection.StringResponseListener() {
                    @Override
                    public void onError(String message) {
                        Log.e("Sign Into Lesson", "Error, "+message);
                    }
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("doesn'tExistLesson")){
                            Toast.makeText(LessonDisplayStudent.this,"ERROR. Lesson Doesn't Exist.\nRefresh the Main Page and try again",Toast.LENGTH_LONG);
                            Log.e("Sign Into Lesson", "Response error: "+response);
                            startActivity(new Intent(LessonDisplayStudent.this,MainPageStudent.class));
                        }
                        else if (response.equals("signedLesson")){
                            Toast.makeText(LessonDisplayStudent.this,"This Lesson has been capture.\nBack to Main Page and try another lesson. Sorry :(",Toast.LENGTH_LONG);
                            Log.e("Sign Into Lesson", "Response error: "+response);
                            startActivity(new Intent(LessonDisplayStudent.this,MainPageStudent.class));
                        }
                        else if (response.equals("doesn'tExistStudent")){
                            Toast.makeText(LessonDisplayStudent.this,"Error at User. log out & in and try again",Toast.LENGTH_LONG);
                            Log.e("Sign Into Lesson", "Response error: "+response);
                            startActivity(new Intent(LessonDisplayStudent.this,MainPageStudent.class));
                        }
                        else if (response.equals("success")){
                            Toast.makeText(LessonDisplayStudent.this,"Successfully Sign Into Lesson! Good Lick :)",Toast.LENGTH_LONG);
                            Log.i("Sign Into Lesson", "Success: "+response);
                            startActivity(new Intent(LessonDisplayStudent.this,MainPageStudent.class));
                        }
                        else{
                            Toast.makeText(LessonDisplayStudent.this,"Error At Signing",Toast.LENGTH_LONG);
                            Log.e("Sign Into Lesson", "Success: "+response);
                        }
                    }
                });
            }
        });
    }
}