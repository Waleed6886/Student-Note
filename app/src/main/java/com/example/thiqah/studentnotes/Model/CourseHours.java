package com.example.thiqah.studentnotes.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Thiqah on 3/29/2018.
 */

public class CourseHours extends RealmObject {

    @PrimaryKey
    long id;
    String day;
    int hour;
    int minute;
    Course course;


    public long getId() {
        return id;
    }

    public String getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public Course getCourse() {
        return course;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
