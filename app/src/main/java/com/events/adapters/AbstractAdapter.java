package com.events.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.events.model.Model;

import java.util.List;

public abstract class AbstractAdapter<T extends Model> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	
	protected List<T> input;
	
	protected AbstractAdapter (List<T> input) {
		this.input = input;
	}
	
	public void setInputData (List<T> input) {
		this.input = input;
		notifyDataSetChanged ();
	}
	
	@Override
	public int getItemCount () {
		return input == null ? 0 : input.size ();
	}
	
	protected T getRecord (int position) {
		if (getItemCount () == 0) {
			return null;
		}
		
		return input.get (position);
	}
	
	@Override
	public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {
		holder.itemView.setClickable (true);
	}
	
	protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		
		protected ViewHolder (View itemView) {
			super (itemView);
			
			if (clickable (AbstractAdapter.this.getItemViewType (getAdapterPosition ()))) {
				itemView.setOnClickListener (this);
			}
		}
		
		@Override
		public void onClick (View v) {
			int position = getAdapterPosition ();
			onRecordClick (position, getRecord (position));
		}
	}
	
	protected boolean clickable (int viewType) {
		return true;
	}
	
	protected abstract void    onRecordClick   (int position, T data);
}
