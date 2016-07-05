package co.tactusoft.ordercollector.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.format.Time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by csarmiento
 * 5/07/16
 * csarmiento@gentemovil.co
 */
public class UniqueFileName {

    Context context;
    String fileName;

    public UniqueFileName (Context context) {
        this.context = context;
        Calendar calendar = Calendar.getInstance();
        int miliSecond = calendar.get(Calendar.MILLISECOND);
        int second = calendar.get(Calendar.SECOND);
        int minute = calendar.get(Calendar.MINUTE);
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        int year = calendar.get(Calendar.YEAR);
        fileName = "img_" + miliSecond + second + minute + day + year + android.os.Build.SERIAL + ".jpeg";;
    }

    public String writeToFileRawData(Bitmap bitmap) {
        Utils.saveImage(context, bitmap, fileName);
        return fileName;
    }
}
