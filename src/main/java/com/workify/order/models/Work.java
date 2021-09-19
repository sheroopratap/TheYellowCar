package com.workify.order.models;

import com.workify.order.constants.OrderType;

import java.util.Objects;


public final class Work extends Task{
    
    private final long id;
    private final long startTime;
    
    public Work( long id, OrderType orderType ){
        this.id = id;
        this.orderType = orderType;
        this.startTime = System.currentTimeMillis ();
    }
    
    public long getId () {
        return id;
    }
    
    public long getStartTime () {
        return startTime;
    }
    
    @Override
    public OrderType getTaskType () {
        return orderType;
    }
   
    @Override
    public String toString () {
        return "Work{" +
                "id=" + id +
                ", startTime=" + startTime + ", Type="+this.orderType+ ", CurrentRank="+this.getCurrentRank ()+
                '}';
    }
    
    
    @Override
    public boolean equals ( Object o ) {
        if (this == o) return true;
        if (o == null || getClass () != o.getClass ()) return false;
        Work work = (Work) o;
        return id == work.id ;
    }
    
    @Override
    public int hashCode () {
        return Objects.hash (id, startTime);
    }
}
