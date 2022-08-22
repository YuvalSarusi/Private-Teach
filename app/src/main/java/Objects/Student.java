package Objects;

import org.json.JSONException;
import org.json.JSONObject;

public class Student {

    private int id;
    private String username;
    private String password;
    private String token;
    private String fullName;
    private String phoneNumber;
    private String email;

    public Student(int id, String username, String password, String token, String fullName, String phoneNumber, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.token = token;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Student(String username, String token, String fullName, String phoneNumber, String email) {
        this.id = 0;
        this.username = username;
        this.password = null;
        this.token = token;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Student(String username, String password, String token, String fullName, String phoneNumber, String email) {
        this.username = username;
        this.password = password;
        this.token = token;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static Student convertJSONObjectToStudent(JSONObject jsonObject){
        Student student = null;
        try {
            student = new Student(
                    jsonObject.getString("username"),
                    jsonObject.getString("token"),
                    jsonObject.getString("fullName"),
                    jsonObject.getString("phoneNumber"),
                    jsonObject.getString("email")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return student;
    }
}
