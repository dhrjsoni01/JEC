package com.example.dks.jec;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DKS on 3/22/2017.
 */

public class Faculty extends AppCompatActivity {

    Button btn_attendance,btn_view,btn_history,btn_chnage_pin,btn_logout;
    int status = 0;
    String tableurl = "http://jecattendance.host22.com/official/getlist.php";
    ProgressDialog progressDialog;
    DBRollList dbRollList = new DBRollList(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_faculty);
        variable_in();
        onclicks();
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Wait..");
    }


    @Override
    protected void onStart() {
        SharedPreferences sharedPreferences = Faculty.this.getSharedPreferences(getString(R.string.shared_pref_file),MODE_PRIVATE);
        if (sharedPreferences.getString(getString(R.string.pref_pin),"error").equals("error")){
            setpin();
        }
        SyncTask syncTask = new SyncTask(this);
        syncTask.SendData();
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_faculty_sync,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id =item.getItemId();
        if (id == R.id.sync){
            new AlertDialog.Builder(Faculty.this)
                    .setTitle("WARNING")
                    .setMessage("It may take longer for downloading important files from server. Do you want to proceed??")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            progressDialog.show();
                            for (int b=1; b<8 ;b++){
                                for (int y = 1; y<5 ;y++){
                                    Cursor gettablename = dbRollList.getTableName("A"+String.valueOf(y),b);
                                    while (gettablename.moveToNext()) {

                                        rollbtask btask = new rollbtask(getApplicationContext());
                                        btask.execute(gettablename.getString(0));
                                    }

                                }
                            }

                        }
                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void variable_in(){
        btn_attendance = (Button) findViewById(R.id.btn_take_attendance);
        btn_view = (Button) findViewById(R.id.btn_view_attendance);
        btn_history = (Button) findViewById(R.id.btn_history);
        btn_chnage_pin = (Button) findViewById(R.id.btn_change_pin);
        btn_logout = (Button) findViewById(R.id.btn_logout_faculty);
        SharedPreferences sharedPreferences = Faculty.this.getSharedPreferences(getString(R.string.shared_pref_file),MODE_PRIVATE);
        TextView faculty_name = (TextView)findViewById(R.id.text_name_faculty);
        faculty_name.setText(sharedPreferences.getString(getString(R.string.pref_user_name),"Fake user,contacting developer"));
    }

    private void onclicks(){

        btn_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifypin();
            }
        });

        btn_chnage_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogChangePin dialogChangePin = new DialogChangePin();
                dialogChangePin.show(getFragmentManager(),"Change_pin");
            }
        });

        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RollSelect.class));
            }
        });
        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HistoryActivity.class));

            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleardata();
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });
    }

    public static class DialogChangePin extends DialogFragment {

        LayoutInflater inflater;
        View v;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            inflater = getActivity().getLayoutInflater();
            v = inflater.inflate(R.layout.changepin_dialog, null);
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
            builder.setView(v).setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            return builder.create();
        }
    }

    private void RollList(){

       /* for (int i=1;i<8;i++){
            for (int j=1;j<5;j++){
                final Cursor cursor;
                cursor = dbRollList.getTableName("A"+String.valueOf(j),i);
                while (cursor.moveToNext()) {
                    dbRollList.getTableOfRoll(cursor.getString(0));
                    StringRequest roll = new StringRequest(Request.Method.POST, tableurl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            JSONObject paranetobj = null;
                            try {
                                paranetobj = new JSONObject(response);
                                JSONArray parantarray = paranetobj.getJSONArray(cursor.getString(0));
                                boolean adding = false;
                                for (int z=0;z<parantarray.length();z++){
                                    JSONObject finalobj = parantarray.getJSONObject(z);
                                    adding = dbRollList.insertRoll(finalobj.getString("roll"),finalobj.getString("name"),cursor.getString(0));
                                }
                                Toast.makeText(Faculty.this,cursor.getString(0)+" added",Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Faculty.this,"Error in rolllist",Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("table",cursor.getString(0));
                            return params;
                        }
                    };

                    MySingletonVolly.getInstance(Faculty.this).addToRequestQue(roll);

                }
            }
        }
*/
    }

    private void setpin(){
        final AlertDialog.Builder lab_dialog = new AlertDialog.Builder(Faculty.this);
        final View alertview = getLayoutInflater().inflate(R.layout.set_pin,null);
        final EditText pin =(EditText)alertview.findViewById(R.id.pin);
        final TextView tv_pin = (TextView) alertview.findViewById(R.id.pin_text);
        tv_pin.setText("SET 4 DIGIT PIN");
        lab_dialog.setView(alertview).setPositiveButton("SET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String pinno = pin.getText().toString().trim();
                if (pinno.length()==4){
                    SharedPreferences sharedPreferences = Faculty.this.getSharedPreferences(getString(R.string.shared_pref_file),MODE_PRIVATE);
                    SharedPreferences.Editor editor =  sharedPreferences.edit();
                    editor.putString(getString(R.string.pref_pin),pinno);
                    editor.commit();
                }else {
                    dialog.cancel();
                    Toast.makeText(getApplicationContext(),"Incorrect input",Toast.LENGTH_LONG).show();
                    setpin();
                }
            }
        });
        AlertDialog dialog = lab_dialog.create();
        dialog.show();
    }

    private void verifypin(){
        SharedPreferences sharedPreferences = Faculty.this.getSharedPreferences(getString(R.string.shared_pref_file),MODE_PRIVATE);
        if (sharedPreferences.getString(getString(R.string.pref_pin),"error").equals("error")){
            setpin();
        }else {

            final AlertDialog.Builder lab_dialog = new AlertDialog.Builder(Faculty.this);
            final View alertview = getLayoutInflater().inflate(R.layout.set_pin, null);
            final EditText pin = (EditText) alertview.findViewById(R.id.pin);
            final TextView tv_pin = (TextView) alertview.findViewById(R.id.pin_text);
            tv_pin.setText("PIN");
            lab_dialog.setView(alertview).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String pinno = pin.getText().toString().trim();
                    SharedPreferences sharedPreferences = Faculty.this.getSharedPreferences(getString(R.string.shared_pref_file), MODE_PRIVATE);
                    if (sharedPreferences.getString(getString(R.string.pref_pin), "error").equals(pinno)) {
                        dialog.cancel();
                        new AlertDialog.Builder(Faculty.this)
                                .setTitle("Select Type Of Attendance")
                                .setMessage("Choose for Lab or Normal Attendance")
                                .setCancelable(true)
                                .setPositiveButton("Normal", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        SharedPreferences sharedPreferences = Faculty.this.getSharedPreferences(getString(R.string.shared_pref_file), MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putBoolean(getString(R.string.pref_atten_lab_mode), false);
                                        editor.commit();
                                        startActivity(new Intent(getApplicationContext(), Selection.class));
                                    }
                                }).setNegativeButton("Lab", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedPreferences = Faculty.this.getSharedPreferences(getString(R.string.shared_pref_file), MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean(getString(R.string.pref_atten_lab_mode), true);
                                editor.commit();
                                startActivity(new Intent(getApplicationContext(), Selection.class));
                            }
                        })
                                .show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Incorrect PIN", Toast.LENGTH_LONG).show();
                    }

                }
            });
            AlertDialog dialog = lab_dialog.create();
            dialog.show();
        }
    }
    
    public class rollbtask extends AsyncTask<String,String,String>{
        Context ctx;
        rollbtask(Context context){
            ctx = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String table1 = params[0];
            String table = table1.toLowerCase();

            try {
                URL url = new URL(tableurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("table","UTF-8")+"="+URLEncoder.encode(table,"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line=bufferedReader.readLine())!=null){
                    buffer.append(line);
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                String response = buffer.toString();
                dbRollList.getTableOfRoll(table);
                if(!response.equals("error")) {
                    JSONObject parentobject = new JSONObject(response);
                    JSONArray parentarray = parentobject.getJSONArray(table);
                    boolean add = false;
                    for(int i=0;i<parentarray.length();i++){
                        JSONObject finalobject = parentarray.getJSONObject(i);
                         add = dbRollList.insertRoll(finalobject.getString("roll"),finalobject.getString("name"),table);
                    }
                    if (add)
                    return table;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return "error";
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("error")){
                Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
            }else if (s.equals("me2013")) {
                progressDialog.dismiss();
            }
            progressDialog.setMessage(String.valueOf(status)+"/28...");
            status=status+1;
            super.onPostExecute(s);
        }
    }

    private void cleardata(){
        SharedPreferences sharedPreferences = Faculty.this.getSharedPreferences(getString(R.string.shared_pref_file),MODE_PRIVATE);
        SharedPreferences.Editor editor =  sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

}
