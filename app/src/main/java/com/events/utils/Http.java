package com.events.utils;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class Http {

    private static OkHttpClient HttpClient;

    public static OkHttpClient client () {
        if (HttpClient == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor ();
            logging.setLevel (HttpLoggingInterceptor.Level.BODY);
            
            HttpClient = new OkHttpClient.Builder ()
                .addInterceptor (logging)
                .build ();
		}
		
        return HttpClient;
    }
}