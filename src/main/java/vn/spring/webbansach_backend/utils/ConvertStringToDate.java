package vn.spring.webbansach_backend.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConvertStringToDate {
    public static Date convert(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date dateUtil = null;
        try{
            dateUtil = sdf.parse(date);
        }catch(Exception e){
            throw new RuntimeException(("Không thể convert string sang Date!"));
        }
        return new Date(dateUtil.getTime());
    }

    public static LocalDateTime convertToLocalDateTime(String date){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        try{
            return  LocalDateTime.parse(date,dtf);
        }catch(Exception e){
            throw new RuntimeException(("Không thể convert string sang Date!"));
        }
    }
}
