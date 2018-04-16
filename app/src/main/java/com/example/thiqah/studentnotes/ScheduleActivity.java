package com.example.thiqah.studentnotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.thiqah.studentnotes.Model.Course;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ScheduleActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ScheduleAdapter scheduleAdapter;

    private Realm realm;

    SwipeRefreshLayout mySwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        realm = Realm.getDefaultInstance(); // opens "myrealm.realm"

        setViews();
        initializeLayoutManager();
        initializeAdapter();
        initializeData();
        swipeRefresh();

        }

    //initialize Layout Manger for the adapter
    private void initializeLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    //initialize the adapter
    private void initializeAdapter() {
        scheduleAdapter = new ScheduleAdapter();
        recyclerView.setAdapter(scheduleAdapter);

    }

    private void initializeData() {
        final RealmResults<Course> courses = realm.where(Course.class).findAll();
        scheduleAdapter.update(courses);
        courses.addChangeListener(new RealmChangeListener<RealmResults<Course>>() {
            @Override
            public void onChange(@NonNull RealmResults<Course> courses) {
                scheduleAdapter.update(courses);
            }
        });
    }

    private void setViews() {
        recyclerView = findViewById(R.id.recycler_view);
        mySwipeRefreshLayout = findViewById(R.id.swiperefresh);
        setToolbar();
        setFloatingButton();
    }

    private void setFloatingButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScheduleActivity.this,EditCourseActivity.class);
                startActivity(intent);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_schedule);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.delete_item) {

            delete_database();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void delete_database() {
        final RealmResults<Course> results = realm.where(Course.class).findAll();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Delete everything
                results.deleteAllFromRealm();
            }
        });
    }
    private void swipeRefresh() {

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        myUpdateOperation();
                    }
                }
        );
    }
    private void myUpdateOperation() {
        //Reload the data
        final RealmResults<Course> courses = realm.where(Course.class).findAll();
        scheduleAdapter.update(courses);
        mySwipeRefreshLayout.setRefreshing(false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();

    }

}
