package com.events.activities.impls.onboarding;

import android.os.Bundle;

import com.events.R;
import com.events.activities.BaseActivity;
import com.events.utils.Logger;

public class SplashActivity extends BaseActivity {
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);
		setContentView (R.layout.activity_splash);
		
		new Thread (new Runnable () {
			
			@Override
			public void run () {
				try {
					Thread.sleep (3000);
				} catch (InterruptedException e) {
					Logger.error (SplashActivity.class.getSimpleName () + " --> ", e);
				} finally {
					goTo (AttendeeActivity.class, Navigation.FINISH);
				}
			}
		}).start ();
	}
	
	@Override
	protected void initEvents () {
	
	}
	
	@Override
	protected void initUi () {
	
	}
}