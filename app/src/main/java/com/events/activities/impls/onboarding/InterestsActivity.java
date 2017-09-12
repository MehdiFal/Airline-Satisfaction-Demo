package com.events.activities.impls.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.events.R;
import com.events.activities.impls.FormActivity;
import com.events.model.impls.Attendee;
import com.events.utils.Auth;
import com.events.utils.Json;
import com.events.utils.Lang;
import com.events.utils.Logger;
import com.events.workers.callbacks.impls.SimpleNetWorkerCallback;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class InterestsActivity extends FormActivity implements View.OnTouchListener {
	
	private Set<Integer> checkableChoices;
	
	private Attendee 	attendee;
	private Set<String> interests;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);
		setContentView (R.layout.activity_interests);
		
		attendee = getIntent ().getExtras ().getParcelable (Attendee.class.getSimpleName ());
	}
	
	@Override
	protected void initEvents () {
		findViewById (R.id.nextButton).setOnClickListener (new View.OnClickListener () {
			
			@Override
			public void onClick (View v) {
				if (interests != null) {
					for (String s : interests) {
						Logger.error (InterestsActivity.class.getSimpleName () + " --> onTouch", "Interests Set record -> " + s);
					}
					
					String [] aInterests = interests.toArray (new String [interests.size ()]);
					
					Logger.error (InterestsActivity.class.getSimpleName () + " --> Before click", "INTERESTS Array: " + Arrays.toString (aInterests));
					
					attendee.setInterests (aInterests);
					
					postData ();
				}
			}
		});
		
		for (int res : checkableChoices) {
			findViewById (res).setOnTouchListener (this);
		}
	}
	
	private void postData () {
		netWorker.post (
			getString (R.string.cloudantUrl) + Lang.SLASH + getString (R.string.cloudantDatabase),
			Json.build (attendee.toString ()),
			Auth.basic (getString (R.string.cloudantUsername), getString (R.string.cloudantPassword)),
			new SimpleNetWorkerCallback (this) {
				
				@Override
				public void onUIEnd () {
					Intent intent = new Intent (InterestsActivity.this, ThanksActivity.class);
					intent.putExtra (Attendee.class.getSimpleName (), attendee);
					startActivity (intent);
				}
			}
		);
	}
	
	@Override
	protected void bind () {
		checkableChoices = new HashSet<> ();
		checkableChoices.add (R.id.choice_ecm); 			checkableChoices.add (R.id.choice_bpm); 				checkableChoices.add (R.id.choice_odm);
		checkableChoices.add (R.id.choice_data_science); 	checkableChoices.add (R.id.choice_watson_analytics); 	checkableChoices.add (R.id.choice_bi);
		checkableChoices.add (R.id.choice_cloud); 			checkableChoices.add (R.id.choice_devops); 				checkableChoices.add (R.id.choice_integration);
	}
	
	@Override
	protected void initUi () {
	
	}
	
	@Override
	public boolean onTouch (View v, MotionEvent event) {
		if (event.getAction () == MotionEvent.ACTION_DOWN) {
			if (!(v instanceof TextView)) {
				return true;
			}
			
			v.performClick ();
			
			if (interests == null) {
				interests = new HashSet<> ();
			}
			
			String interest = ((TextView)v).getText ().toString ();
			Logger.error (InterestsActivity.class.getSimpleName () + " --> onTouch", "Interest: " + interest + " ? " + !v.isSelected ());
			if (v.isSelected ()) {
				interests.remove (interest);
			} else {
				interests.add (interest);
			}
			
			boolean newState = !v.isSelected ();
			v.setPressed (newState);
			v.setSelected (newState);
		}
		
		return true;
	}
}