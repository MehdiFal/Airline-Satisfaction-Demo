package com.events.activities.impls.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.events.R;
import com.events.activities.impls.FormActivity;
import com.events.model.impls.Attendee;
import com.events.utils.Auth;
import com.events.utils.Json;
import com.events.utils.Lang;
import com.events.workers.callbacks.impls.SimpleNetWorkerCallback;

public class RatingActivity extends FormActivity {
	
	private Attendee 	attendee;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);
		setContentView (R.layout.activity_rating);
	}
	
	@Override
	public void bind () {
		attendee = getIntent ().getParcelableExtra (Attendee.class.getSimpleName ());
		attendee.setRating (5);
	}
	
	@Override
	protected void initEvents () {
		((RadioGroup)findViewById (R.id.satisfactionRadioGroup)).setOnCheckedChangeListener (new RadioGroup.OnCheckedChangeListener () {
			
			@Override
			public void onCheckedChanged (RadioGroup radioGroup, int i) {
				attendee.setRating (Integer.valueOf (findViewById (i).getTag ().toString ()));
			}
		});
		
		findViewById (R.id.nextButton).setOnClickListener (new View.OnClickListener () {
			
			@Override
			public void onClick (View v) {
				String comment = ((EditText)findViewById (R.id.commentEditText)).getText ().toString ();
				if (!Lang.isNullOrEmpty (comment)) {
					attendee.setMessage (comment);
				}
				
				postData ();
			}
		});
	}
	
	private void postData () {
		netWorker.post (
				getString (R.string.cloudantUrl) + Lang.SLASH + getString (R.string.cloudantDatabase),
				Json.build (attendee.toString ()),
				Auth.basic (getString (R.string.cloudantUsername), getString (R.string.cloudantPassword)),
				new SimpleNetWorkerCallback (this) {
					
					@Override
					public void onUIEnd () {
						Intent intent = new Intent (RatingActivity.this, ThanksActivity.class);
						intent.putExtra (Attendee.class.getSimpleName (), attendee);
						startActivity (intent);
					}
				}
		);
	}
	
	@Override
	protected void initUi () {
	
	}
}
