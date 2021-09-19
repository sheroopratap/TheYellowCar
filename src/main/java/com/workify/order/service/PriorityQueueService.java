package com.workify.order.service;

import com.workify.order.constants.OrderType;
import com.workify.order.controller.OrderProcessorController;
import com.workify.order.dto.OrderDTO;
import com.workify.order.generator.IdGeneratorImpl;
import com.workify.order.models.Task;
import com.workify.order.models.Work;
import com.workify.order.queue.WorkOrderQueue;
import com.workify.order.queue.WorkOrderQueueIntf;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PriorityQueueService {
    
    private static final Logger logger = LogManager.getLogger(PriorityQueueService.class);
    
    @Autowired
    private IdGeneratorImpl idGenerator;
    
    private WorkOrderQueueIntf workOrderQ;
    
    
    @PostConstruct
    public void init () {
        workOrderQ = new WorkOrderQueue ();
        logger.info ("initialized work order Q, the magic start from here !!!");
        //TODO: if while shutdown there are some pending order and the Q can be intilize from persistent.
    }
    
    public boolean submitTask ( double id ) {
        try {
            workOrderQ.enqueue (getTaskForID (id));
        } catch (Exception e) {
            logger.error ("Exception occurred while add the task in Q {} ", e.getMessage ());
            return false;
        }
        logger.debug ("Task submitted for oder id {} ", id);
        return true;
    }
    
    public boolean deleteIdFromQ ( double id ) {
        
        try {
            idGenerator.validateId (id);
            Task task = getTaskForID (id);
            workOrderQ.deleteId (task);
        } catch (Exception e) {
            logger.error ("Exception while deleting id {} ", id);
            e.printStackTrace ();
            return false;
        }
        return true;
    }
    
    public OrderDTO deleteHighestRankId () {
        try {
            Task task = workOrderQ.deQueue ();
            logger.debug ("Removed order {} ",task);
            return fromTaskToDto (task);
        } catch (Exception e) {
            throw new NoSuchElementException ("There is order to process");
        }
    }
    
    public Integer getPosition ( double id ) {
        
        try {
            idGenerator.validateId (id);
            Task task = getTaskForID (id);
            return  workOrderQ.getPosition (task);
        } catch (Exception e) {
            return -1;
        }
    }
   
    public long findElapsTimeFromTimeGiven(long time){
        logger.debug ("time supplied in ms {} ", time);
        List<Task> list = workOrderQ.getAllTask ();
        
        if(list.isEmpty ())
            return -1;
        
        LongSummaryStatistics summaryStatistics = list.stream ().mapToLong (value -> (time - value.getStartTime ()) / 1000).summaryStatistics ();
        logger.debug ("Summary Stats {} ", summaryStatistics);
        
        return (long)summaryStatistics.getAverage ();
    }
    
    
    public List<OrderDTO> getAllOrderFromQ () {
        List<Task> list = workOrderQ.getAllTask ();
        Collections.sort (list);
        logger.debug ("Pending Task {} ", list);
        return fromTaskToDto(list);
    }
    
    private Task getTaskForID ( double id ) {
        ImmutablePair<Long, OrderType> pair = idGenerator.generate (id);
        return new Work (pair.getLeft (), pair.getRight ());
    }
    
    private OrderDTO fromTaskToDto ( Task task ) {
        OrderDTO dto = new OrderDTO (task.getId (), task.getStartTime (), task.getRank ());
        return dto;
    }

    private  List<OrderDTO> fromTaskToDto(List<Task> taskList){
        return taskList.stream ().map (task -> new OrderDTO (task.getId (),task.getStartTime (), task.getRank ())).collect (Collectors.toList ());
    }
}
