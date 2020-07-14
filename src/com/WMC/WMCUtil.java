package com.WMC;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WMCUtil {
	private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("[HH:mm:ss]");
	private static final SimpleDateFormat DATETIMESTAMP_FORMAT = new SimpleDateFormat("[yyyy/MM/dd HH:mm:ss]");
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
	private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private static final SimpleDateFormat FILENAME_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	
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
	* @return Current date in form: yyyy-MM-dd
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
	
	/**
	 * For use in log file naming
	* @return Current datetime in form: yyyy-MM-dd-HH-mm-ss
	*/
	public static String getFilenameDateTime() {
		return FILENAME_DATETIME_FORMAT.format(new Date().getTime());
	}
	
	/**
	 * Get an exception's stack trace as a String object
	 * @param e An exception
	 * @return The stack trace of the e parameter as a String
	 */
	public static String stackTraceToString(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}
	
	public static Logger createDefaultLogger(String name) {
		Logger LOGGER = Logger.getLogger(name);
		Handler fileHandler = null;
		try {
			String logFile = "./LOGS/" + getFilenameDateTime() + "_" + name;
			fileHandler = new FileHandler(logFile);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
		LOGGER.addHandler(fileHandler);
		LOGGER.setLevel(Level.ALL);
		
		return LOGGER;
	}
}
