package com.workify.order.models;

import com.workify.order.constants.OrderType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;

public abstract class Task implements Cloneable, Serializable, Comparable<Task>{
    
    private static final Logger logger = LogManager.getLogger(Task.class);
    
    protected OrderType orderType;
    private long currentRank;
    
    /**
     * get the task type
     * @return
     */
    public abstract OrderType getTaskType ();
    
    /**
     * get the time when task was submitted.
     * @return
     */
    public abstract long getStartTime ();
    
    /**
     * get the id
     * @return
     */
    public abstract long getId();
    
    /**
     * the current rank when this api is called
     * @return
     */
    public double getCurrentRank () {
        return currentRank;
    }
    
    /**
     * calculate the Rank
     * @return
     */
    public final long getRank () {
        currentRank = calculateRank();
        return currentRank;
    }
    
    /**
     * main api to calculate the Rank
     * @return
     */
    public final long calculateRank (){
        return RankCalculator.getRank (this.orderType, this);
    }
    
    @Override
    public Task clone () throws CloneNotSupportedException {
        return (Task) super.clone ();
    }
    
    public long getTimeElapsed(){
       return   (System.currentTimeMillis () - getStartTime ()) / 1000;
    }
    
    /**
     * to sort the task based on Rank
     * @param that
     * @return
     */
    @Override
    public int compareTo ( Task that ) {
        
        if(this.getRank () < that.getRank ())
            return -1;
        else if(this.getRank () > that.getRank ())
            return 1;
        else {
            if(this.getTimeElapsed () < that.getTimeElapsed ())
                return -1;
            else if(this.getTimeElapsed () > that.getTimeElapsed ())
                return 1;
            else
                return 0;
        }
    }
}
