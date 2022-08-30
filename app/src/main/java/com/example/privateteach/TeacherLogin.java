package com.example.privateteach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.chromium.net.CronetEngine;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Objects.MySingleton;
import Objects.ServerConnection;
import utils.Utils;

public class TeacherLogin extends AppCompatActivity {

    private TextView signUp;
    private Button login;
    private EditText usernameEditText;
    private EditText passwordEditText;

    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);
        init();
    }
    private void init(){
        setLogin();
        setEditTexts();
        signUp = findViewById(R.id.teacherSignUpText);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //move to sign up page
                Intent intent = new Intent(TeacherLogin.this,TeacherSignUp.class);
                startActivity(intent);
            }
        });

    }

    private void setLogin(){
        login = findViewById(R.id.teacherLoginButton);
        login.setEnabled(false);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //login
                String token = Utils.createHash(username,password);
                ServerConnection serverConnection = new ServerConnection(TeacherLogin.this);
                serverConnection.checkTeacherExist(username, token,
                        new ServerConnection.StringResponseListener() {
                            @Override
                            public void onError(String message) {
                                Log.e("Teacher Login",message);
                            }

                            @Override
                            public void onResponse(String response) {
                                if (response.equals("usernameDoesn'tExist")){
                                    Toast.makeText(TeacherLogin.this,"Teacher Doesn't Exist",Toast.LENGTH_LONG).show();
                                }
                                else if (response.equals("passwordWrong")){
                                    Toast.makeText(TeacherLogin.this,"Wrong password. try again",Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Intent intent = new Intent(TeacherLogin.this, MainPageTeacher.class);
                                    Log.i("Teacher Login", "success, "+response);
                                    SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(R.string.sharedPrefName),MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("token",response);
                                    editor.putString("type","teacher");
                                    editor.commit();
                                    startActivity(intent);
                                }
                            }
                        }

                );
            }
        });
    }
    private void setEditTexts(){
        usernameEditText = findViewById(R.id.teacherUsernameLogin);
        passwordEditText = findViewById(R.id.teacherPasswordLogin);

        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                username = usernameEditText.getText().toString();
                login.setEnabled(TeacherLogin.this.checkLegalData());

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
                login.setEnabled(TeacherLogin.this.checkLegalData());
            }

            @Override
            public void afterTextChanged(Editable s) {

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