package com.workify.order.generator;

import com.workify.order.constants.OrderType;
import com.workify.order.execption.InvalidInputException;
import org.apache.commons.lang3.tuple.ImmutablePair;

public interface IdGenerator {
    
    /**
     * method to generate the type based on id provided
     * @param id
     * @return
     */
    ImmutablePair<? extends Number, OrderType> generate( double id);
    
    /**
     * api to validate the id
     * @param id
     * @throws InvalidInputException
     */
    void validateId(double id) throws InvalidInputException;
    
}
