package com.events.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class PopupUtils {

    public static void show (Context context) {
        show (context, null, null);
    }

    public static void show (Context context, String message) {
        show (context, null, message);
    }

    public static void show (Context context, String title, String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder (context);

        if (!Lang.isNullOrEmpty (title)) {
            dialog.setTitle (title);
        }

        if (!Lang.isNullOrEmpty (message)) {
            dialog.setMessage (message);
        }

        dialog.setCancelable (false);

        dialog.setPositiveButton (
			"Ok",
			new DialogInterface.OnClickListener () {

				@Override
				public void onClick (DialogInterface dialog, int id) {
					dialog.dismiss ();
				}
        	}
		);
        dialog.create ().show ();
    }
	
	public static void show (Context context, String title, String message, DialogInterface.OnClickListener okListener) {
		AlertDialog.Builder dialog = new AlertDialog.Builder (context);
		
		if (!Lang.isNullOrEmpty (title)) {
			dialog.setTitle (title);
		}
		
		if (!Lang.isNullOrEmpty (message)) {
			dialog.setMessage (message);
		}
		
		dialog.setCancelable (false);
		
		dialog.setPositiveButton ("Ok", okListener);
		dialog.setNegativeButton ("Cancel", new DialogInterface.OnClickListener () {
			@Override
			public void onClick (DialogInterface dialog, int which) {
				dialog.dismiss ();
			}
		});
		dialog.create ().show ();
	}
}