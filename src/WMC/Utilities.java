package WMC;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilities {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
	
	public static String getTimeStamp() {
		return sdf.format(new Date().getTime());
	}	
}
