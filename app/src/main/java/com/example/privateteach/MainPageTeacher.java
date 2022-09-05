package com.example.privateteach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import Objects.Lesson;
import Objects.ServerConnection;
import Objects.Student;
import Objects.Teacher;
import Objects.TeacherLessonAdapter;

public class MainPageTeacher extends AppCompatActivity{

    private ServerConnection serverConnection;

    private Button addLesson;
    private TextView titleTextView;

    private ListView futureLessonList;
    private ListView pastLessonList;
    private List<Lesson> futureLessons;
    private List<Lesson> pastLessons;

    TeacherLessonAdapter pastLessonsAdapter;
    TeacherLessonAdapter futureLessonsAdapter;

    private String teacherToken;
    private String fullName;
    private JSONObject returnedTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_teacher);
        init();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.teacher_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.teacherMenuLogOut:
                //log out - delete from shared preference
                SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(R.string.sharedPrefName),MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
               break;
            case R.id.teacherMenuSettings:
                //go to settings
                intent = new Intent(this,TeacherSettingsPage.class);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void init(){
        returnedTeacher = new JSONObject();
        serverConnection = new ServerConnection(this);

        //get teacher token from preference
        SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(R.string.sharedPrefName),MODE_PRIVATE);
        teacherToken = sharedPreferences.getString("token","No User");

        addLesson = findViewById(R.id.teacherAddLesson);
        futureLessonList = findViewById(R.id.teacherFutureLessonList);
        pastLessonList = findViewById(R.id.teacherPastLessonList);
        titleTextView = findViewById(R.id.teacherHomeTitle);

        //get teacher information token from server by token
        returnedTeacher = serverConnection.getTeacherByToken(teacherToken, new ServerConnection.JSONObjectResponseListener() {
            @Override
            public void onError(String message) {
                Log.e("Get Teacher By Token","Error "+message);
            }
            @Override
            public void onResponse(JSONObject response) {
                returnedTeacher = response;
                try {
                    fullName = returnedTeacher.getString("fullName");
                    titleTextView.setText("Welcome, "+ fullName);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Get Teacher Name","Error");
                }
            }
        });
        //add listener to the button
        addLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPageTeacher.this, AddLesson.class);
                startActivity(intent);
            }
        });
        //get lessons data from server and put it in list
        getLessonsData();
    }
    private void getLessonsData(){
        pastLessons = new ArrayList<>();
        futureLessons = new ArrayList<>();
        serverConnection.getTeacherPastLessons(teacherToken, new ServerConnection.JSONArrayResponseListener() {
            @Override
            public void onError(String message) {
                Log.e("Get Past Lessons", "Error, "+message);
            }
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i<response.length(); i++){
                    try {
                        pastLessons.add(Lesson.convertJSONObjectToLesson(response.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //connect adapter to lists
                pastLessonsAdapter = new TeacherLessonAdapter(MainPageTeacher.this,pastLessons);
                pastLessonList.setAdapter(pastLessonsAdapter);
            }
        });
        serverConnection.getTeacherFutureLessons(teacherToken, new ServerConnection.JSONArrayResponseListener() {
            @Override
            public void onError(String message) {
                Log.e("Get Future Lessons", "Error, "+message);
            }
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i<response.length(); i++) {
                    try {
                        futureLessons.add(Lesson.convertJSONObjectToLesson(response.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //create new adapter to connect between the ArrayList and the listview
                futureLessonsAdapter = new TeacherLessonAdapter(MainPageTeacher.this,futureLessons);
                futureLessonList.setAdapter(futureLessonsAdapter);
            }
        });
    }

}