package com.events.model;

import android.os.Parcel;

import com.events.utils.Json;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class Model {

    protected String id;
    protected String revision;
	
    protected Model () {
    }

    protected Model (Parcel in) {
        id  = in.readString ();
    }

    public void modelWriteToParcel (Parcel dest, int flags) {
        dest.writeString (id);
    }
    
    @JsonProperty("_id")
    public String getId () {
        return id;
    }
    
    @JsonProperty("_id")
    public void setId (String id) {
        this.id = id;
    }
	
	@JsonProperty("_rev")
	public String getRevision () {
		return revision;
	}
	
	@JsonProperty("_rev")
	public void setRevision (String revision) {
		this.revision = revision;
	}
    
    @Override
    public String toString() {
        return Json.toString (this);
    }
    
    public static <T> T buildModelObject (String jsonStr, Class<T> modelClass) {
        return Json.buildObjectFromJsonStr (jsonStr, modelClass);
    }
}