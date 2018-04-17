package com.example.thiqah.studentnotes;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codetroopers.betterpickers.recurrencepicker.EventRecurrence;
import com.codetroopers.betterpickers.recurrencepicker.EventRecurrenceFormatter;
import com.codetroopers.betterpickers.recurrencepicker.RecurrencePickerDialogFragment;
import com.example.thiqah.studentnotes.Model.Course;
import com.example.thiqah.studentnotes.Model.CourseDate;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

public class EditCourseActivity extends AppCompatActivity implements RecurrencePickerDialogFragment.OnRecurrenceSetListener {
    public static final String COURSE_KEY = "COURSE";
    // TODO: 4/8/2018 Use butter knife
    private RecyclerView recyclerView;
    private EditCourseAdapter editCourseAdapter;
    Realm realm;

    EditText editTextCourseName;

    LinearLayout buttonPickDate;
    LinearLayout buttonPickTime;

    TextView textViewDate;
    TextView textViewEndDateAndTime;
    Calendar calendar = Calendar.getInstance();


    int minute;
    int hour;
    int day;
    String month;
    int year;
    int minuteStart, hourStart, dayStart, monthStart, yearStart;
    int minuteFinal, hourFinal, dayFinal, monthFinal, yearFinal;
    String repeatString = "";
    String courseName;

    //better picker
    private String mRrule;
    private static final String FRAG_TAG_RECUR_PICKER = "recurrencePickerDialogFragment";
    private EventRecurrence mEventRecurrence = new EventRecurrence();
    private String TAG = "EditCourseActivity";

    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        ButterKnife.bind(this);
        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        realm = Realm.getDefaultInstance();
        initializeViews();
        initializeLayoutManager();
        initializeAdapter();
        initializeData();


        //FAB Button
        floatingButton();

        year = calendar.get(Calendar.YEAR);
        month = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        String newMonth = month + " " + day + " " + hour + ":" + minute;
//        textViewStartDateAndTime.setText(newMonth);
        Log.v("time", newMonth);


        buttonPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });


    }


    private void initializeViews() {
        buttonPickDate = findViewById(R.id.buttonPickDate);
        editTextCourseName = findViewById(R.id.editTextCourseName);
        recyclerView = findViewById(R.id.editRecyclerView);
    }

    private void initializeData() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            Course courseData = bundle.getParcelable(COURSE_KEY);
            editCourseAdapter.update(courseData);
        }
    }



    //initialize the adapter
    private void initializeAdapter() {
        editCourseAdapter = new EditCourseAdapter();
        recyclerView.setAdapter(editCourseAdapter);
    }


    public void endTimeAndDate() {

    }

    public void pickDate() {
        FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        Time time = new Time();
        time.setToNow();
        bundle.putLong(RecurrencePickerDialogFragment.BUNDLE_START_TIME_MILLIS, time.toMillis(false));
        bundle.putString(RecurrencePickerDialogFragment.BUNDLE_TIME_ZONE, time.timezone);

        // may be more efficient to serialize and pass in EventRecurrence
        bundle.putString(RecurrencePickerDialogFragment.BUNDLE_RRULE, mRrule);

        RecurrencePickerDialogFragment rpd = (RecurrencePickerDialogFragment) fm.findFragmentByTag(
                FRAG_TAG_RECUR_PICKER);
        if (rpd != null) {
            rpd.dismiss();
        }
        rpd = new RecurrencePickerDialogFragment();
        rpd.setArguments(bundle);
        rpd.setOnRecurrenceSetListener(EditCourseActivity.this);
        rpd.show(fm, FRAG_TAG_RECUR_PICKER);


    }

    private void save_to_database(final String courseName, final String repeatString) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Course course = bgRealm.createObject(Course.class);
                course.setCourseName(courseName);
                CourseDate courseDate = bgRealm.createObject(CourseDate.class);
                courseDate.setDate(repeatString);
                courseDate.setTime(repeatString);
                courseDate.setCourse(course);
                RealmList<CourseDate> list = course.getCourseDateRealmList();
                if (list == null) list = new RealmList<>();
                list.add(courseDate);
                course.setCourseDateRealmList(list);


            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
                Log.e(TAG, "execute: success");
                final RealmResults<Course> courseRealmResults = realm.where(Course.class).findAll();
                Log.e(TAG, "onSuccess: " + courseRealmResults.size());

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
            }
        });

    }

    //initialize Layout Manger for the adapter
    private void initializeLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onRecurrenceSet(String rrule) {
        mRrule = rrule;
        if (mRrule != null) {
            mEventRecurrence.parse(mRrule);
        }
        populateRepeats();
    }

    private void populateRepeats() {
        Resources r = getResources();

        boolean enabled;
        if (!TextUtils.isEmpty(mRrule)) {
            repeatString = EventRecurrenceFormatter.getRepeatString(this, r, mEventRecurrence, true);
            Log.e(TAG, "populateRepeats: "+repeatString);
        }

    }

    private void floatingButton() {
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle = getIntent().getExtras();
                if (bundle != null) {
                    Course courseData = bundle.getParcelable(COURSE_KEY);
                    edit_in_database(courseData);
                    editCourseAdapter.update(courseData);
                }
                courseName = editTextCourseName.getText().toString();
                if (!courseName.isEmpty() || !repeatString.isEmpty()) {
                    Course course = realm.where(Course.class).equalTo("courseName", courseName).findFirst();
                    if (((course != null ? course.getCourseName() : null) == courseName)) {
//                        edit_in_database(course, courseName);
                    } else {
                        save_to_database(courseName, repeatString);
                        Snackbar.make(view, "Replace with your own action ", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                } else {
                    Snackbar.make(view, "I need data (╯°□°）╯︵ ┻━┻", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    private void edit_in_database(Course course) {
        final Course updateCourse = realm.where(Course.class).equalTo("courseName", course.getCourseName()).findFirst();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                int courseDateSize = updateCourse.getCourseDateRealmList().size();
                CourseDate courseDate = realm.createObject(CourseDate.class);
                courseDate.setCourse(updateCourse);
                }
        });



    }


}

