package com.events.adapters.impls;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.events.R;
import com.events.adapters.AbstractAdapter;
import com.events.model.impls.Attendee;
import com.events.utils.Lang;

import java.util.List;

public abstract class AttendeesAdapter extends AbstractAdapter<Attendee> {
	
	public AttendeesAdapter (List<Attendee> input) {
		super (input);
	}
	
	@SuppressLint ("SetTextI18n")
	@Override
	public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {
		super.onBindViewHolder (holder, position);
		
		AttendeeViewHolder viewHolder = (AttendeeViewHolder)holder;
		
		Attendee record = getRecord (position);
		
		viewHolder.fullNameTv.setText (record.getFirstName () + Lang.SPACE + record.getLastName ());
		
		String jobPosition = record.getCompany ();
		viewHolder.positionTv.setText (record.getJobTitle () + " at " + record.getCompany ());
	}
	
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
		return new AttendeeViewHolder (LayoutInflater.from (parent.getContext ()).inflate (R.layout.attendee_record, parent, false));
	}
	
	private class AttendeeViewHolder extends ViewHolder {
		
		private TextView fullNameTv;
		private TextView positionTv;
		
		AttendeeViewHolder (View itemView) {
			super (itemView);
			
			fullNameTv  = (TextView) itemView.findViewById (R.id.fullNameTextView);
			positionTv  = (TextView) itemView.findViewById (R.id.positionTextView);
		}
	}
}