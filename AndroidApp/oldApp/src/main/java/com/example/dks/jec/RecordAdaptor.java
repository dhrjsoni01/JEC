package com.example.dks.jec;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by DKS on 4/9/2017.
 */

public class RecordAdaptor extends ArrayAdapter<RecordModel> {

    ArrayList<RecordModel> values = new ArrayList<>();

    public RecordAdaptor(Context context, int resource, ArrayList<RecordModel> objects) {
        super(context, resource, objects);
        values = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.record_list_layout, null);
        TextView firstline= (TextView) v.findViewById(R.id.subject_name);
        TextView  secondline= (TextView) v.findViewById(R.id.no_of_presence);
        TextView  thirdline= (TextView) v.findViewById(R.id.percentage);
        firstline.setText(values.get(position).getSubject());
        secondline.setText(values.get(position).getPesence());
        thirdline.setText(values.get(position).getPercentage());
        return v;
    }
}
