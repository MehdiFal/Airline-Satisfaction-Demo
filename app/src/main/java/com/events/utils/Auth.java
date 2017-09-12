package com.events.utils;

import org.apache.commons.codec.binary.Base64;

public class Auth {
	
	public static String basic (String userName, String password) {
		String authString 		= userName + ":" + password;
		
		byte [] authEncBytes 	= new Base64 ().encode (authString.getBytes ());
		
		return "Basic " + new String (authEncBytes);
	}
}