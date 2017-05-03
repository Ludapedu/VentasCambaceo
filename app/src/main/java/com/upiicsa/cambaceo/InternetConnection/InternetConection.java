package com.upiicsa.cambaceo.InternetConnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by luis.perez on 21/10/2016.
 */

public class InternetConection {
    AppCompatActivity activity;

    public InternetConection(AppCompatActivity a)
    {
        activity = a;
    }
    public Boolean IsConnected() {
        ConnectivityManager connectivity = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }
}
