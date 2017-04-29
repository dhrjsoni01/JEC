package com.example.dks.jec;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by DKS on 4/9/2017.
 */

public class HistoryAdaptor extends ArrayAdapter<HistoryModel> {

    ArrayList<HistoryModel> arrayList = new ArrayList<>();

    public HistoryAdaptor(Context context, int resource, ArrayList<HistoryModel> objects) {
        super(context, resource, objects);
        arrayList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        itemView = inflater.inflate(R.layout.atten_hisstory_view, null);

        ImageView status = (ImageView) itemView.findViewById(R.id.sync_status);
        TextView branch =(TextView) itemView.findViewById(R.id.branch_name);
        TextView sem = (TextView) itemView.findViewById(R.id.sem_name);
        TextView subject = (TextView) itemView.findViewById(R.id.sub_name);
        TextView date = (TextView) itemView.findViewById(R.id.date_time);

        branch.setText(arrayList.get(position).getBranch());
        sem.setText(arrayList.get(position).getSem());
        subject.setText(arrayList.get(position).getSubject());
        date.setText(arrayList.get(position).getDatetime());
        int syncstatus = arrayList.get(position).getStatus();
        if (syncstatus == 0){
            status.setImageResource(R.drawable.sync_fail);
        }
        if (syncstatus==1){
            status.setImageResource(R.drawable.sync_done);
        }
        return itemView;
    }

    /* public HistoryAdaptor(ArrayList<HistoryModel> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.atten_hisstory_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.branch.setText(arrayList.get(position).getBranch());
        holder.sem.setText(arrayList.get(position).getSem());
        holder.subject.setText(arrayList.get(position).getSubject());
        holder.date.setText(arrayList.get(position).getDatetime());
        int syncstatus = arrayList.get(position).getStatus();
        if (syncstatus == 0){
            holder.status.setImageResource(R.drawable.sync_fail);
        }
        if (syncstatus==1){
            holder.status.setImageResource(R.drawable.sync_done);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView status;
        TextView branch,sem,subject,date;

        public MyViewHolder(View itemView) {
            super(itemView);
            status = (ImageView) itemView.findViewById(R.id.sync_status);
            branch =(TextView) itemView.findViewById(R.id.branch_name);
            sem = (TextView) itemView.findViewById(R.id.sem_name);
            subject = (TextView) itemView.findViewById(R.id.sub_name);
            date = (TextView) itemView.findViewById(R.id.date_time);
        }
    }
    */
}
