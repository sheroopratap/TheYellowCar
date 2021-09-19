package com.workify.order.models;

import com.workify.order.constants.OrderType;
import com.workify.order.controller.OrderProcessorController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class RankCalculator {
    
    private static final Logger logger = LogManager.getLogger(RankCalculator.class);
    
    private static Map<OrderType, Function<Task, Double>> typeFunctionMap = new ConcurrentHashMap<> ();
    
    static {
        Function<Task, Double> normalIdFormula = ( task ) -> {
            long l =  task.getTimeElapsed ();
            if(l < 2)
                 l = 2L;
            return (double) l;
        };
        
        typeFunctionMap.put (OrderType.NORMAL, normalIdFormula);
        
        Function<Task, Double> priorityID = ( task -> {
            long timeinSec = task.getTimeElapsed ();
            return Math.max (3, timeinSec * Math.log (timeinSec));
        } );
        typeFunctionMap.put (OrderType.PRIORITY, priorityID);
    
        Function<Task, Double> vipID = ( task -> {
            long timeinSec = task.getTimeElapsed ();
            return Math.max (4, ( 2 * timeinSec ) * Math.log (timeinSec));
        } );
        typeFunctionMap.put (OrderType.VIP, vipID);
    
        Function<Task, Double> managmentId = (task -> 1D); //assuming the highest priority ID
        typeFunctionMap.put (OrderType.MANAGEMENT, managmentId);
        
    }
    
    /**
     * api to calculate rank of the task
     * @param orderType
     * @param task
     * @return
     */
    public static synchronized long getRank ( OrderType orderType, Task task ) {
        if (!typeFunctionMap.containsKey (orderType))
              return Integer.MAX_VALUE; // if it is not in the map then lets deprioritize  it;
        
        Function<Task, Double> function = typeFunctionMap.get (task.getTaskType ());
        double rank = function.apply (task);
        return (long) rank;
    }
}
