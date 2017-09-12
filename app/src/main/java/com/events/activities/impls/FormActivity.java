package com.events.activities.impls;

import android.view.View;
import android.widget.EditText;

import com.events.R;
import com.events.activities.BaseActivity;

public abstract class FormActivity extends BaseActivity {
	
	protected void editTextStyle (View editText) {
		if (editText == null || !(editText instanceof EditText)) {
			return;
		}
		
		editText.setOnFocusChangeListener (new View.OnFocusChangeListener () {
			
			@Override
			public void onFocusChange (View view, boolean hasFocus) {
				int bg = hasFocus ? R.drawable.separator_primary_dark : R.drawable.separator_primary_light;
				
				view.setBackgroundDrawable (getResources ().getDrawable (bg));
			}
		});
	}
}