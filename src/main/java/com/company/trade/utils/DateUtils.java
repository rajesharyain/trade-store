package com.company.trade.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

    public static String toString(Date date){
        return dateFormatter.format(date);
    }
    public static Date fromString(String date) throws ParseException {
        return dateFormatter.parse(date);
    }
}
