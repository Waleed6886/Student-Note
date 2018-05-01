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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.codetroopers.betterpickers.recurrencepicker.EventRecurrence;
import com.codetroopers.betterpickers.recurrencepicker.EventRecurrenceFormatter;
import com.codetroopers.betterpickers.recurrencepicker.RecurrencePickerDialogFragment;
import com.example.thiqah.studentnotes.Model.Course;
import com.example.thiqah.studentnotes.Model.CourseDate;

import java.util.Calendar;
import java.util.Locale;

import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class EditCourseActivity extends AppCompatActivity implements RecurrencePickerDialogFragment.OnRecurrenceSetListener, RadialTimePickerDialogFragment.OnTimeSetListener {
    public static final String COURSE_KEY = "COURSE";
    private RecyclerView recyclerView;
    private EditCourseAdapter editCourseAdapter;
    Realm realm;

    EditText editTextCourseName;

    ImageView buttonPickDate;
    ImageView buttonPickTime;


    Calendar calendar = Calendar.getInstance();


    int minute;
    int hour;
    String repetitionOfDays;
    String days;
    String month;
    int year;
    int minuteStart, hourStart, dayStart, monthStart, yearStart;
    int minuteFinal, hourFinal, dayFinal, monthFinal, yearFinal;
    String repeatString = "";
    String courseName;

    //better picker - date
    private String mRrule;
    private static final String FRAG_TAG_RECUR_PICKER = "recurrencePickerDialogFragment";
    private EventRecurrence mEventRecurrence = new EventRecurrence();
    private String TAG = "EditCourseActivity";

    //better picker - time
    private static final String FRAG_TAG_TIME_PICKER = "timePickerDialogFragment";

    Bundle bundle;
    private int day;


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
        buttonPickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickTime();
            }
        });


    }

    private void pickTime() {
        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(EditCourseActivity.this)
                .setThemeLight();
        rtpd.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER);
    }


    private void initializeViews() {
        buttonPickDate = findViewById(R.id.buttonPickDate);
        buttonPickTime = findViewById(R.id.buttonPickTime);
        editTextCourseName = findViewById(R.id.editTextCourseName);
        recyclerView = findViewById(R.id.editRecyclerView);
    }

    private void initializeData() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            Course courseData = bundle.getParcelable(COURSE_KEY);
            retrieve_from_database(courseData);
        }
    }

    private void retrieve_from_database(final Course courseData) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Course course = realm.where(Course.class).equalTo("courseName", courseData.getCourseName()).findFirst();
                editTextCourseName.setText(course.getCourseName());
                editCourseAdapter.update(course);
            }
        });
    }




    //initialize the adapter
    private void initializeAdapter() {
        editCourseAdapter = new EditCourseAdapter();
        recyclerView.setAdapter(editCourseAdapter);

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

    //initialize Layout Manger for the adapter
    private void initializeLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void floatingButton() {
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_if_empty()) {
                    bundle = getIntent().getExtras();
                    if (bundle != null) {
                        Course bundleCourse = bundle.getParcelable(COURSE_KEY);
                        Course course = realm.where(Course.class).equalTo("courseName", bundleCourse.getCourseName()).findFirst();
                        edit_in_database(course);
                        editCourseAdapter.update(course);
                    } else {
                        courseName = editTextCourseName.getText().toString();
                        Course course = realm.where(Course.class).equalTo("courseName", courseName).findFirst();

                        if (course != null && course.getCourseName().equals(courseName)) {
                            edit_in_database(course);
                        } else {
                            save_to_database(courseName, hour, minute);
                        }
                    }
                    Course retrieveCourse = realm.where(Course.class).equalTo("courseName", courseName).findFirst();
                    if (retrieveCourse != null) {
                        editCourseAdapter.update(retrieveCourse);
                    }
                }
            }
        });
    }

    private void save_to_database(final String courseName, final int hour, final int minute) {
        //to get a PK for the new object
        final RealmResults<Course> courseList = realm.where(Course.class).findAll();
        final int pk = courseList.size() + 1;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CourseDate courseDate = realm.createObject(CourseDate.class);
                courseDate.setDate(days);
                courseDate.setTime(String.valueOf(hour + ":" + minute));

                Course course = realm.createObject(Course.class, pk);
                course.setCourseName(courseName);
                course.getCourseDateRealmList().add(courseDate);
            }
        });
    }

    private void edit_in_database(final Course course) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CourseDate courseDate = realm.createObject(CourseDate.class);
                courseDate.setDate(days);
                courseDate.setTime(String.valueOf(hour + ":" + minute));

                course.getCourseDateRealmList().add(courseDate);

                Log.e(TAG, "execute: edit");
            }
        });
    }

    private boolean check_if_empty() {
        String courseName = editTextCourseName.getText().toString();
        View view = findViewById(R.id.activity_main);


        boolean cancel = false;
        View focusView = null;

        // Check for a course name is empty or not
        if (TextUtils.isEmpty(courseName)) {
            editTextCourseName.setError(getString(R.string.error_field_required));
            focusView = editTextCourseName;
            cancel = true;
        }
        if (days == null) {

            Snackbar.make(view, "you need to pick a date ", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else if (hour == 0) {
            Snackbar.make(view, "you need to pick a time ", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        if (cancel) {
            focusView.requestFocus();
        } else if (days != null && hour != 0 && cancel == false) {

            return true;

        }
        return false;
    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int min) {
        hour = hourOfDay;
        minute = min;
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
            //it return the index number of the last equal sign, REQ=WEEKLY;WKST=SU;BYDAY=TH,SA
            int indexNumOfDays = mRrule.lastIndexOf('=');
            //it return the first index where it find the equal sign
            int indexNumOfRepetition = mRrule.indexOf('=');
            int indexNumOfSemicolon = mRrule.indexOf(';');

            //the output
            days = mRrule.substring(indexNumOfDays + 1);
            repetitionOfDays = mRrule.substring(indexNumOfRepetition + 1, indexNumOfSemicolon);
            repeatString = EventRecurrenceFormatter.getRepeatString(this, r, mEventRecurrence, true);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}

