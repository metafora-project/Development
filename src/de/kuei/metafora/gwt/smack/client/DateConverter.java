package de.kuei.metafora.gwt.smack.client;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;

public class DateConverter extends DateTimeFormat {
	String time;
	Date date;
	String cdate;
	TimeZone tz;
	long currentTime;

	public DateConverter(String time) {
		super("");
		this.time = time;
	}

	public String convertTime() {
		long xmlTime = Long.parseLong(time);
		date = new Date(xmlTime);
		currentTime = System.currentTimeMillis();
		long offset = currentTime - xmlTime;
		
		long aDay = 1000l * 60l * 60l * 24l;
		long aWeek = aDay * 7l;
		if (offset < aDay) {
			cdate = DateTimeFormat.getFormat("HH:mm ', 'z").format(date);
		} else {
			if (aDay <= offset && offset < aWeek) {
				int day = date.getDay();
				if (day == 1) {
					cdate = "Monday";
				}
				if (day == 2) {
					cdate = "Tuesday";
				}
				if (day == 3) {
					cdate = "Wednesday";
				}
				if (day == 4) {
					cdate = "Thursday";
				}
				if (day == 5) {
					cdate = "Friday";
				}
				if (day == 6) {
					cdate = "Saturday";
				}
				if (day == 0) {
					cdate = "Sunday";
				}
			} else {
				cdate = this.getLongDateFormat().format(date);
			}
		}

		return cdate;
	}
}
