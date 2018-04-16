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

import io.realm.RealmResults;

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
            holder.textViewEditName.setText(course.getCourseName());
            for (int i = 0; i < course.getCourseDateRealmList().size(); i++) {
                holder.textViewEditTime.setText(Objects.requireNonNull(course.getCourseDateRealmList().get(i)).getTime());
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.courseList.size();
    }

    class EditCourseViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewEditName;
        //        @BindView(R.id.textViewEditDate)
//        TextView textViewEditDate;
        private TextView textViewEditTime;

        EditCourseViewHolder(View itemView) {
            super(itemView);
            textViewEditName = itemView.findViewById(R.id.textViewEditName);
            textViewEditTime = itemView.findViewById(R.id.textViewEditTime);
        }
    }

    public void update(RealmResults<Course> course) {
        this.courseList.clear();
        this.courseList.addAll(course);
        notifyDataSetChanged();
    }


}
