package com.WMC;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WMCUtil {
	private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("[HH:mm:ss]");
	private static final SimpleDateFormat DATETIMESTAMP_FORMAT = new SimpleDateFormat("[yyyy/MM/dd HH:mm:ss]");
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
	private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	 /**
	 * @return Current time in form: [HH:mm:ss]
	 */
	public static String getTimeStamp() {
		return TIMESTAMP_FORMAT.format(new Date().getTime());
	}
	
	/**
	* @return Current datetime in form: [yyyy/MM/dd HH:mm:ss]
	*/	
	public static String getDateTimeStamp() {
		return DATETIMESTAMP_FORMAT.format(new Date().getTime());
	}
	
	/**
	* @return Current date in form: yyyy/MM/dd
	*/
	public static String getDate() {
		return DATE_FORMAT.format(new Date().getTime());
	}
	
	/**
	* @return Current time in form: HH:mm:ss
	*/
	public static String getTime() {
		return TIME_FORMAT.format(new Date().getTime());
	}
	
	/**
	* @return Current datetime in form: yyyy/MM/dd HH:mm:ss
	*/
	public static String getDateTime() {
		return DATETIME_FORMAT.format(new Date().getTime());
	}
}
