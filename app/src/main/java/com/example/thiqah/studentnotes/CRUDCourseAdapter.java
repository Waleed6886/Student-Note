package com.example.thiqah.studentnotes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.thiqah.studentnotes.Model.Course;

import java.util.List;

/**
 * Created by Thiqah on 3/29/2018.
 */

public class CRUDCourseAdapter extends RecyclerView.Adapter<CRUDCourseAdapter.CRUDViewHolder>{

    private Context context;
    private List<Course> crudCourseList;

    public CRUDCourseAdapter() {
    }
    public CRUDCourseAdapter(Context context, List<Course> crudCourseList) {
        this.context = context;
        this.crudCourseList = crudCourseList;
    }



    @Override
    public CRUDViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.content_crudcourse,null);
        return new CRUDViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CRUDViewHolder holder, int position) {
        Course course = crudCourseList.get(position);

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class CRUDViewHolder extends RecyclerView.ViewHolder{

        public CRUDViewHolder(View itemView) {
            super(itemView);
        }
    }
}
