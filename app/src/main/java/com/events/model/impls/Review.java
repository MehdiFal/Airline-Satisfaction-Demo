package com.events.model.impls;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Review implements Parcelable {
	
	private String 			id;
	private Attendee 		attendee;
	private boolean 		isSatisfied;
	private String 			message;
	
	public Review () {
	
	}
	
	private Review (Parcel in) {
		super();
		
		id 			= in.readString ();
		attendee 	= in.readParcelable (Attendee.class.getClassLoader ());
		isSatisfied = in.readByte () != 0;
		message 	= in.readString ();
	}
	
	@Override
	public void writeToParcel (Parcel dest, int flags) {
		dest.writeString 		(id);
		dest.writeParcelable 	(attendee, flags);
		dest.writeByte 			((byte) (isSatisfied ? 1 : 0));
		dest.writeString 		(message);
	}
	
	@Override
	public int describeContents () {
		return 0;
	}
	
	public static final Creator<Review> CREATOR = new Creator<Review> () {
		
		@Override
		public Review createFromParcel (Parcel in) {
			return new Review (in);
		}
		
		@Override
		public Review[] newArray (int size) {
			return new Review[size];
		}
	};
}