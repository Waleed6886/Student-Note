package com.example.thiqah.studentnotes;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.codetroopers.betterpickers.recurrencepicker.EventRecurrence;
import com.codetroopers.betterpickers.recurrencepicker.EventRecurrenceFormatter;
import com.codetroopers.betterpickers.recurrencepicker.RecurrencePickerDialogFragment;
import com.example.thiqah.studentnotes.Model.Course;
import com.example.thiqah.studentnotes.Model.CourseHours;

import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class CRUDCourse extends AppCompatActivity implements RecurrencePickerDialogFragment.OnRecurrenceSetListener {
    // TODO: 4/8/2018 Use butter knife
    Realm realm;
    @BindView(R.id.editTextCourseName)
    EditText editTextCourseName;
    LinearLayout buttonPickStartTime;
    @BindView(R.id.buttonPickEndTimeAndDate)
    LinearLayout buttonPickEndTime;
    @BindView(R.id.textViewStartDateAndTime)
    TextView textViewStartDateAndTime;
    @BindView(R.id.textViewEndDateAndTime)
    TextView textViewEndDateAndTime;
    Calendar calendar = Calendar.getInstance();


    int minute;
    int hour;
    int day;
    String month;
    int year;
    int minuteStart, hourStart, dayStart, monthStart, yearStart;
    int minuteFinal, hourFinal, dayFinal, monthFinal, yearFinal;

    //better picker
    private String mRrule;
    private static final String FRAG_TAG_RECUR_PICKER = "recurrencePickerDialogFragment";
    private EventRecurrence mEventRecurrence = new EventRecurrence();
    private TextView mResultTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crudcourse);
        ButterKnife.bind(this);
        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //FAB Button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_to_database(editTextCourseName.getText().toString().trim());
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        realm = Realm.getDefaultInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        String newMonth = month + " " + day + " " + hour + ":" + minute;
//        textViewStartDateAndTime.setText(newMonth);
        Log.v("time", newMonth);

        mResultTextView = findViewById(R.id.text);
        buttonPickStartTime = (LinearLayout) findViewById(R.id.buttonPickStartTimeAndDate);
        buttonPickStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimeAndDate();
            }
        });


    }

    @OnClick(R.id.buttonPickEndTimeAndDate)
    public void endTimeAndDate() {

    }

    public void startTimeAndDate() {
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
        rpd.setOnRecurrenceSetListener(CRUDCourse.this);
        rpd.show(fm, FRAG_TAG_RECUR_PICKER);


    }

    private void save_to_database(final String courseName) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Course course = bgRealm.createObject(Course.class);
                course.setCourseName(courseName);
                CourseHours courseHours = bgRealm.createObject(CourseHours.class);
                courseHours.setDay("sunday");
                courseHours.setHour(22);
                courseHours.setMinute(40);
                courseHours.setCourse(course);

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
            }
        });

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
        String repeatString = "";
        boolean enabled;
        if (!TextUtils.isEmpty(mRrule)) {
            repeatString = EventRecurrenceFormatter.getRepeatString(this, r, mEventRecurrence, true);
        }

        mResultTextView.setText(mRrule + "\n" + repeatString);
    }
}
