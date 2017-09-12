package com.events.activities.impls.onboarding;

import android.os.Bundle;

import com.events.R;
import com.events.activities.BaseActivity;
import com.events.model.impls.Attendee;
import com.events.utils.Logger;

import java.util.Arrays;

public class ThanksActivity extends BaseActivity {
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);
		setContentView (R.layout.activity_thanks);
		
		Attendee attendee = getIntent ().getExtras ().getParcelable (Attendee.class.getSimpleName ());
		Logger.error (ThanksActivity.class.getSimpleName () + " --> Interests: ", Arrays.toString (attendee.getInterests ()));
		//Toast.makeText (ThanksActivity.this, "Attendee: " + attendee.toString (), Toast.LENGTH_LONG).show ();
		// here server request
	}
	
	@Override
	protected void initEvents () {
		new Thread (new Runnable () {
			
			@Override
			public void run () {
				try {
					Thread.sleep (3000);
				} catch (InterruptedException e) {
					Logger.error (ThanksActivity.class.getSimpleName () + " --> ", e);
				} finally {
					goTo (AttendeeActivity.class, Navigation.FINISH);
				}
			}
		}).start ();
	}
	
	@Override
	protected void initUi () {
	
	}
}