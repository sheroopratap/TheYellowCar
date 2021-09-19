package com.workify.order.generator;

import java.util.function.Predicate;

public interface ClassifyOrderCriteria {
    /**
     * function to identify priority id
     */
    Predicate<Long> isPriority = id -> id % 3 == 0;
    
    /**
     * function to identify VIP id
     */
    Predicate<Long> isVip = id -> id % 5 == 0;
    
    /**
     * function to identify management id
     */
    Predicate<Long> isManagement = id -> id % 3 == 0 && id % 5 == 0  ;
    
    /**
     * function to identify normal id
     */
    Predicate<Long> isNormal = id -> id % 3 != 0 ||  id % 5 != 0  ;
    
}
