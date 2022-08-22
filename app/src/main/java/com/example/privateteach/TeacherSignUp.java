package com.example.privateteach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

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
        usernameEditText = findViewById(R.id.teacherSignUpUsername);
        passwordEditText = findViewById(R.id.teacherSignUpPassword);
        fullNameEditText = findViewById(R.id.teacherSignUpFullName);
        phoneNumberEditText = findViewById(R.id.teacherSignUpPhone);
        emailEditText = findViewById(R.id.teacherSignUpEmail);
        priceEditText = findViewById(R.id.teacherSignUpPrice);

        subjectSpinner = findViewById(R.id.teacherSubjectSpinner);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(
                this,R.array.subjects, android.R.layout.simple_spinner_item
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

        singUpButton = findViewById(R.id.teacherSingUpButton);
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
                try{
                    price = Integer.parseInt(priceEditText.getText().toString());
                }
                catch (NumberFormatException exception){
                    Toast.makeText(TeacherSignUp.this,"Format Exception. Enter a number Fare",Toast.LENGTH_LONG).show();
                }
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
}