package com.freeuc.earn.utils;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {
    AlertDialog dialog;
    @Override
    public void onReceive(final Context context, final Intent intent) {
        final boolean status = NetworkUtil.isInternetOn(context);
        if (status){
            Toast.makeText(context,"Connected",Toast.LENGTH_SHORT).show();
            if(dialog != null){
                dialog.dismiss();
            }
        }
        else{
            Toast.makeText(context,"Not Connected",Toast.LENGTH_SHORT).show();
            dialog = new AlertDialog.Builder(context)
                    .setTitle("No Internet!!")
                    .setMessage("Please check your internet connection.")
                    .setCancelable(false).create();
            dialog.show();
        }
    }
}
