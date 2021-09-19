package com.workify.order.util;

import com.workify.order.execption.InvalidInputException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GeneralValidator {
    
    public static final String SUPPORTED_DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SUPPORTED_DATE_FORMAT);
    
    /**
     * utility api to convert and validate the provide date string.
     * TODO: as of now not used
     * @param dateString
     * @return
     * @throws ParseException
     * @throws InvalidInputException
     */
    public static synchronized long formatDateString(String dateString) throws ParseException, InvalidInputException {
    
        try {
            Date date = simpleDateFormat.parse (dateString);
            if( System.currentTimeMillis () - date.getTime () < 0  )
                throw new InvalidInputException("The provided time in more that the present time");
            
            return date.getTime ();
        }catch (ParseException e){
            throw  new ParseException("Provided Date format is not supported \n " +
                    "Supported format is "+GeneralValidator.SUPPORTED_DATE_FORMAT,1);
        }
    }
    
    /**
     * api to get human readable date string.
     * TODO: as of now the timezone is not considered
     * @param time
     * @return
     */
    public static synchronized String getFormatedDateString(long time){
       return simpleDateFormat.format (new Date (time));
    }
}
