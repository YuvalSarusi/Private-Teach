package com.example.privateteach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Objects.ServerConnection;
import Objects.Student;


public class StudentSettingsPage extends AppCompatActivity {

    private Student student;

    private ServerConnection serverConnection;

    private TextView usernameTextView;
    private EditText fullNameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private Button saveButton;
    private Button backButton;

    private String fullName;
    private String phoneNumber;
    private String email;


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
        fullNameEditText = findViewById(R.id.studentSettingsFullNameEdit);
        phoneEditText = findViewById(R.id.studentSettingsPhoneEdit);
        emailEditText = findViewById(R.id.studentSettingsEmailEdit);

        fullNameEditText.setHint(student.getFullName());
        phoneEditText.setHint(student.getPhoneNumber());
        emailEditText.setHint(student.getEmail());

        fullName = student.getFullName();
        phoneNumber = student.getPhoneNumber();
        email = student.getEmail();

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                email = emailEditText.getText().toString();
                //singUpButton.setEnabled(TeacherSignUp.this.checkLegalData());
                saveButton.setEnabled(StudentSettingsPage.this.checkLegalData());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fullNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fullName = fullNameEditText.getText().toString();
                saveButton.setEnabled(StudentSettingsPage.this.checkLegalData());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        phoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phoneNumber = phoneEditText.getText().toString();
                saveButton.setEnabled(StudentSettingsPage.this.checkLegalData());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void setButtons(){
        saveButton = findViewById(R.id.studentSettingsSave);
        backButton = findViewById(R.id.studentSettingsBack);
        saveButton.setEnabled(false);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverConnection.changeStudentDetails(student.getToken(), fullName, phoneNumber, email, new ServerConnection.StringResponseListener() {
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

    private boolean checkLegalData(){
        boolean answer = false;
        Pattern phoneNumberPattern = Pattern.compile("^05\\d([-]?)\\d{7}$");
        Pattern emailPattern = Pattern.compile("^(.+)@(.+)$");
        Matcher phoneMatcher = phoneNumberPattern.matcher(phoneNumber);
        Matcher emailMatcher = emailPattern.matcher(email);
        answer = emailMatcher.matches() &&
                phoneMatcher.matches();
        return answer;
    }
}