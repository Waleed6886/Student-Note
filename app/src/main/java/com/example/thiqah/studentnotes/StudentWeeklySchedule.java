package com.example.thiqah.studentnotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.thiqah.studentnotes.Model.Course;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.realm.Realm;

public class StudentWeeklySchedule extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    CourseAdapter courseAdapter;

    private Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_weekly_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentWeeklySchedule.this,CRUDCourse.class);
                startActivity(intent);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        realm = Realm.getDefaultInstance(); // opens "myrealm.realm"


//        //initialize layout manager
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        //initialize adapter
//        recyclerView.setAdapter(new CourseAdapter());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();

    }

}
