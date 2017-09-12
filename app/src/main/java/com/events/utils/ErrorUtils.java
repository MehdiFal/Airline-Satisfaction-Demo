package com.events.utils;

import com.events.model.Error;

public class ErrorUtils {
	
	public static Error getError () {
		return getError (null, null, null);
	}
	
	public static Error getError (Error.Type type) {
		return getError (null, null, type);
	}
	
    public static Error getError (String message, Exception ex) {
        return getError (message, ex, null);
    }
    
    public static Error getError (String message, Exception ex, Error.Type type) {
		Error err = new Error ();
	
		if (!Lang.isNullOrEmpty (message)) {
			err.setMsg (message);
		}
		if (ex != null) {
			err.setException (ex);
			if (!Lang.isNullOrEmpty (message)) {
				err.setMsg (ex.getMessage ());
			}
		}
		Logger.error (ErrorUtils.class.getSimpleName () + " --> getError", "Message " + message + " / Exception " + ex);
		
		if (type != null) {
			err.setType (type);
		}
		
		return err;
	}
}