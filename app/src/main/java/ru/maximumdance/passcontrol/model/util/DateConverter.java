package ru.maximumdance.passcontrol.model.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static String toString(Date date){
        if (date==null){
            return null;
        }
        return simpleDateFormat.format(date);
    }

    public static Date fromString(String date) throws ParseException {
        if (date==null){
            return null;
        }
        return simpleDateFormat.parse(date);
    }

}
