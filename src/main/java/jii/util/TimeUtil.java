package jii.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimeUtil {

    public static final SimpleDateFormat YMD = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat YMD_HIS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getSpendTime(long a) {
        return (System.currentTimeMillis() - a) + " ms";
    }

    public static String int2strZ(int ts) {
        LocalDateTime dt = LocalDateTime.ofEpochSecond(ts, 0, ZoneOffset.ofHours(0));
        return DateTimeFormatter.ISO_DATE_TIME.format(dt) + "Z";
    }

    public static int strZ2int(String sz) {
        LocalDateTime dt = LocalDateTime.parse(sz, DateTimeFormatter.ISO_DATE_TIME);
        return (int)(dt.toInstant(ZoneOffset.UTC).toEpochMilli() / 1000);
    }

    public static String date2Ymd(Date a) {
        return YMD.format(a);
    }

    public static String date2YmdHis(Date a) {
        return YMD_HIS.format(a);
    }

}
