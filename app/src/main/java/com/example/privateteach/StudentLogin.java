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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Objects.ServerConnection;
import utils.Utils;

public class StudentLogin extends AppCompatActivity {

    private TextView signUp;
    private Button login;
    private EditText usernameEditText;
    private EditText passwordEditText;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        init();
    }
    private void init(){
        setLogin();
        setEditTexts();
        signUp = findViewById(R.id.studentSignUpText);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //move to sign up page
                Intent intent = new Intent(StudentLogin.this, StudentSignUp.class);
                startActivity(intent);
            }
        });
    }

    private void setEditTexts(){
        usernameEditText = findViewById(R.id.studentUsernameLogin);
        passwordEditText = findViewById(R.id.studentPasswordLogin);

        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                username = usernameEditText.getText().toString();
                login.setEnabled(StudentLogin.this.checkLegalData());

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
                login.setEnabled(StudentLogin.this.checkLegalData());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void setLogin(){
        login = findViewById(R.id.studentLoginButton);
        login.setEnabled(false);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //login
                TextView textView = findViewById(R.id.loginTitle);
                String token = Utils.createHash(username,password);
                ServerConnection serverConnection = new ServerConnection(StudentLogin.this);
                serverConnection.checkStudentExist(username, token,
                        new ServerConnection.StringResponseListener() {
                            @Override
                            public void onError(String message) {
                                Log.e("Student Login",message);
                            }

                            @Override
                            public void onResponse(String response) {
                                if (response.equals("usernameDoesn'tExist")){
                                    Toast.makeText(StudentLogin.this,"Student Doesn't Exist",Toast.LENGTH_LONG).show();
                                }
                                else if (response.equals("passwordWrong")){
                                    Toast.makeText(StudentLogin.this,"Wrong password. try again",Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Intent intent = new Intent(StudentLogin.this, MainPageStudent.class);
                                    Log.i("Student Login", "success, "+response);
                                    SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(R.string.sharedPrefName),MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("token",response);
                                    editor.putString("type","student");
                                    editor.commit();
                                    startActivity(intent);
                                }
                            }
                        }

                );
            }
        });
    }

    private boolean checkLegalData(){
        boolean answer = false;
        if (username != null && password != null){
            answer = !username.equals("") && !password.equals("");
        }
        return answer;
    }
}