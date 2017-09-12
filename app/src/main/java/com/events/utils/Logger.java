package com.events.utils;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class Logger {
	
	private enum Level {
		Debug,
		Info,
		Warning,
		Error,
		Severe
	}
	
	private final static Map<Level, Integer> Levels = new HashMap<Level, Integer> ();
	static {
		Levels.put (Level.Debug, 0);
		Levels.put (Level.Info, 1);
		Levels.put (Level.Warning, 2);
		Levels.put (Level.Error, 3);
		Levels.put (Level.Severe, 4);
	}
	
	private static Level currentLevel = Level.Debug;
	
	public static void debug (String where, Object message) {
		if (Levels.get (Level.Debug) < Levels.get (currentLevel)) {
			return;
		}
		if (message == null) {
			return;
		}
		Log.d (where, message.toString ());
	}
	
	public static void info (String where, Object message) {
		if (Levels.get (Level.Info) < Levels.get (currentLevel)) {
			return;
		}
		if (message == null) {
			return;
		}
		Log.i (where, message.toString ());
	}
	
	public static void warning (String where, Object message) {
		if (Levels.get (Level.Warning) < Levels.get (currentLevel)) {
			return;
		}
		if (message == null) {
			return;
		}
		Log.w (where, message.toString ());
	}
	
	public static void error (String where, Object message) {
		if (Levels.get (Level.Error) < Levels.get (currentLevel)) {
			return;
		}
		if (message == null) {
			return;
		}
		
		if (message instanceof Exception) {
			((Exception)message).printStackTrace ();
			return;
		}
		
		Log.e (where, message.toString ());
	}
	
	public static void severe (String where, Object message) {
		if (Levels.get (Level.Severe) < Levels.get (currentLevel)) {
			return;
		}
		if (message == null) {
			return;
		}
		Log.wtf (where, message.toString ());
	}
	
	public static void severe (String where, Throwable throwable) {
		if (Levels.get (Level.Severe) < Levels.get (currentLevel)) {
			return;
		}
		if (throwable == null) {
			return;
		}
		Log.wtf (where, throwable);
	}
}