package com.events.activities.impls.stats;

import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.events.R;
import com.events.activities.BaseActivity;
import com.events.model.impls.Attendee;
import com.events.utils.Lang;

public class AttendeeDetailsActivity extends BaseActivity {
	
	private final static SparseIntArray ReverseRate = new SparseIntArray ();
	static {
		ReverseRate.put (5, R.string.activity_rating_options_5);
		ReverseRate.put (4, R.string.activity_rating_options_4);
		ReverseRate.put (3, R.string.activity_rating_options_3);
		ReverseRate.put (2, R.string.activity_rating_options_2);
		ReverseRate.put (1, R.string.activity_rating_options_1);
	}
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);
		
		setContentView (R.layout.activity_attendee_details);
	}
	
	@Override
	protected void bind () {
		Attendee attendee = getIntent ().getParcelableExtra (Attendee.class.getSimpleName ());
		
		String none = getString (R.string.none);
		
		((TextView)findViewById (R.id.firstNameTv)).setText 	(Lang.noneIfNullOrEmpty (attendee.getFirstName (), none));
		((TextView)findViewById (R.id.lastNameTv)).setText 		(Lang.noneIfNullOrEmpty (attendee.getLastName (), none));
		((TextView)findViewById (R.id.jobTitleTv)).setText 		(Lang.noneIfNullOrEmpty (attendee.getJobTitle (), none));
		((TextView)findViewById (R.id.companyTv)).setText 		(Lang.noneIfNullOrEmpty (attendee.getCompany (), none));
		((TextView)findViewById (R.id.emailTv)).setText 		(Lang.noneIfNullOrEmpty (attendee.getEmail (), none));
		((TextView)findViewById (R.id.phoneNumberTv)).setText 	(Lang.noneIfNullOrEmpty (attendee.getPhoneNumber (), none));
		
		RadioButton ratingRadio = (RadioButton)findViewById (R.id.ratingRadio);
		if (attendee.getRating () == 0) {
			ratingRadio.setVisibility (View.GONE);
		} else {
			ratingRadio.setText (getString (ReverseRate.get (attendee.getRating ())));
		}
		
		if (!Lang.isNullOrEmpty (attendee.getMessage ())) {
			((TextView)findViewById (R.id.messageTV)).setText (attendee.getMessage ());
		}
	}
	
	@Override
	protected void initEvents () {
	
	}
	
	@Override
	protected void initUi () {
	
	}
}