package com.example.privateteach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Objects.ServerConnection;
import Objects.Student;
import Objects.Teacher;

public class TeacherSettingsPage extends AppCompatActivity {


    private Teacher teacher;

    private ServerConnection serverConnection;

    private TextView usernameTextView;
    private EditText fullNameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText priceEditText;
    private Spinner subjectSpinner;
    private Button saveButton;
    private Button backButton;

    private String fullName;
    private String phoneNumber;
    private String email;
    private int price;
    private String selectedSubject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_settings_page);
    }



    private void init(){
        serverConnection = new ServerConnection(this);
        getStudentInfo();
        setButtons();
    }

    private void getStudentInfo(){
        SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(R.string.sharedPrefName),MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "No User");
        serverConnection.getTeacherByToken(token, new ServerConnection.JSONObjectResponseListener() {
            @Override
            public void onError(String message) {
                Log.e("Get Student By Token","Error, "+message);
            }
            @Override
            public void onResponse(JSONObject response) {
                teacher = Teacher.convertJSONObjectToTeacher(response);
                setEditTexts();
                setTextView();
                setSpinner();
            }
        });
    }

    private void setTextView(){
        usernameTextView = findViewById(R.id.studentSettingsUsername);
        usernameTextView.setText(teacher.getUsername());
    }
    private void setEditTexts(){
        fullNameEditText = findViewById(R.id.studentSettingsFullNameEdit);
        phoneEditText = findViewById(R.id.studentSettingsPhoneEdit);
        emailEditText = findViewById(R.id.studentSettingsEmailEdit);

        fullNameEditText.setHint(teacher.getFullName());
        phoneEditText.setHint(teacher.getPhoneNumber());
        emailEditText.setHint(teacher.getEmail());

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                email = emailEditText.getText().toString();
                //singUpButton.setEnabled(TeacherSignUp.this.checkLegalData());
                saveButton.setEnabled(TeacherSettingsPage.this.checkLegalData());
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
                saveButton.setEnabled(TeacherSettingsPage.this.checkLegalData());
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
                saveButton.setEnabled(TeacherSettingsPage.this.checkLegalData());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        priceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String a = priceEditText.getText().toString();
                if (!a.equals("")){
                    try{
                        price = Integer.parseInt(priceEditText.getText().toString());
                    }
                    catch (NumberFormatException exception){
                        Toast.makeText(TeacherSettingsPage.this,"Format Exception. Enter a number Fare",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    price =0;
                }
                saveButton.setEnabled(TeacherSettingsPage.this.checkLegalData());
            }
        });
    }

    private void setSpinner(){
        subjectSpinner = findViewById(R.id.teacherSubjectSpinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.subjects)
        );
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(arrayAdapter);
        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSubject = arrayAdapter.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //there is a default value
            }
        });
        subjectSpinner.setSelection(arrayAdapter.getPosition(teacher.getSubject()));
    }

    private void setButtons(){
        saveButton = findViewById(R.id.studentSettingsSave);
        backButton = findViewById(R.id.studentSettingsBack);
        saveButton.setEnabled(false);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverConnection.changeTeacherDetails(teacher.getToken(), fullName, phoneNumber, email, price, selectedSubject, new ServerConnection.StringResponseListener() {
                    @Override
                    public void onError(String message) {
                        Log.e("Change Student Details","Error, "+message);
                    }

                    @Override
                    public void onResponse(String response) {
                        if (response.equals("success")){
                            Toast.makeText(TeacherSettingsPage.this,"Your Details has been changed!",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(TeacherSettingsPage.this,MainPageTeacher.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(TeacherSettingsPage.this,"Price Problem. Pick legal Price - Number not smaller then 0",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherSettingsPage.this,MainPageTeacher.class);
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