package com.workify.order.dto;

import com.workify.order.util.GeneralValidator;

import java.io.Serializable;

public class OrderDTO implements Serializable {
    
    private double id;
    /**
     * as of now this is String
     * TODO: if timezone needs to consider the it should be in long value in UTC
     * TODO: and its UI responsibility to parse the time val in client timezone.
     *
     */
    private String startTime;
    private long rank;
    
    public OrderDTO ( double id ) {
        this.id = id;
    }
    
    public OrderDTO ( double id, long startTime ) {
        this.id = id;
        /**
         * assumption for String: its timezone irrespective. If need to consider the timezone read the note on the top.
         */
        this.startTime = GeneralValidator.getFormatedDateString (startTime);
    }
    
    public OrderDTO ( double id, long startTime, long rank ) {
        this.id = id;
        /**
         * assumption for String: its timezone irrespective. If need to consider the timezone read the note on the top.
         */
        this.startTime = GeneralValidator.getFormatedDateString (startTime);
        this.rank = rank;
    }
    
    public double getId () {
        return id;
    }
    
    public String getStartTime () {
        return startTime;
    }
    
    public double getRank () {
        return rank;
    }
}
