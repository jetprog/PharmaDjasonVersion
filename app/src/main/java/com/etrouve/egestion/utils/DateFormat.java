package com.etrouve.egestion.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ingdjason on 12/20/16.
 */

public class DateFormat {
    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        // Ex: 21-Nov-2016 11h37 AM
        //sdf.applyPattern("dd-MMM-yyyy hh'h'mm a");
        sdf.applyPattern("dd-MMM-yyyy");

        if (date != null)
            return sdf.format(date.getTime());

        return "now";
    }
}
