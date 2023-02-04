package com.example.baseproject.shedulefiles;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baseproject.R;

public class ViewHolderHeader extends RecyclerView.ViewHolder {

    private Context context;
    private final TextView title;

    public ViewHolderHeader(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        title = itemView.findViewById(R.id.schedule_header);
    }

    public void bind(String date){
        title.setText(date);
    }
}