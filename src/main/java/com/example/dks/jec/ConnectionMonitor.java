package com.example.dks.jec;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by DKS on 4/9/2017.
 */

public class ConnectionMonitor extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager  = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
