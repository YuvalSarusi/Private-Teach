package com.example.privateteach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import Objects.ServerConnection;
import Objects.Student;


public class StudentSettingsPage extends AppCompatActivity {

    private Student student;

    private ServerConnection serverConnection;

    private TextView usernameTextView;
    private EditText fullNameEdit;
    private EditText phoneEditText;
    private EditText emailEditText;
    private Button saveButton;
    private Button backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_settings_page);
        init();
    }

    private void init(){
        serverConnection = new ServerConnection(this);
        getStudentInfo();
        setButtons();
    }

    private void getStudentInfo(){
        SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(R.string.sharedPrefName),MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "No User");
        serverConnection.getStudentByToken(token, new ServerConnection.JSONObjectResponseListener() {
            @Override
            public void onError(String message) {
                Log.e("Get Student By Token","Error, "+message);
            }
            @Override
            public void onResponse(JSONObject response) {
                student = Student.convertJSONObjectToStudent(response);
                setEditTexts();
                setTextView();
            }
        });
    }

    private void setTextView(){
        usernameTextView = findViewById(R.id.studentSettingsUsername);
        usernameTextView.setText(student.getUsername());
    }
    private void setEditTexts(){
        fullNameEdit = findViewById(R.id.studentSettingsFullNameEdit);
        phoneEditText = findViewById(R.id.studentSettingsPhoneEdit);
        emailEditText = findViewById(R.id.studentSettingsEmailEdit);

        fullNameEdit.setHint(student.getFullName());
        phoneEditText.setHint(student.getPhoneNumber());
        emailEditText.setHint(student.getEmail());
    }
    private void setButtons(){
        saveButton = findViewById(R.id.studentSettingsSave);
        backButton = findViewById(R.id.studentSettingsBack);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = fullNameEdit.getText().toString();
                String phone = phoneEditText.getText().toString();
                String email = emailEditText.getText().toString();
                serverConnection.changeStudentDetails(student.getToken(), fullName, phone, email, new ServerConnection.StringResponseListener() {
                    @Override
                    public void onError(String message) {
                        Log.e("Change Student Details","Error, "+message);
                    }

                    @Override
                    public void onResponse(String response) {
                        if (response.equals("success")){
                            Toast.makeText(StudentSettingsPage.this,"Your Details has been changed!",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(StudentSettingsPage.this,MainPageStudent.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentSettingsPage.this,MainPageStudent.class);
                startActivity(intent);
            }
        });
    }
}