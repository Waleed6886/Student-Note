package com.example.thiqah.studentnotes.Model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Thiqah on 3/27/2018.
 */

public class Course extends RealmObject implements Parcelable {

    @PrimaryKey
    private int primaryKey;
    private String courseName;
    private RealmList<CourseDate> courseDateRealmList;

    public Course() {
    }

    protected Course(Parcel in) {
        primaryKey = in.readInt();
        courseName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(primaryKey);
        dest.writeString(courseName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    public int getPrimaryKey() {
        return primaryKey;
    }

    public String getCourseName() {
        return courseName;
    }

    public RealmList<CourseDate> getCourseDateRealmList() {
        return courseDateRealmList;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseDateRealmList(RealmList<CourseDate> courseDateRealmList) {
        this.courseDateRealmList = courseDateRealmList;
    }
}
