package com.events.activities.impls.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.events.R;
import com.events.activities.impls.FormActivity;
import com.events.activities.impls.stats.AttendeesListActivity;
import com.events.model.impls.Attendee;

public class AttendeeActivity extends FormActivity {
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);
		setContentView (R.layout.activity_attendee);
	}
	
	@Override
	protected void initEvents () {
		findViewById (R.id.nextButton).setOnClickListener (new View.OnClickListener () {
			
			@Override
			public void onClick (View v) {
				Intent intent = new Intent (AttendeeActivity.this, RatingActivity.class);
				intent.putExtra (Attendee.class.getSimpleName (), buildAttendee ());
				startActivity (intent);
			}
		});
	}
	
	@Override
	protected void initUi () {
		editTextStyle (findViewById (R.id.firstNameEditText));
		editTextStyle (findViewById (R.id.lastNameEditText));
		editTextStyle (findViewById (R.id.jobTitleEditText));
		editTextStyle (findViewById (R.id.companyEditText));
		editTextStyle (findViewById (R.id.emailEditText));
		editTextStyle (findViewById (R.id.phoneNumberEditText));
	}
	
	private Attendee buildAttendee () {
		Attendee attendee = new Attendee ();
		
		attendee.setFirstName 	(((TextView)findViewById (R.id.firstNameEditText)).getText ().toString ());
		attendee.setLastName 	(((TextView)findViewById (R.id.lastNameEditText)).getText ().toString ());
		attendee.setJobTitle 	(((TextView)findViewById (R.id.jobTitleEditText)).getText ().toString ());
		attendee.setCompany 	(((TextView)findViewById (R.id.companyEditText)).getText ().toString ());
		attendee.setEmail 		(((TextView)findViewById (R.id.emailEditText)).getText ().toString ());
		attendee.setPhoneNumber (((TextView)findViewById (R.id.phoneNumberEditText)).getText ().toString ());
		
		return attendee;
	}
	
	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate (R.menu.attendee_menu, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
		if (item.getItemId () == R.id.stats) {
			goTo (AttendeesListActivity.class);
		}
		return true;
	}
}