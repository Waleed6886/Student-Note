package com.example.thiqah.studentnotes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thiqah.studentnotes.Model.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Thiqah on 3/29/2018.
 */

public class EditCourseAdapter extends RecyclerView.Adapter<EditCourseAdapter.EditCourseViewHolder> {

    private List<Course> courseList = new ArrayList<>();

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
//            holder.textViewTime.setText(Objects.requireNonNull(course.getCourseDateRealmList().get(0)).getTime());
//            holder.textViewDate.setText(Objects.requireNonNull(course.getCourseDateRealmList().get(0)).getDate());

        }
    }

    @Override
    public int getItemCount() {
        return this.courseList.size();
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
        this.courseList.clear();
        this.courseList.add(course);
        notifyDataSetChanged();
    }


}
