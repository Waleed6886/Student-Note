package com.example.thiqah.studentnotes;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thiqah.studentnotes.Model.Course;
import com.example.thiqah.studentnotes.Model.CourseDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.realm.RealmList;

import static android.support.constraint.Constraints.TAG;

/**
 * Created by Thiqah on 3/29/2018.
 */

public class EditCourseAdapter extends RecyclerView.Adapter<EditCourseAdapter.EditCourseViewHolder> {

    private List<Course> courseList = new ArrayList<>();
    private RealmList<CourseDate> results = new RealmList<CourseDate>();

    EditCourseAdapter() {
    }

    @Override
    public EditCourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EditCourseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_course_item, parent, false));
    }

    @Override
    public void onBindViewHolder(EditCourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        if (course != null) {
            holder.textViewName.setText(course.getCourseName());
            if (course.getCourseDateRealmList() != null) {
                holder.textViewTime.setText(Objects.requireNonNull(course.getCourseDateRealmList().get(position)).getTime());
                holder.textViewDate.setText(Objects.requireNonNull(course.getCourseDateRealmList().get(position)).getDate());
            }
        }
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    class EditCourseViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewDate;
        private TextView textViewTime;

        EditCourseViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewEditCourseName);
            textViewDate = itemView.findViewById(R.id.textViewEditCourseDate);
            textViewTime = itemView.findViewById(R.id.textViewEditCourseTime);
        }
    }

    public void update(Course course) {
        Log.e(TAG, "getItemCount: "+ course.getCourseDateRealmList());

        this.courseList.clear();
        this.courseList.add(course);
//        this.results.addAll(course.getCourseDateRealmList());
        notifyDataSetChanged();
    }


}
