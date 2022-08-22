package Objects;

import org.json.JSONObject;

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
}
