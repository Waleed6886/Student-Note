package com.example.thiqah.studentnotes.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Thiqah on 3/29/2018.
 */

public class CourseDate extends RealmObject {

    String date;
    String time;

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

}



