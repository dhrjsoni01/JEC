package com.example.dks.jec;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

public class Register extends AppCompatActivity {
    EditText et_name,et_email,et_mobile,et_pass;
    Button btn_register;
    TextInputLayout layout_name,layout_pass,layout_email,layout_mobile;
    String register_url="http://jecattendance.host22.com/official/reg.php";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        variable_in();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });

    }

    private void variable_in() {
        et_email = (EditText) findViewById(R.id.input_email_register);
        et_mobile = (EditText) findViewById(R.id.input_phone_register);
        et_name = (EditText) findViewById(R.id.input_name_register);
        et_pass = (EditText) findViewById(R.id.input_password_register);
        btn_register = (Button) findViewById(R.id.btn_register_register);

        layout_email = (TextInputLayout)findViewById(R.id.input_layout_email);
        layout_mobile = (TextInputLayout) findViewById(R.id.input_layout_phone);
        layout_name = (TextInputLayout) findViewById(R.id.input_layout_name);
        layout_pass = (TextInputLayout) findViewById(R.id.input_layout_password);


        et_pass.addTextChangedListener(new MyTextWatcher(et_pass));
        et_mobile.addTextChangedListener(new MyTextWatcher(et_mobile));
        et_pass.addTextChangedListener(new MyTextWatcher(et_pass));
        et_name.addTextChangedListener(new MyTextWatcher(et_name));

    }

    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait..");
        regnow();


    }
    private void regnow(){
        StringRequest register_Req = new StringRequest(Request.Method.POST, register_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if (response.equals("done")) {
                    new AlertDialog.Builder(Register.this)
                            .setTitle("Success")
                            .setMessage("you are successfully registered with us. Please wait for you account until your account get verified by us. THANK YOU")
                            .setCancelable(true)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .show();
                }else {
                    new AlertDialog.Builder(Register.this)
                            .setTitle("Fail")
                            .setMessage("Email is already registered with us")
                            .setCancelable(true)
                            .setPositiveButton("OK", null)
                            .show();
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
                params.put("email", et_email.getText().toString().trim());
                params.put("pass", et_pass.getText().toString().trim());
                params.put("name",et_name.getText().toString().trim());
                params.put("mob",et_mobile.getText().toString().trim());
                return params;
            }
        };
        MySingletonVolly.getInstance(getApplicationContext()).addToRequestQue(register_Req);
    }

    private boolean validateName() {
        if (et_name.getText().toString().trim().isEmpty()) {
            layout_name.setError("Please Enter your name");
            requestFocus(et_name);
            return false;
        } else {
            layout_name.setErrorEnabled(false);
        }

        return true;
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
        if (et_pass.getText().toString().length() < 6) {
            layout_pass.setError("Password is too short");
            requestFocus(et_pass);
            return false;
        } else {
            layout_pass.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateMobile() {
        if (et_mobile.getText().toString().length() != 10) {
            layout_mobile.setError("Invalid Mobile");
            requestFocus(et_mobile);
            return false;
        } else {
            layout_mobile.setErrorEnabled(false);
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

    private class MyTextWatcher implements TextWatcher {

            private View view;

            private MyTextWatcher(View view) {
                this.view = view;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                switch (view.getId()) {
                    case R.id.input_name_register:
                        validateName();
                        break;
                    case R.id.input_email_register:
                        validateEmail();
                        break;
                    case R.id.input_password_register:
                        validatePassword();
                        break;
                    case R.id.input_phone_register:
                        validateMobile();
                        break;
                }
            }

            }

}
