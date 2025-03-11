package com.sedin.analytics.utility;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtility
{
    public static LocalDateTime atStartOfDay(LocalDateTime localDateTime)
    {
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return startOfDay;
    }
    public static Date atEndOfDay(Date date)
    {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return localDateTimeToDate(endOfDay);
    }

    private static LocalDateTime dateToLocalDateTime(Date date)
    {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private static Date localDateTimeToDate(LocalDateTime localDateTime)
    {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
