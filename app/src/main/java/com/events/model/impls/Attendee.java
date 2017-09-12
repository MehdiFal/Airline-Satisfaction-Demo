package com.events.model.impls;

import android.os.Parcel;
import android.os.Parcelable;

import com.events.model.Model;

public class Attendee extends Model implements Parcelable {
	
	private String 		firstName;
	private String 		lastName;
	private String 		jobTitle;
	private String 		company;
	private String 		email;
	private String 		phoneNumber;
	private String [] 	interests;
	private int 		rating;
	private String 		message;
	
	public Attendee () {
	
	}
	
	private Attendee (Parcel in) {
		super (in);
		
		firstName 	= in.readString ();
		lastName 	= in.readString ();
		jobTitle 	= in.readString ();
		company 	= in.readString ();
		email 		= in.readString ();
		phoneNumber = in.readString ();
		interests 	= in.createStringArray ();
		rating 		= in.readInt 	();
		message 	= in.readString ();
	}
	
	@Override
	public void writeToParcel (Parcel parcel, int i) {
		super.modelWriteToParcel (parcel, i);
		
		parcel.writeString 		(firstName);
		parcel.writeString 		(lastName);
		parcel.writeString 		(jobTitle);
		parcel.writeString 		(company);
		parcel.writeString 		(email);
		parcel.writeString 		(phoneNumber);
		parcel.writeStringArray (interests);
		parcel.writeInt 		(rating);
		parcel.writeString 		(message);
	}
	
	@Override
	public int describeContents () {
		return 0;
	}
	
	public static final Creator<Attendee> CREATOR = new Creator<Attendee> () {
		
		@Override
		public Attendee createFromParcel (Parcel in) {
			return new Attendee (in);
		}
		
		@Override
		public Attendee[] newArray (int size) {
			return new Attendee [size];
		}
	};
	
	public String getFirstName () {
		return firstName;
	}
	
	public void setFirstName (String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName () {
		return lastName;
	}
	
	public void setLastName (String lastName) {
		this.lastName = lastName;
	}
	
	public String getJobTitle () {
		return jobTitle;
	}
	
	public void setJobTitle (String jobTitle) {
		this.jobTitle = jobTitle;
	}
	
	public String getCompany () {
		return company;
	}
	
	public void setCompany (String company) {
		this.company = company;
	}
	
	public String getEmail () {
		return email;
	}
	
	public void setEmail (String email) {
		this.email = email;
	}
	
	public String getPhoneNumber () {
		return phoneNumber;
	}
	
	public void setPhoneNumber (String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String[] getInterests () {
		return interests;
	}
	
	public void setInterests (String [] interests) {
		this.interests = interests;
	}
	
	public int getRating () {
		return rating;
	}
	
	public void setRating (int rating) {
		this.rating = rating;
	}
	
	public String getMessage () {
		return message;
	}
	
	public void setMessage (String message) {
		this.message = message;
	}
}