package com.example.dks.jec;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by DKS on 4/9/2017.
 */

public class HistoryActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<HistoryModel> arrayList = new ArrayList<>();
    DBRollList dbRollList = new DBRollList(this);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        dbRollList.createRecordTable();

        listView = (ListView) findViewById(R.id.recycler_view);
        setlist();

    }

    private void setlist(){
        arrayList.clear();
        String[] branches =  getResources().getStringArray(R.array.branch);
        Cursor history = dbRollList.gethistory();
        while (history.moveToNext()){
            int branch = Integer.parseInt(history.getString(0));
            int year = Integer.parseInt(history.getString(1));
            int sub = Integer.parseInt(history.getString(3));
            int column = (branch*4)+year;
            String subject = "";
            Cursor csubject = dbRollList.getSubjectName("S"+String.valueOf(column),sub);
            while (csubject.moveToNext()){
                subject = csubject.getString(0);
            }
            arrayList.add(new HistoryModel(branches[branch],"Semester "+history.getString(2),subject,history.getString(4),Integer.parseInt(history.getString(5))));

        }
        HistoryAdaptor historyAdaptor = new HistoryAdaptor(getApplicationContext(),R.layout.atten_hisstory_view,arrayList);
        listView.setAdapter(historyAdaptor);

    }



}
