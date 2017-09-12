package com.events.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import com.events.R;

public class ProgressView {
	
	private static Dialog progress;
	
	public static void show (final Context context, final boolean cancelable) {
		if (progress != null && progress.isShowing ()) {
			return;
		}
		
		((Activity)context).runOnUiThread (new Runnable () {
			
			@Override
			public void run () {
				try {
					progress = new Dialog (context);
					//progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
					if (progress.getWindow () != null) {
						progress.getWindow ().setBackgroundDrawable (new ColorDrawable (android.graphics.Color.TRANSPARENT));
					}
					progress.setContentView (R.layout.default_progress_view);
					progress.setCancelable (cancelable);
					progress.show ();
				} catch (Exception e) {
					e.printStackTrace ();
					Logger.error (ProgressView.class.getSimpleName () + " --> Show", "Error when showing Progress view.");
				}
			}
		});
	}
	
	public static void dismiss () {
		if (progress != null && progress.isShowing ()) {
			progress.dismiss ();
		}
	}
}