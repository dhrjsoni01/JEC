package com.example.dks.jec;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DKS on 4/12/2017.
 */

public class SyncTask {
    Context context;
    DBRollList dbRollList;
    Cursor cursor;
    String sendurl = "http://jecattendance.host22.com/official/sync.php";

    public SyncTask(Context context) {
        this.context = context;
        dbRollList = new DBRollList(context);

    }
    public void SendData(){
        cursor = dbRollList.getattendata();
        while (cursor.moveToNext()){
            Log.i("cursor test",cursor.getString(3));

            if (Integer.parseInt(cursor.getString(3))==0){
                final int id = Integer.parseInt(cursor.getString(0));
                StringRequest senddata = new StringRequest(Request.Method.POST, sendurl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("update response",response);
                        if (response.equals("done")){
                            dbRollList.updatestatus(id);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("update response","error");
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        Cursor data = dbRollList.getattendata();
                        while (data.moveToNext()) {
                            if (Integer.parseInt(data.getString(0))==id) {
                                params.put("table", data.getString(5));
                                params.put("user", data.getString(4));
                                params.put("date", data.getString(1));
                                params.put("sub", data.getString(6));
                                for (int i = 0; i < 120; i++) {
                                    params.put("S" + String.valueOf(i), data.getString(10 + i));
                                }
                            }
                        }
                        return params;
                    }
                };

                MySingletonVolly.getInstance(context).addToRequestQue(senddata);

            }
        }


    }



}
