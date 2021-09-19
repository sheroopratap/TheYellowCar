package com.workify.order.generator;

import com.workify.order.constants.OrderType;
import com.workify.order.execption.InvalidInputException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

@Service
public class IdGeneratorImpl implements IdGenerator , ClassifyOrderCriteria {
    
    /**
     *
     * @param id
     * @return
     */
    @Override
    public ImmutablePair<Long, OrderType> generate ( double id ) {
        validateId(id);
        return generatePair ((long) id);
    }
    
    /**
     *
     * @param id
     * @throws InvalidInputException
     */
    public void validateId(double id) throws InvalidInputException{
        if(id < 0 ||  id > Long.MAX_VALUE){
           throw new InvalidInputException ("The ID range is invalid should be between 1 to "+Long.MAX_VALUE);
        }
    }
    
    /**
     * get type of the id
     * @param id
     * @return
     * @throws InvalidInputException
     */
    private ImmutablePair< Long, OrderType> generatePair( long id) throws InvalidInputException{
        
        validateId (id);
        
        OrderType  orderType = null;
        if(isManagement (id))
            orderType = OrderType.MANAGEMENT;
        else if(isVip (id))
            orderType = OrderType.VIP;
        else if(isPriorityID (id))
            orderType = OrderType.PRIORITY;
        else if(isNormal (id))
            orderType = OrderType.NORMAL;
        else
            orderType = OrderType.INVALID;
        
        return new ImmutablePair<> (id, orderType);
    }
  
    private boolean isPriorityID(long id){
        return isPriority.test (id);
    }
    
    private boolean isVip(long id){
        return isVip.test (id);
    }
    
    private boolean isManagement(long id){
        return isManagement.test (id);
    }
    
    private boolean isNormal(long id){
        return isNormal.test (id);
    }
    
}
