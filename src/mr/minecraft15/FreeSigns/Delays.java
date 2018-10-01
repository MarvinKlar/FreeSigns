package mr.minecraft15.FreeSigns;

import java.util.HashMap;

public class Delays {
    public static HashMap<String, Long> delays = new HashMap<String, Long>();

    public static void registerDelay(String key) {
	delays.put(key, System.currentTimeMillis());
    }

    public static boolean haveToWait(String key, int seconds) {
	if (Delays.delays.containsKey(key)) {
	    if ((System.currentTimeMillis() - Delays.delays.get(key)) / 1000 >= seconds) {
		Delays.delays.remove(key);
	    }
	}
	return Delays.delays.containsKey(key);
    }

    public static String getRemainingTime(String key, int seconds) {
	return getTime(seconds - (System.currentTimeMillis() - Delays.delays.get(key)) / 1000);
    }

    public static String getTime(long seconds) {
	int sec = 0, min = 0, h = 0, d = 0, w = 0, m = 0, y = 0;
	for (int i = 0; i < seconds; ++i) {
	    ++sec;
	    if (sec >= 60) {
		min++;
		sec = 0;
	    }
	    if (min >= 60) {
		h++;
		min = 0;
	    }
	    if (h >= 24) {
		d++;
		h = 0;
	    }
	    if (d >= 7) {
		w++;
		d = 0;
	    }
	    if (w >= 4) {
		m++;
		w = 0;
	    }
	    if (m >= 12) {
		y++;
		m = 0;
	    }
	}
	String secStr = (sec != 0) ? sec + " second" + ((sec == 1) ? "" : "s") : null,
		minStr = (min != 0) ? min + " minute" + ((min == 1) ? "" : "s") : null,
		hStr = (h != 0) ? h + " hour" + ((h == 1) ? "" : "s") : null,
		dStr = (d != 0) ? d + " day" + ((d == 1) ? "" : "s") : null,
		wStr = (w != 0) ? w + " week" + ((w == 1) ? "" : "s") : null,
		mStr = (m != 0) ? m + " month" + ((m == 1) ? "" : "s") : null,
		yStr = (y != 0) ? y + " year" + ((y == 1) ? "" : "s") : null;
	String r = (yStr == null ? "" : yStr + " ") + (mStr == null ? "" : mStr + " ")
		+ (wStr == null ? "" : wStr + " ") + (dStr == null ? "" : dStr + " ") + (hStr == null ? "" : hStr + " ")
		+ (minStr == null ? "" : minStr + " ") + (secStr == null ? "" : secStr + " ");
	return r.substring(0, r.length() - 1);
    }
}
