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

import java.text.SimpleDateFormat;

import Objects.Lesson;
import Objects.ServerConnection;

public class LessonDisplayTeacher extends AppCompatActivity {


    private TextView studentTextView;
    private TextView usernameTextView;
    private TextView startTextView;
    private TextView endTextView;
    private Button deleteLessonButton;

    private Lesson lesson;

    ServerConnection serverConnection;

    private int lessonId;
    private String teacherToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_display_teacher);
        init();
    }


    private void init(){
        lessonId = getIntent().getIntExtra("lessonId",0);

        SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(R.string.sharedPrefName),MODE_PRIVATE);
        teacherToken = sharedPreferences.getString("token","No User");

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
                setSignLessonButton();
            }
        });
    }

    private void setTextViews(){
        endTextView = findViewById(R.id.lessonDisplayTeacherEnd);
        startTextView = findViewById(R.id.lessonDisplayTeacherStart);
        studentTextView = findViewById(R.id.lessonDisplayTeacherStudent);;
        usernameTextView = findViewById(R.id.lessonDisplayTeacherUsername);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        endTextView.setText(format.format(lesson.getEndDate()));
        startTextView.setText(format.format(lesson.getStartDate()));
        if (lesson.getStudent() == null){
            usernameTextView.setText("Clear Lesson");
        }
        else{
            usernameTextView.setText(lesson.getStudent().getUsername());
        }
    }

    private void setSignLessonButton(){
        deleteLessonButton = findViewById(R.id.lessonDisplayStudentButton);
        if (lesson.getStudent() != null){
            deleteLessonButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    serverConnection.deleteLesson(teacherToken, lessonId, new ServerConnection.StringResponseListener() {
                        @Override
                        public void onError(String message) {
                            Log.e("Delete Lesson","Error, "+message);
                        }

                        @Override
                        public void onResponse(String response) {
                            String message = "Error Happened. Try Again Later";
                            final String TAG = "Delete Lesson";
                            if (response.equals("unExistLesson")){
                                Log.e(TAG,"Error, lesson doesn't exist");
                                message = "Error, Please Try Again To Enter This Lesson";
                            }
                            else if (response.equals("wrongTeacher")){
                                Log.e(TAG,"Error, lesson doesn't belong to this teacher");
                                message = "Error, Please Try Again To Enter This Lesson";
                            }
                            else if (response.equals("signedLesson")){
                                Log.e(TAG,"Problem, Button need to be disabled");
                                message = "You Can't Delete Signed Lesson";
                            }
                            else if (response.equals("success")){
                                message = "Your Lesson Has Been Deleted";
                            }
                            Toast.makeText(LessonDisplayTeacher.this,message,Toast.LENGTH_LONG).show();
                            startActivity(new Intent(LessonDisplayTeacher.this,MainPageTeacher.class));
                        }
                    });
                }
            });
        }
        else{
            deleteLessonButton.setEnabled(false);
        }
    }
}