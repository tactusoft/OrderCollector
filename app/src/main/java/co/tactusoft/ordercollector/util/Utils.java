package co.tactusoft.ordercollector.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by csarmiento
 * 10/06/16
 * csarmiento@gentemovil.co
 */
public class Utils {

    public static String dateToString(Date date, String format) {
        DateFormat df = new SimpleDateFormat(format);
        String result = df.format(date);
        return result;
    }

    public static String dateToString(Date date) {
        return dateToString(date, "yyyy-MM-dd HH:mm");
    }
}
