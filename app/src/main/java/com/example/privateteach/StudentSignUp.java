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
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Objects.ServerConnection;
import Objects.Student;
import Objects.Teacher;
import utils.Utils;

public class StudentSignUp extends AppCompatActivity {


    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText fullNameEditText;
    private EditText phoneNumberEditText;
    private EditText emailEditText;
    private Button singUpButton;

    private String username;
    private String password;
    private String fullName;
    private String phoneNumber;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_sign_up);
        init();
    }


    private void init(){
        setButton();
        setEditTexts();

    }
    private void setButton(){
        singUpButton = findViewById(R.id.studentSingUpButton);
        singUpButton.setEnabled(false);
        singUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create new teacher when click on the button
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                fullName = fullNameEditText.getText().toString();
                phoneNumber = phoneNumberEditText.getText().toString();
                email = emailEditText.getText().toString();
                String token = Utils.createHash(username,password);
                Student student = new Student(username,token,fullName,phoneNumber,email);
                ServerConnection serverConnection = new ServerConnection(StudentSignUp.this);
                serverConnection.createStudent(student, new ServerConnection.StringResponseListener() {
                    @Override
                    public void onError(String message) {
                        Log.e("Create Teacher","failed, "+message);
                    }

                    @Override
                    public void onResponse(String response) {
                        if (response.equals("formatProblem")){
                            Toast.makeText(StudentSignUp.this,"Format Exception. Enter a number Fare",Toast.LENGTH_LONG).show();
                        }
                        else{
                            if (response.equals("usernameExist")){
                                Toast.makeText(StudentSignUp.this,"Username Already Exist. Choose another one",Toast.LENGTH_LONG).show();
                                Log.e("Create Teacher",response);
                            }
                            else{
                                Log.i("Create Teacher", "succeeded, "+response);
                                SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(R.string.sharedPrefName),MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("token",response);
                                editor.commit();
                                Intent intent = new Intent(StudentSignUp.this,MainPageStudent.class);
                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        });
    }
    private void setEditTexts(){
        usernameEditText = findViewById(R.id.studentSignUpUsername);
        passwordEditText = findViewById(R.id.studentSignUpPassword);
        fullNameEditText = findViewById(R.id.studentSignUpFullName);
        phoneNumberEditText = findViewById(R.id.studentSignUpPhone);
        emailEditText = findViewById(R.id.studentSignUpEmail);

        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                username = usernameEditText.getText().toString();
                singUpButton.setEnabled(StudentSignUp.this.checkLegalData());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password = passwordEditText.getText().toString();
                singUpButton.setEnabled(StudentSignUp.this.checkLegalData());
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
                singUpButton.setEnabled(StudentSignUp.this.checkLegalData());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        phoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phoneNumber = phoneNumberEditText.getText().toString();
                singUpButton.setEnabled(StudentSignUp.this.checkLegalData());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                email = emailEditText.getText().toString();
                //singUpButton.setEnabled(TeacherSignUp.this.checkLegalData());
                singUpButton.setEnabled(StudentSignUp.this.checkLegalData());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean checkLegalData(){
        boolean answer = false;
        if (phoneNumber != null && email != null && username != null && password != null && fullName != null){
            Pattern phoneNumberPattern = Pattern.compile("^05\\d([-]?)\\d{7}$");
            Pattern emailPattern = Pattern.compile("^(.+)@(.+)$");
            Matcher phoneMatcher = phoneNumberPattern.matcher(phoneNumber);
            Matcher emailMatcher = emailPattern.matcher(email);
            answer = emailMatcher.matches() &&
                    phoneMatcher.matches() &&
                    !username.equals("")&&
                    !password.equals("")&&
                    !fullName.equals("")
            ;
        }
        return answer;
    }

}