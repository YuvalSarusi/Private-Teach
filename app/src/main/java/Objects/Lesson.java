package Objects;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Lesson {

    private int id;
    private Date startDate;
    private Date endDate;
    private Teacher teacher;
    private Student student;

    public Lesson(int id, Date startDate, Date endDate, Teacher teacher, Student student) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.teacher = teacher;
        this.student = student;
    }

    public Lesson(Date startDate, Date endDate, Teacher teacher, Student student) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.teacher = teacher;
        this.student = student;
    }

    public Lesson(Date startDate, Date endDate, Teacher teacher) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.teacher = teacher;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public static Lesson convertJSONObjectToLesson(JSONObject jsonObject){
        Lesson lesson = null;
        try {
            //get String start & end date
            String startDateString = jsonObject.getString("startDateString");
            String endDateString = jsonObject.getString("endDateString");
            //create date format to insert Date object to Lesson object
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Student student;
            //check if the student value at the JSONObject is null or not. can't access to null value at json
            if (jsonObject.isNull("student")) {
                //if the value is null, student defined to be null
                student = null;
            } else {
                student = Student.convertJSONObjectToStudent(jsonObject.getJSONObject("student"));
            }
            //create Lesson object with the json values
            lesson = new Lesson(
                    Integer.parseInt(jsonObject.getString("id")),
                    simpleDateFormat.parse(startDateString),
                    simpleDateFormat.parse(endDateString),
                    Teacher.convertJSONObjectToTeacher(jsonObject.getJSONObject("teacher")),
                    student
            );
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return lesson;
    }
}
