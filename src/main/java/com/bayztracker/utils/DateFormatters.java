package com.bayztracker.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateFormatters

{
    public static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
    return Instant.ofEpochMilli(dateToConvert.getTime())
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
}
}
