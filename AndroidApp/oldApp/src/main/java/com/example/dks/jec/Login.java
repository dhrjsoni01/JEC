package com.example.dks.jec;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

public class Login extends AppCompatActivity {

    Button btn_login,btn_register,btn_student;
    String username="fake",password="fake",email="fake";
    TextView tv_forget;
    EditText et_email,et_password;
    String crosstableurl = "http://jecattendance.host22.com/official/crosstable.php";
    String subjectUrl ="http://jecattendance.host22.com/official/subject.php";
    String loginUrl = "http://jecattendance.host22.com/official/login.php";
    DBRollList dbRollList = new DBRollList(this);
    ProgressDialog progressDialog;
    TextInputLayout layout_pass,layout_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbRollList.createRecordTable();

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait..");

        SharedPreferences sharedPreferences = Login.this.getSharedPreferences(getString(R.string.shared_pref_file),MODE_PRIVATE);
        String mode =sharedPreferences.getString(getString(R.string.pref_login_mode),"error");
        if (mode.equals("Student")){
            if (sharedPreferences.getBoolean(getString(R.string.student_login_status),false)){
                startActivity(new Intent(getApplicationContext(),StudentWelcome.class));
                finish();
            }
        }else if (mode.equals("Faculty")) {
            if (sharedPreferences.getBoolean(getString(R.string.pref_login_status), false)) {
                Intent welcome = new Intent(getApplicationContext(), Faculty.class);
                startActivity(welcome);
                finish();
            }
        }
        variable_in();
        onclicks();

    }

    @Override
    protected void onStart() {
        if (isNetworkAvailable()){
            getcrosstable();
            getSubject();
        }else {
            new AlertDialog.Builder(Login.this)
                    .setTitle("No Internet")
                    .setMessage("Internet is mandatory for login")
                    .setCancelable(true)
                    .setPositiveButton("OK", null)
                    .show();
        }
        super.onStart();
    }

    private void variable_in(){
        btn_login = (Button) findViewById(R.id.btn_login_login);
        btn_register = (Button)findViewById(R.id.btn_register_login);
        btn_student = (Button)findViewById(R.id.btn_student_login_login);
        tv_forget = (TextView) findViewById(R.id.text_forget_login);
        et_email = (EditText)findViewById(R.id.input_email_login);
        et_password = (EditText)findViewById(R.id.input_password_login);
        layout_email = (TextInputLayout)findViewById(R.id.input_layout_email);
        layout_pass = (TextInputLayout)findViewById(R.id.input_layout_password);
    }

    private void onclicks(){
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(getApplicationContext(),Register.class);
                startActivity(register);
            }
        });

        btn_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getcrosstable();
                getSubject();
                Intent student = new Intent(getApplicationContext(),Student.class);
                startActivity(student);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getcrosstable();
                getSubject();
                progressDialog.show();
                logintask();

            }
        });
        tv_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               new AlertDialog.Builder(Login.this)
                        .setTitle("Attention")
                        .setMessage("this function is coming soon")
                        .setCancelable(true)
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }

    private boolean validateEmail() {
        String email = et_email.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            layout_email.setError("Invalid Email");
            requestFocus(et_email);
            return false;
        } else {
            layout_email.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (et_password.getText().toString().length() < 6) {
            layout_pass.setError("Password is too short");
            requestFocus(et_password);
            return false;
        } else {
            layout_pass.setErrorEnabled(false);
        }
        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void getcrosstable(){


        dbRollList.getcrosstable();

        StringRequest crosstable =new StringRequest(Request.Method.POST, crosstableurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parentobject = new JSONObject(response);
                    JSONArray parentarray = parentobject.getJSONArray("crosstable");
                    boolean add = false;
                    for(int i=0;i<parentarray.length();i++){
                        JSONObject finalobject = parentarray.getJSONObject(i);
                        String one =finalobject.getString("one");
                        String two = finalobject.getString("two");
                        String three =finalobject.getString("three");
                        String four =finalobject.getString("four");
                        add = dbRollList.addDataCrossTable(one.toLowerCase(),two.toLowerCase(),three.toLowerCase(),four.toLowerCase());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this,"Connection error, Please check network and Restart App",Toast.LENGTH_LONG).show();
            }
        });
        MySingletonVolly.getInstance(Login.this).addToRequestQue(crosstable);

    }

    private void logintask(){

        password =et_password.getText().toString().trim();
        email = et_email.getText().toString().trim();
        if(!validateEmail()){
            progressDialog.dismiss();

            return;
        }else if (!validatePassword()){
            progressDialog.dismiss();
            return;

        }else {
            StringRequest login = new StringRequest(Request.Method.POST, loginUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject parantobj = new JSONObject(response);
                        JSONArray parantarray = parantobj.getJSONArray("response");
                        JSONObject finalObj = parantarray.getJSONObject(0);
                        String status = finalObj.getString("status");
                        progressDialog.dismiss();
                        if (status.equals("ok")) {
                            username = finalObj.getString("username");
                            save_data();
                            Intent welcome = new Intent(getApplicationContext(), Faculty.class);
                            startActivity(welcome);
                            finish();
                        } else if (status.equals("wait")) {
                            new AlertDialog.Builder(Login.this)
                                    .setTitle("Attention")
                                    .setMessage("Dear " + finalObj.getString("username") + ", your account is not verified please wait or contact developer")
                                    .setCancelable(true)
                                    .setPositiveButton("OK", null)
                                    .show();

                        } else {
                            new AlertDialog.Builder(Login.this)
                                    .setTitle("Attention")
                                    .setMessage("Invalid Email or Password")
                                    .setCancelable(true)
                                    .setPositiveButton("OK", null)
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
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", email);
                    params.put("pass", password);
                    return params;
                }
            };
            MySingletonVolly.getInstance(getApplicationContext()).addToRequestQue(login);
               /* getcrosstable();
                getSubject();
               /* save_data();
                Intent welcome = new Intent(getApplicationContext(),Faculty.class);
                startActivity(welcome);
                */
        }
    }

    private void getSubject(){
        dbRollList.creatSubTable();
        StringRequest subjectrequest = new StringRequest(Request.Method.POST, subjectUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject parentobject = new JSONObject(response);
                    JSONArray parentarray = parentobject.getJSONArray("subject");
                    boolean add = false;
                    for(int i=0;i<parentarray.length();i++){
                        JSONObject finalobject = parentarray.getJSONObject(i);
                        add = dbRollList.insertSubject(finalobject.getString("s0"),finalobject.getString("s1"),finalobject.getString("s2"),finalobject.getString("s3"),finalobject.getString("s4"),finalobject.getString("s5"),finalobject.getString("s6"),finalobject.getString("s7"),finalobject.getString("s8"),finalobject.getString("s9"),finalobject.getString("s10"),finalobject.getString("s11"),finalobject.getString("s12"),finalobject.getString("s13"),finalobject.getString("s14"),finalobject.getString("s15"),finalobject.getString("s16"),finalobject.getString("s17"),finalobject.getString("s18"),finalobject.getString("s19"),finalobject.getString("s20"),finalobject.getString("s21"),finalobject.getString("s22"),finalobject.getString("s23"),finalobject.getString("s24"),finalobject.getString("s25"),finalobject.getString("s26"),finalobject.getString("s27"));
                    }
                    //Toast.makeText(getApplicationContext(),"subject table done",Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Login.this,"Connection error, Please check network and Restart App",Toast.LENGTH_LONG).show();

            }
        });

        MySingletonVolly.getInstance(Login.this).addToRequestQue(subjectrequest);
    }

    private void save_data(){
        SharedPreferences sharedPreferences = Login.this.getSharedPreferences(getString(R.string.shared_pref_file),MODE_PRIVATE);
        SharedPreferences.Editor editor =  sharedPreferences.edit();
        editor.putString(getString(R.string.pref_email_id),email);
        editor.putString(getString(R.string.pref_pass),password);
        editor.putString(getString(R.string.pref_user_name),username);
        editor.putBoolean(getString(R.string.pref_login_status),true);
        editor.putString(getString(R.string.pref_login_mode),"Faculty");
        editor.commit();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager  = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
