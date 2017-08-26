package com.example.dks.jec;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class Selection extends AppCompatActivity {

    Button start;
    Spinner spinner_branch,spinner_year,spinner_subject;
    ArrayAdapter<CharSequence> arrayAdapter_branch,arrayAdapter_year;
    int int_branch,int_year,int_subject,strength;
    String table;
    DBRollList dbRollList = new DBRollList(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection);
        spinner_setting();
        buttonaction();

    }

    private void buttonaction(){
        start = (Button) findViewById(R.id.start_atten_select);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = Selection.this.getSharedPreferences(getString(R.string.shared_pref_file),MODE_PRIVATE);
                if (sharedPreferences.getBoolean(getString(R.string.pref_atten_lab_mode),false)){
                    lab_atten();
                }else{
                    Intent atten = new Intent(Selection.this,Attendance.class);
                    atten.putExtra("table",table);
                    atten.putExtra("branch",int_branch);
                    atten.putExtra("year",int_year);
                    atten.putExtra("subject",int_subject);
                    startActivity(atten);

                }

            }
        });
    }

    private void spinner_setting(){
        spinner_branch = (Spinner)findViewById(R.id.spinner_branch);
        spinner_year = (Spinner) findViewById(R.id.spinner_year);
        spinner_subject = (Spinner) findViewById(R.id.spinner_subject);

        arrayAdapter_branch = ArrayAdapter.createFromResource(this,R.array.branch,android.R.layout.simple_spinner_item);
        arrayAdapter_year = ArrayAdapter.createFromResource(this,R.array.year,android.R.layout.simple_spinner_item);

        arrayAdapter_year.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        arrayAdapter_branch.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinner_branch.setAdapter(arrayAdapter_branch);
        spinner_year.setAdapter(arrayAdapter_year);

        spinner_branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int_branch = (int) parent.getItemIdAtPosition(position);
                set_subject(int_branch,int_year);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getBaseContext(),"nothing selected in branch",Toast.LENGTH_LONG).show();
            }
        });
        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int_year = (int) parent.getItemIdAtPosition(position);
                set_subject(int_branch,int_year);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(getBaseContext(),"nothing selected in year",Toast.LENGTH_LONG).show();

            }
        });
    }

    public void lab_atten(){
        AlertDialog.Builder lab_dialog = new AlertDialog.Builder(Selection.this);
        View alertview = getLayoutInflater().inflate(R.layout.lab_atten_dialog,null);
        final EditText start = (EditText) alertview.findViewById(R.id.et_start);
        final EditText end = (EditText) alertview.findViewById(R.id.et_end);
        TextView total = (TextView) alertview.findViewById(R.id.total_strength);
        strength = dbRollList.strength(table);
        total.setText("Total Strength is "+String.valueOf(strength));
        lab_dialog.setView(alertview).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!start.getText().toString().isEmpty() && ! end.getText().toString().isEmpty()) {
                    int startno = Integer.parseInt(start.getText().toString());
                    int endno = Integer.parseInt(end.getText().toString());
                    if (endno > strength || startno > strength || startno > endno) {
                        Toast.makeText(Selection.this, "Wrong Input", Toast.LENGTH_SHORT).show();
                        lab_atten();
                    } else {
                        Intent atten = new Intent(Selection.this,Attendance.class);
                        atten.putExtra("table",table);
                        atten.putExtra("branch",int_branch);
                        atten.putExtra("year",int_year);
                        atten.putExtra("subject",int_subject);
                        atten.putExtra("start",startno);
                        atten.putExtra("end",endno);
                        startActivity(atten);
                    }
                }else {
                    Toast.makeText(Selection.this, "Wrong Input", Toast.LENGTH_SHORT).show();
                    lab_atten();
                }
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = lab_dialog.create();
        dialog.show();
    }


    private void set_subject(int branch,int year){

        final List<String> subject = new ArrayList<String>();

        int coloumn = (branch*4)+year ;

        final Cursor cursor = dbRollList.getAllSubject("s"+String.valueOf(coloumn));
        while (cursor.moveToNext()){
            String subjecttoadd = cursor.getString(0);
            if (subjecttoadd.equals("end")) {
            }else {
                subject.add(cursor.getString(0));
            }
        }
        ArrayAdapter<String> arrayAdapter_subject = new ArrayAdapter<String>(Selection.this,android.R.layout.simple_spinner_item,subject);

        arrayAdapter_subject.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinner_subject.setAdapter(arrayAdapter_subject);

        spinner_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursortable = dbRollList.getTableName("A"+String.valueOf(int_year+1),int_branch+1);
                while (cursortable.moveToNext()){
                    table = cursortable.getString(0);
                }
                int_subject = 1 + (int) parent.getItemIdAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

}
