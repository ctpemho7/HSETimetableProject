package com.example.baseproject.shedulefiles;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baseproject.R;

//это всё сопряжается с разметкой
public class ViewHolder extends RecyclerView.ViewHolder{
    private Context context;
    private TextView start;
    private TextView end;
    private TextView type;
    private TextView name;
    private TextView place;
    private TextView teacher;

    public ViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        start = itemView.findViewById(R.id.class_start);
        end = itemView.findViewById(R.id.class_end);
        type = itemView.findViewById(R.id.class_type);
        name = itemView.findViewById(R.id.class_name);
        place = itemView.findViewById(R.id.class_place);
        teacher = itemView.findViewById(R.id.class_teacher);
    }

    public void bind(final ScheduleItem data){
        start.setText(data.getStart());
        end.setText(data.getEnd());
        type.setText(data.getType());
        name.setText(data.getName());
        place.setText(data.getPlace());
        teacher.setText(data.getTeacher());
    }
}

