package com.events.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtils {

    public static boolean isOnline (final Activity context, boolean showErrPopup) {
        boolean connected = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
        NetworkInfo info 						= connectivityManager.getActiveNetworkInfo ();
		
        if (info != null && info.isAvailable () && info.isConnected()) {
            connected = true;
        }

        Logger.debug (context.getClass ().getSimpleName () + " -- Connectivity", (connected ? Lang.EMPTY : "Not ") + "Connected.");

        if (!connected && showErrPopup) {
            context.runOnUiThread (new Runnable () {
                
                @Override
                public void run () {
                    PopupUtils.show (context, "Please verify your internet connection.");
                }
            });
        }

        return connected;
    }
}