package com.example.dks.jec;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
 * Created by DKS on 3/22/2017.
 */

public class Student extends AppCompatActivity {
    Spinner spinner_branch,spinner_year;
    ArrayAdapter<CharSequence> arrayAdapter_branch,arrayAdapter_year;
    int int_branch,int_year;
    String roll,Stringpass,Stringmobile,Stringnewpass,table,name;
    Button btn_login,btn_forget;
    EditText et_enroll,et_pass;
    TextInputLayout layout_enroll,layout_pass;
    DBRollList dbRollList = new DBRollList(this);
    String mobile_url ="http://jecattendance.host22.com/official/mobile.php";
    String setpass_url = "http://jecattendance.host22.com/official/setpass.php";
    String studentloginurl = "http://jecattendance.host22.com/official/studentlogin.php";
    ProgressDialog progressDialog;
    boolean showdialog = false;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait..");
        spinner_setting();
        variable_in();
        
    }

    private void variable_in(){
        btn_forget = (Button) findViewById(R.id.btn_forget_student);
        btn_login = (Button) findViewById(R.id.btn_login_student);
        et_enroll = (EditText) findViewById(R.id.input_enroll_student);
        et_pass = (EditText) findViewById(R.id.input_password_student);
        layout_enroll = (TextInputLayout) findViewById(R.id.input_layout_enroll);
        layout_pass = (TextInputLayout) findViewById(R.id.input_layout_password);

        btn_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               forget();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void forget(){
        if (!validateEnroll()){
            return;
        }
        progressDialog.show();
        roll = et_enroll.getText().toString().trim();
        Cursor cursor = dbRollList.getTableName("A"+String.valueOf(int_year+1),int_branch+1);
        while (cursor.moveToNext()) {
            table = cursor.getString(0);
        }

        forgetdialog();

    }

    private void login(){
        if (!validateEnroll()){
            return;
        }
        if (!validatePassword()){
            return;
        }

        // logic on login...
        progressDialog.show();
        roll = et_enroll.getText().toString().trim();
        Cursor cursor = dbRollList.getTableName("A"+String.valueOf(int_year+1),int_branch+1);
        while (cursor.moveToNext()) {
            table = cursor.getString(0);
        }
        studentlogin();


    }
    private void savedata(){
        SharedPreferences sharedPreferences = Student.this.getSharedPreferences(getString(R.string.shared_pref_file),MODE_PRIVATE);
        SharedPreferences.Editor editor =  sharedPreferences.edit();
        editor.putString(getString(R.string.student_roll),roll);
        editor.putString(getString(R.string.student_pass),Stringpass);
        editor.putString(getString(R.string.student_table),table);
        editor.putBoolean(getString(R.string.student_login_status),true);
        editor.putString(getString(R.string.pref_login_mode),"Student");
        editor.putInt(getString(R.string.student_branch),int_branch);
        editor.putInt(getString(R.string.student_year),int_year);
        editor.putString(getString(R.string.student_name),name);
        editor.commit();
    }

    private void studentlogin(){
        roll = et_enroll.getText().toString().trim();
        Stringpass = et_pass.getText().toString().trim();
        StringRequest login = new StringRequest(Request.Method.POST, studentloginurl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (response.equals("error")){
                    progressDialog.dismiss();
                    new android.support.v7.app.AlertDialog.Builder(Student.this)
                            .setTitle("Error")
                            .setMessage("Invalid login, please check Enroll, Branch, Year or Password")
                            .setCancelable(true)
                            .setPositiveButton("OK", null)
                            .show();
                }else {
                    try {
                        JSONObject parantobj = new JSONObject(response);
                        JSONArray parantarray = parantobj.getJSONArray("response");
                        JSONObject finalObj = parantarray.getJSONObject(0);
                        String status = finalObj.getString("status");
                        if (status.equals("ok")){
                            name = finalObj.getString("username");
                            savedata();
                            startActivity(new Intent(getApplicationContext(),StudentWelcome.class));
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("roll",roll);
                params.put("table",table.toLowerCase());
                params.put("pass",Stringpass);
                return params;
            }
        };
        MySingletonVolly.getInstance(getApplicationContext()).addToRequestQue(login);
    }

    private boolean validatePassword() {
        if (et_pass.getText().toString().length() < 6) {
            layout_pass.setError("Password is too short");
            requestFocus(et_pass);
            return false;
        } else {
            layout_pass.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateEnroll() {
        if (et_enroll.getText().toString().length() != 12) {
            layout_enroll.setError("Invalid Enrollment No.");
            requestFocus(et_enroll);
            return false;
        } else {
            layout_enroll.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void spinner_setting(){
        spinner_branch = (Spinner)findViewById(R.id.spinner_branch_student);
        spinner_year = (Spinner) findViewById(R.id.spinner_year_student);

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

    private void forgetdialog(){

        final AlertDialog.Builder lab_dialog = new AlertDialog.Builder(Student.this);
        final View alertview = getLayoutInflater().inflate(R.layout.student_forget_dialog,null);
        final EditText mobile =(EditText)alertview.findViewById(R.id.input_phone_dialog);
        final EditText password = (EditText) alertview.findViewById(R.id.input_password_dialog);
        final TextView tv_mobile = (TextView) alertview.findViewById(R.id.tv_mobile_no);
        final StringRequest setdialog = new StringRequest(Request.Method.POST, mobile_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if (response.equals("error")){
                    showdialog = false;

                    new android.support.v7.app.AlertDialog.Builder(Student.this)
                            .setTitle("Error")
                            .setMessage("Something went wrong,Please check Enrollment no, branch and year")
                            .setCancelable(true)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();

                }else if(response.equals("fail")){
                    showdialog = false;
                    new android.support.v7.app.AlertDialog.Builder(Student.this)
                            .setTitle("Error")
                            .setMessage("You have already changed your password")
                            .setCancelable(true)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();

                }else {
                    showdialog = true;
                    tv_mobile.setText("XXXXXXX"+response);
                    Log.d("response from server",response);
                    lab_dialog.setView(alertview).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progressDialog.show();
                            Stringmobile = mobile.getText().toString().trim();
                            Stringnewpass = password.getText().toString().trim();
                            setpass();

                        }
                    }).setNegativeButton("Cancel", null);
                    AlertDialog dialog = lab_dialog.create();
                    if (showdialog) {
                        dialog.show();
                    }
                    showdialog = false;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                showdialog = false;
                new android.support.v7.app.AlertDialog.Builder(Student.this)
                        .setTitle("Error")
                        .setMessage("network error")
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("roll",roll);
                params.put("table",table.toLowerCase());
                return params;
            }
        };
        MySingletonVolly.getInstance(getApplicationContext()).addToRequestQue(setdialog);

        lab_dialog.setView(alertview).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.show();
                Stringmobile = mobile.getText().toString().trim();
                Stringnewpass = password.getText().toString().trim();
                setpass();

            }
        }).setNegativeButton("Cancel", null);
        AlertDialog dialog = lab_dialog.create();
        if (showdialog) {
            dialog.show();
        }

    }

    private void setpass() {
        if (Stringnewpass.length() < 6) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),"Password is too short",Toast.LENGTH_LONG).show();
        } else {
            StringRequest setdialog = new StringRequest(Request.Method.POST, setpass_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    if (response.equals("done")) {

                        new android.support.v7.app.AlertDialog.Builder(Student.this)
                                .setTitle("Success")
                                .setMessage("Your password Successfully changed. you will not be able to change password again.")
                                .setCancelable(true)
                                .setPositiveButton("OK", null)
                                .show();

                    } else {
                        new android.support.v7.app.AlertDialog.Builder(Student.this)
                                .setTitle("Error")
                                .setMessage("Incorrect mobile no")
                                .setCancelable(true)
                                .setPositiveButton("OK", null)
                                .show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    showdialog = false;
                    new android.support.v7.app.AlertDialog.Builder(Student.this)
                            .setTitle("Error")
                            .setMessage("network error")
                            .setCancelable(true)
                            .setPositiveButton("OK", null)
                            .show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("mobile", Stringmobile);
                    params.put("table", table.toLowerCase());
                    params.put("pass",Stringnewpass);
                    params.put("roll",roll);
                    return params;
                }
            };
            MySingletonVolly.getInstance(getApplicationContext()).addToRequestQue(setdialog);

        }
    }



}
