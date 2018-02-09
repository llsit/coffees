package com.cmu.nuts.coffee9.utillity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by tcdm053 on 1/2/2561.
 */

public class TimeManager {

    public TimeManager() {
    }

    public String epochConverter(Long epoch){
        if(epoch != null){
            Date date = new Date(epoch);
            DateFormat format = new SimpleDateFormat("EEE, d MMM yyyy");
            format.setTimeZone(TimeZone.getTimeZone("GMT/UTC"));
            String formatted = format.format(date);
            System.out.println(formatted);
            format.setTimeZone(TimeZone.getTimeZone("Thailand/Bangkok"));
            formatted = format.format(date);
            return formatted;
        }else {
            return "something wrong!";
        }
    }
}
