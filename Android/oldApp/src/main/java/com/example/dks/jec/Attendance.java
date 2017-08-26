package com.example.dks.jec;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DKS on 3/23/2017.
 */

public class Attendance extends AppCompatActivity {

    ListView lv_names;
    Button next,back;
    int int_branch,int_year,int_subject,strength,start,end;
    String table;
    DBRollList dbRollList = new DBRollList(this);
    int value[];
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance);
        Bundle data = getIntent().getExtras();
        table = data.getString("table");
        int_branch = data.getInt("branch");
        int_year = data.getInt("year");
        int_subject = data.getInt("subject");
        strength = dbRollList.strength(table);
        dbRollList.createRecordTable();
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait..");
        start = 1;
        end = strength+1;
        SharedPreferences sharedPreferences = Attendance.this.getSharedPreferences(getString(R.string.shared_pref_file),MODE_PRIVATE);
        if (sharedPreferences.getBoolean(getString(R.string.pref_atten_lab_mode),false)){
            start = data.getInt("start");
            end = data.getInt("end");
        }
        firstcall();
        set_atten_value();
        onclicks();

    }
    private void firstcall(){
        next = (Button) findViewById(R.id.next_button);
        back = (Button) findViewById(R.id.prev_button);
        lv_names = (ListView) findViewById(R.id.list1);
        lv_names.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        List<String> list = new ArrayList<String>();

        for (int count = start;count<end;count++){
            Cursor student_cursor = dbRollList.getStudent(count,table);
            while (student_cursor.moveToNext()){
                list.add(student_cursor.getString(0)+"\n"+student_cursor.getString(1));
            }
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(),R.layout.row_atten,R.id.textview_row,list);
        lv_names.setAdapter(arrayAdapter);
    }
    private void set_atten_value(){
        value = new int[strength];
        for (int count=0;count<strength;count++){
            value[count] = 0;
        }
        lv_names.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (value[position+start-1]==0){
                    value[position+start-1]=1;
                }else {
                    value[position+start-1]=0;
                }
            }
        });

    }

    private long feed_data(){
        SharedPreferences sharedPreferences = Attendance.this.getSharedPreferences(getString(R.string.shared_pref_file),MODE_PRIVATE);
        int odd_sem = (int_year*2)+1;
        int even_sem = (int_year*2)+2;
        long id = dbRollList.firstfill(sharedPreferences.getString(getString(R.string.pref_user_name),"fake"),table,int_subject,int_branch,int_year,even_sem);
        return id;
    }

    private void onclicks(){
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(Attendance.this)
                        .setTitle("Attention")
                        .setMessage("Once Attendance Submitted can't modify latter. Are you sure to Submit it??")
                        .setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                btast backend = new btast(getApplicationContext());
                                backend.execute();
                            }
                        })
                        .setNegativeButton("No",null)
                .show();


            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new AlertDialog.Builder(Attendance.this)
                        .setTitle("Attention")
                        .setMessage("Are you sure you want to cancel attendance??")
                        .setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("No",null)
                .show();

            }
        });
    }
    private void submit(){



    }

    private class btast extends AsyncTask<String,String,String>{

        Context context;
        int id;
        public btast(Context context){
            this.context  = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            id  =(int) feed_data();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            boolean update = false;
            for (int count = start;count<end;count++){
                update = dbRollList.updaterecord(count,value[count-1],id);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            finish();
            super.onPostExecute(s);
        }
    }
}
