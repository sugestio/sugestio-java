package com.sugestio.client.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class Base {

    private static String longFormat = "yyyy'-'MM'-'dd'T'HH:mm:ss";
    private static String shortFormat = "yyyy'-'MM'-'dd";

    public Base() {
        
    }

    public static String getDateString(int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day, 00, 00, 00);
        DateFormat df = new SimpleDateFormat(shortFormat);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.format(cal.getTime());
    }

    public static String getDateString(long milliseconds) {
        Date date = new Date(milliseconds);
        DateFormat df = new SimpleDateFormat(longFormat);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.format(date);
    }
}
