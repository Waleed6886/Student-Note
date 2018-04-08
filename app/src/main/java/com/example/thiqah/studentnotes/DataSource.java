package com.example.thiqah.studentnotes;


public interface DataSource {

    void passEndDate(int year, int month, int day, int hour, int minute);

    void passStartDate(int year, int month, int day, int hour, int minute);

}
