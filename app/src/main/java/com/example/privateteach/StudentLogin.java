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

import Objects.ServerConnection;
import utils.Utils;

public class StudentLogin extends AppCompatActivity {

    private TextView signUp;
    private Button login;
    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        init();
    }
    private void init(){
        usernameEditText = findViewById(R.id.studentUsernameLogin);
        passwordEditText = findViewById(R.id.studentPasswordLogin);

        signUp = findViewById(R.id.studentSignUpText);
        login = findViewById(R.id.studentLoginButton);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //move to sign up page
                Intent intent = new Intent(StudentLogin.this, StudentSignUp.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //login
                TextView textView = findViewById(R.id.loginTitle);
                //textView.setText("response");
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
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
                                    Intent intent = new Intent(StudentLogin.this, MainPageTeacher.class);
                                    Log.i("Student Login", "success, "+response);
                                    SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(R.string.sharedPrefName),MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("token",response);
                                    editor.commit();
                                    startActivity(intent);
                                }
                            }
                        }

                );
            }
        });
    }
}