package com.events.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.events.utils.ProgressView;
import com.events.workers.NetWorker;
import com.events.workers.impls.net.NetWorkerImpl;

public abstract class BaseActivity extends AppCompatActivity {
	
	private final static int 	NavCode = 1001;
	
	private final static String FontDir = "fonts/";
	
	private static Typeface LightFont;
	private static Typeface LightItalicFont;
	private static Typeface RegularFont;
	private static Typeface RegularItalicFont;
	private static Typeface BoldFont;
	private static Typeface BoldItalicFont;
	
	protected enum Navigation {
		REGULAR,
		FINISH,
		FOR_RESULT,
		CLEAR_HISTORY
	}
	
	protected NetWorker netWorker;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);
		
		if (LightFont == null) {
			LightFont 			= Typeface.createFromAsset (getAssets (), FontDir + "Roboto-Light.ttf");
			LightItalicFont 	= Typeface.createFromAsset (getAssets (), FontDir + "Roboto-LightItalic.ttf");
			RegularFont 		= Typeface.createFromAsset (getAssets (), FontDir + "Roboto-Regular.ttf");
			RegularItalicFont 	= Typeface.createFromAsset (getAssets (), FontDir + "Roboto-RegularItalic.ttf");
			BoldFont 			= Typeface.createFromAsset (getAssets (), FontDir + "Roboto-Medium.ttf");
			BoldItalicFont 		= Typeface.createFromAsset (getAssets (), FontDir + "Roboto-MediumItalic.ttf");
		}
		
		netWorker = new NetWorkerImpl ();
	}
	
	@Override
	public void setContentView (@LayoutRes int layoutResID) {
		super.setContentView (layoutResID);
		
		ui ();
		
		bind ();
		
		events ();
	}
	
	@Override
	protected void onPause () {
		ProgressView.dismiss ();
		super.onPause ();
	}
	
	// bind method on resume, release method onPause
	
	private void events () {
		initEvents ();
	}
	
	protected void bind () {
	
	}
	
	private void ui () {
		initFonts ();
		initUi ();
	}
	
	protected void initFonts () {
		textViews ((ViewGroup) findViewById (android.R.id.content));
	}
	
	private void textViews (ViewGroup viewGroup) {
		for (int i = 0; i < viewGroup.getChildCount (); i ++) {
			View child = viewGroup.getChildAt (i);
			if (child instanceof ViewGroup) {
				textViews ((ViewGroup) child);
			} else if (child instanceof TextView) {
				TextView tv = (TextView)child;
				if (tv.getTypeface () != null) {
					boolean italic 	= tv.getTypeface ().isItalic ();
					boolean bold 	= tv.getTypeface ().isBold ();
					
					if (italic && bold) {
						tv.setTypeface (RegularItalicFont);
						continue;
					}
					
					if (italic) {
						tv.setTypeface (LightItalicFont);
						continue;
					}
					
					if (bold) {
						tv.setTypeface (RegularFont);
						continue;
					}
				}
				
				if (tv instanceof Button) {
					if (tv instanceof CheckBox || child instanceof RadioButton) {
						tv.setTypeface (LightFont);
					} else {
						tv.setTypeface (RegularFont);
					}
					continue;
				}
				
				tv.setTypeface (LightFont);
			}
		}
	}
	
	protected void goTo (Class<? extends BaseActivity> targetActivity) {
		goTo (targetActivity, Navigation.REGULAR);
	}
	
	protected void goTo (Class<? extends BaseActivity> targetActivity, Navigation navType) {
		Intent intent = new Intent (this, targetActivity);
		
		if (navType == Navigation.FOR_RESULT) {
			startActivityForResult (intent, NavCode);
			return;
		}
		
		if (navType == Navigation.CLEAR_HISTORY) {
			intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		}
		startActivity (intent);
		
		if (navType == Navigation.FINISH) {
			finish ();
		}
	}
	
	protected 	abstract 	void 	initEvents 	();
	protected 	abstract 	void 	initUi 		();
}