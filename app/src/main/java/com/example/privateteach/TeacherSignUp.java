package com.example.privateteach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.autofill.RegexValidator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Objects.ServerConnection;
import Objects.Teacher;
import utils.Utils;

public class TeacherSignUp extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText fullNameEditText;
    private EditText phoneNumberEditText;
    private EditText emailEditText;
    private EditText priceEditText;
    private Spinner subjectSpinner;
    private Button singUpButton;

    private String username;
    private String password;
    private String fullName;
    private String phoneNumber;
    private String email;
    private int price;
    private String selectedSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_sign_up);
        init();
    }

    private void init(){
        setButton();
        setEditTexts();
        setSpinner();
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
    }
    private void setEditTexts(){
        usernameEditText = findViewById(R.id.teacherSignUpUsername);
        passwordEditText = findViewById(R.id.teacherSignUpPassword);
        fullNameEditText = findViewById(R.id.teacherSignUpFullName);
        phoneNumberEditText = findViewById(R.id.teacherSignUpPhone);
        emailEditText = findViewById(R.id.teacherSignUpEmail);
        priceEditText = findViewById(R.id.teacherSignUpPrice);

        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                username = usernameEditText.getText().toString();
                singUpButton.setEnabled(TeacherSignUp.this.checkLegalData());

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
                singUpButton.setEnabled(TeacherSignUp.this.checkLegalData());
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
                singUpButton.setEnabled(TeacherSignUp.this.checkLegalData());
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
                singUpButton.setEnabled(TeacherSignUp.this.checkLegalData());
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
                singUpButton.setEnabled(TeacherSignUp.this.checkLegalData());
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
                        Toast.makeText(TeacherSignUp.this,"Format Exception. Enter a number Fare",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    price =0;
                }
                singUpButton.setEnabled(TeacherSignUp.this.checkLegalData());
            }
        });
    }
    private void setButton() {
        singUpButton = findViewById(R.id.teacherSingUpButton);
        singUpButton.setEnabled(false);
        singUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create new teacher when click on the button
                String token = Utils.createHash(username,password);
                Teacher teacher = new Teacher(username,password,token,fullName,phoneNumber,email,price,selectedSubject);
                ServerConnection serverConnection = new ServerConnection(TeacherSignUp.this);
                serverConnection.createTeacher(teacher, new ServerConnection.StringResponseListener() {
                    @Override
                    public void onError(String message) {
                        Log.e("Create Teacher","failed, "+message);
                    }

                    @Override
                    public void onResponse(String response) {
                        if (response.equals("formatProblem")){
                            Toast.makeText(TeacherSignUp.this,"Format Exception. Enter a number Fare",Toast.LENGTH_LONG).show();
                        }
                        else{
                            if (response.equals("usernameExist")){
                                Toast.makeText(TeacherSignUp.this,"Username Already Exist. Choose another one",Toast.LENGTH_LONG).show();
                                Log.e("Create Teacher",response);
                            }
                            else{
                                Log.i("Create Teacher", "succeeded, "+response);
                                SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(R.string.sharedPrefName),MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("token",response);
                                editor.commit();
                                Intent intent = new Intent(TeacherSignUp.this,MainPageTeacher.class);
                                startActivity(intent);
                            }
                        }
                    }
                });
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
                    !fullName.equals("") &&
                    price != 0
            ;
        }
        return answer;
    }


}