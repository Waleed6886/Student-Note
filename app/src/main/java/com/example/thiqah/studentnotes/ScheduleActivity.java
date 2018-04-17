package com.example.thiqah.studentnotes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.thiqah.studentnotes.Model.Course;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ScheduleActivity extends AppCompatActivity {

    EmptyRecyclerView recyclerView;
    ScheduleAdapter scheduleAdapter;
    private Handler mHandler;
    private final static int DELAY = 3000;

    private Realm realm;
    SwipeRefreshLayout mySwipeRefreshLayout;
    ConstraintLayout emptyView;

    private Runnable updateAdapterRunnable = new Runnable() {
        @Override
        public void run() {
            myUpdateOperation();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        realm = Realm.getDefaultInstance(); // opens "myrealm.realm"

        initializeViews();
        initializeAdapter();
        initializeData();
        }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(updateAdapterRunnable, DELAY);
        myUpdateOperation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(updateAdapterRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();

    }
    //initialize Views
    private void initializeViews() {
        recyclerView = findViewById(R.id.recycler_view);
        emptyView = findViewById(R.id.empty_view);
        mySwipeRefreshLayout = findViewById(R.id.swiperefresh);
        setToolbar();
        setFloatingButton();
    }
    //initialize the adapter
    private void initializeAdapter() {
        scheduleAdapter = new ScheduleAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setEmptyView(findViewById(R.id.empty_view));
        recyclerView.setAdapter(scheduleAdapter);

        mHandler = new Handler();
    }
    //initialize data
    private void initializeData() {
        myUpdateOperation();
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
        courses.addChangeListener(new RealmChangeListener<RealmResults<Course>>() {
            @Override
            public void onChange(@NonNull RealmResults<Course> courses) {
                scheduleAdapter.update(courses);
            }
        });
        mySwipeRefreshLayout.setRefreshing(false);

    }
}
