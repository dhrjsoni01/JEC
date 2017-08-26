package com.example.dks.jec;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DKS on 3/24/2017.
 */

public class RollSelect extends AppCompatActivity {

    Spinner spinner_branch,spinner_year;
    ArrayAdapter<CharSequence> arrayAdapter_branch,arrayAdapter_year;
    int int_branch,int_year;
    EditText et_roll;
    Button check;
    TextInputLayout inputLayout;
    String roll,name,table;
    String checkurl = "http://jecattendance.host22.com/official/check.php";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roll_select);
        spinner_setting();
        et_roll = (EditText)findViewById(R.id.input_enroll_student);
        check = (Button) findViewById(R.id.btn_view_attendance);
        inputLayout = (TextInputLayout)findViewById(R.id.input_layout_enroll);
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclicks();
            }
        });


    }

    private void spinner_setting(){
        spinner_branch = (Spinner)findViewById(R.id.spinner_branch);
        spinner_year = (Spinner) findViewById(R.id.spinner_year);

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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(getBaseContext(),"nothing selected in year",Toast.LENGTH_LONG).show();

            }
        });
    }

    private void onclicks(){
       if (!validateEnroll()){
           return;
       }

        progressDialog.show();
        vollycheck();
    }

    private boolean validateEnroll() {
        if (et_roll.getText().toString().length() != 12) {
            inputLayout.setError("Invalid Enrollment No.");
            requestFocus(et_roll);
            return false;
        } else {
            inputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void vollycheck(){

        roll = et_roll.getText().toString();

        StringRequest check = new StringRequest(Request.Method.POST, checkurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.i("Roll select Response",response);
                try {
                    JSONObject parentobj = new JSONObject(response);
                    JSONArray parentarray = parentobj.getJSONArray("sresponse");
                    JSONObject finalobj = parentarray.getJSONObject(0);
                    String code = finalobj.getString("code");
                    if (code.equals("ok")){
                        table = finalobj.getString("table");
                        name = finalobj.getString("name");
                        //Intent
                        Intent check = new Intent(getApplicationContext(),RecordNormal.class);
                        check.putExtra("branch",int_branch);
                        check.putExtra("year",int_year);
                        check.putExtra("name",name);
                        check.putExtra("table",table);
                        check.putExtra("roll",roll);
                        startActivity(check);

                    }else {
                        new AlertDialog.Builder(RollSelect.this)
                                .setTitle("Notification")
                                .setMessage("Invalid selection please conform and retry")
                                .setCancelable(true)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                })
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                new AlertDialog.Builder(RollSelect.this)
                        .setTitle("Notification")
                        .setMessage("error in network")
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        })
                        .show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("roll",roll);
                params.put("branch",String.valueOf(int_branch));
                params.put("year",String.valueOf(int_year));
                return params;
            }
        };
        MySingletonVolly.getInstance(getApplicationContext()).addToRequestQue(check);


    }


}
