package com.example.privateteach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button teacherButton;
    private Button studentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
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