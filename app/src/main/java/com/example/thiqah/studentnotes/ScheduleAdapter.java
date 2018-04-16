package com.example.thiqah.studentnotes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thiqah.studentnotes.Model.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import io.realm.RealmResults;


public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.CourseViewHolder> {

    private List<Course> courseList = new ArrayList<>();

    ScheduleAdapter() {
    }



    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CourseViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.course_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.textViewCourseName.setText(course.getCourseName());
        holder.setOnClick(position);
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    class CourseViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewCourseName;

        CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCourseName = itemView.findViewById(R.id.textViewCourseName);
        }

        public void setOnClick(final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(),EditCourseActivity.class);
                    Bundle bundle = new Bundle();
                    Course course = courseList.get(position);
                    bundle.putParcelable(EditCourseActivity.COURSE_KEY, course);
                    intent.putExtras(bundle);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
    public void update(RealmResults<Course> course) {
        this.courseList.clear();
        this.courseList.addAll(course);
        notifyDataSetChanged();
    }
}