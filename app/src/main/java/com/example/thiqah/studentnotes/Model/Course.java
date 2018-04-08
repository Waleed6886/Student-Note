package com.example.thiqah.studentnotes.Model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Thiqah on 3/27/2018.
 */

public class Course extends RealmObject {

    @PrimaryKey
    long id;
    @Required
    String courseName;



    public long getId() {
        return id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
