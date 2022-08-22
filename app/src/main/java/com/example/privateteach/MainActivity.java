package com.example.privateteach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button teacherButton;
    private Button studentButton;
    String teacherToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        //get teacher token from preference
        SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(R.string.sharedPrefName),MODE_PRIVATE);
        teacherToken = sharedPreferences.getString("token","No User");
        //check if user already logged in and auto-login if does
        if(!teacherToken.equals("No User")){
            Intent intent = new Intent(this, MainPageTeacher.class);
            startActivity(intent);
        }
        teacherButton = findViewById(R.id.teacherButton);
        studentButton = findViewById(R.id.studentButton);

        teacherButton.setOnClickListener(this);
        studentButton.setOnClickListener(this);

    }

    //handle the user button click - move to the right page
    @Override
    public void onClick(View view) {
        Intent intent;
        if (view.getId() == R.id.teacherButton){
            //if the chosen button is teacher
            intent = new Intent(this,TeacherLogin.class);
        }
        else{
            //if the chosen button is student
            intent = new Intent(this, StudentLogin.class);
        }
        startActivity(intent);
    }
}