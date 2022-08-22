package com.example.privateteach;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import Objects.ServerConnection;

public class AddLesson extends AppCompatActivity {

    private String teacherToken;

    private Button saveLesson;
    private Button selectDateButton;
    private Button selectStartTimeButton;
    private Button selectEndTimeButton;
    private EditText selectDateEditText;
    private EditText selectStartTimeEditText;
    private EditText selectEndTimeEditText;

    private int mYear,mMonth,mDay, mStartHour, mStartMinute, mEndHour, mEndMinute;

    private Date startDate;
    private Date endDate;
    private String dateString;
    private String startTimeString;
    private String endTimeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lesson);
        init();
    }

    private void init(){
        SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(R.string.sharedPrefName),MODE_PRIVATE);
        teacherToken = sharedPreferences.getString("token","No User");

        selectDateEditText = findViewById(R.id.selectDateEditText);
        selectStartTimeEditText = findViewById(R.id.selectStartTimeEditText);
        selectEndTimeEditText = findViewById(R.id.selectEndTimeEditText);
        selectDateButton = findViewById(R.id.selectDateButton);
        selectStartTimeButton = findViewById(R.id.selectStartTimeButton);
        selectEndTimeButton = findViewById(R.id.selectEndTimeButton);

        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddLesson.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dateString = year+"-"+(month+1)+"-"+dayOfMonth;
                                selectDateEditText.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                            }
                        },mYear,mMonth,mDay);
                datePickerDialog.show();
            }
        });
        selectStartTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                mStartHour = calendar.get(Calendar.HOUR);
                mStartMinute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddLesson.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                startTimeString = hourOfDay+":"+minute;
                                selectStartTimeEditText.setText(hourOfDay+":"+minute);
                            }
                        }
                        , mStartHour, mStartMinute, false);
                timePickerDialog.show();
            }
        });
        selectEndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                mEndHour = calendar.get(Calendar.HOUR);
                mEndMinute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddLesson.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                endTimeString = hourOfDay+":"+minute;
                                selectEndTimeEditText.setText(hourOfDay+":"+minute);
                            }
                        }
                        , mStartHour, mStartMinute, false);
                timePickerDialog.show();
            }
        });
        saveLesson = findViewById(R.id.saveLesson);
        saveLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateString+" "+ startTimeString);
                    endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateString+" "+ endTimeString);
                    Log.i("Date Example", startDate.toString());
                } catch (ParseException e) {
                    Log.e("Lesson Date","Format Exception");
                    e.printStackTrace();
                }
                if (startDate.before(endDate)){
                    ServerConnection serverConnection = new ServerConnection(AddLesson.this);
                    serverConnection.addLesson(startDate, endDate, teacherToken, "", new ServerConnection.StringResponseListener() {
                        @Override
                        public void onError(String message) {
                            Log.e("Save Lesson", message);
                        }

                        @Override
                        public void onResponse(String response) {
                            Log.i("Save Lesson", "Response from server: "+response);
                        }
                    });
                }
                else{
                    Toast.makeText(AddLesson.this,"Choose End Time after Start time",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}