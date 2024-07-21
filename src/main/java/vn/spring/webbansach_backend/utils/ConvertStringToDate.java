package vn.spring.webbansach_backend.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class ConvertStringToDate {
    public static Date convert(String date){
        System.out.println("Date"+date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date dateUtil = null;
        try{
            dateUtil = sdf.parse(date);
        }catch(Exception e){
            throw new RuntimeException(("Không thể convert string sang Date!"));
        }
        return new Date(dateUtil.getTime());
    }
}
