package com.mohistmc.ai.live.bilibili.entry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LiveBotUtils {

    public static boolean isBelong() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置日期格式
        Date now = null;
        Date beginTime = null;
        Date endTime = null;
        try {
            now = df.parse(df.format(new Date()));
            beginTime = df.parse("01:00");
            endTime = df.parse("6:00");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return belongCalendar(now, beginTime, endTime);
    }

    /**
     * 判断时间是否在时间段内
     */
    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        return date.after(begin) && date.before(end);
    }
}
