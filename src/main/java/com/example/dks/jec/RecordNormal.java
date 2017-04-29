package com.example.dks.jec;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DKS on 3/24/2017.
 */

public class RecordNormal extends AppCompatActivity {

    String name,roll,table;
    int branch,year;
    TextView name_roll;
    ProgressDialog progressDialog;
    String record_url="http://jecattendance.host22.com/official/rec2.php";
    ListView listView;
    DBRollList dbRollList = new DBRollList(this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);

        Bundle data = getIntent().getExtras();
        name = data.getString("name");
        roll = data.getString("roll");
        table = data.getString("table");
        branch =data.getInt("branch");
        year = data.getInt("year");

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        listView = (ListView)findViewById(R.id.record_list);

        name_roll = (TextView)findViewById(R.id.tv_name_roll);
        name_roll.setText(name+"\n"+roll.toUpperCase());
        recordvolly();

    }

    private void recordvolly(){

        dbRollList.get_personal_record();
        dbRollList.dropdata();

        StringRequest record_requst = new StringRequest(Request.Method.POST, record_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("RecordNormalResponse",response);

                try {
                    JSONObject parantobj = new JSONObject(response);
                    JSONArray parentarray = parantobj.getJSONArray("sresponse");
                    for(int z=0;z<parentarray.length();z++){
                        JSONObject finalobj = parentarray.getJSONObject(z);
                        String dateof= finalobj.getString("date");
                        String userof = finalobj.getString("user");
                        String subjectof = finalobj.getString("subject");
                        String valueof = finalobj.getString("data");
                        int intsub= Integer.parseInt(subjectof);
                        int intdata = Integer.parseInt(valueof);
                        boolean add = dbRollList.insetdata(dateof,intsub,intdata,userof);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                set_list();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("table",table.toLowerCase());
                params.put("roll",roll);
                return params;
            }
        };
        MySingletonVolly.getInstance(getApplicationContext()).addToRequestQue(record_requst);

    }

    private void set_list(){
        int coloumn = (branch*4)+year;
        int present,total;
        Float percent;
        ArrayList<RecordModel> arrayList = new ArrayList<>();
        Cursor cursor = dbRollList.getAllSubject("S"+String.valueOf(coloumn));
        while (cursor.moveToNext()){
            present = 0; total = 0;
            if (cursor.getString(0).equals("end")){
               break;
            }else {
                Cursor data = dbRollList.getdata(Integer.parseInt(cursor.getString(1)));
                while (data.moveToNext()){
                    int value = Integer.parseInt(data.getString(0));
                    if (value == 1){
                        present = present + 1;
                        total = total +1;
                    }
                    if (value == 0){
                        total = total+1;
                    }
                }
                percent = ((float)present/(float)total) *100;
                arrayList.add(new RecordModel(cursor.getString(0),"Present in "+String.valueOf(present)+" out of "+String.valueOf(total)+" class","Percentage = "+String.valueOf(percent)+"%"));
            }

        }

        RecordAdaptor recordAdaptor = new RecordAdaptor(getApplicationContext(),R.layout.record_list_layout,arrayList);
        listView.setAdapter(recordAdaptor);

    }

}
