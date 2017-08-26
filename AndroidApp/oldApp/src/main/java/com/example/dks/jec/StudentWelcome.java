package com.example.dks.jec;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by DKS on 4/10/2017.
 */

public class StudentWelcome extends AppCompatActivity {

    Button own,friends,logout;
    TextView tvname;
    String name,roll,table;
    int branch,year;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_welcome);
        tvname = (TextView)findViewById(R.id.text_name_student);
        own = (Button) findViewById(R.id.btn_view_attendance);
        friends = (Button) findViewById(R.id.btn_view_friend);
        logout = (Button)findViewById(R.id.btn_logout_faculty);

        SharedPreferences sharedPreferences = StudentWelcome.this.getSharedPreferences(getString(R.string.shared_pref_file),MODE_PRIVATE);
        name= sharedPreferences.getString(getString(R.string.student_name),"fake");
        roll= sharedPreferences.getString(getString(R.string.student_roll),"Fake");
        table= sharedPreferences.getString(getString(R.string.student_table),"CS2015");
        branch =sharedPreferences.getInt(getString(R.string.student_branch),0);
        year = sharedPreferences.getInt(getString(R.string.student_year),0);

        tvname.setText(name+"\n"+roll.toUpperCase());

        own.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent check = new Intent(getApplicationContext(),RecordNormal.class);
                check.putExtra("branch",branch);
                check.putExtra("year",year);
                check.putExtra("name",name);
                check.putExtra("table",table);
                check.putExtra("roll",roll);
                startActivity(check);

            }
        });

        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RollSelect.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleardata();
                startActivity(new Intent(getApplicationContext(),Student.class));
                finish();
            }
        });

    }
    private void cleardata(){
        SharedPreferences sharedPreferences = StudentWelcome.this.getSharedPreferences(getString(R.string.shared_pref_file),MODE_PRIVATE);
        SharedPreferences.Editor editor =  sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
