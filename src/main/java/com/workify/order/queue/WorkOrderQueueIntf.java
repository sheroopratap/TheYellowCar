package com.workify.order.queue;

import com.workify.order.models.Task;

import java.util.List;
import java.util.NoSuchElementException;

public interface WorkOrderQueueIntf {
    
    /**
     * api to submit the task
     * @param task
     */
    void enqueue( Task task );
    
    /**
     * api to get and remove the task
     * @return
     * @throws NoSuchElementException
     */
    Task deQueue() throws NoSuchElementException;
    
    /**
     * api to get all task in sorted order
     * @return
     */
    List<Task> getAllTask();
    
    /**
     * api to delete the specific id
     * @param id
     * @return
     */
    boolean deleteId(long id);
    
    /**
     * api to delete the spcific task
     * @param task
     * @return
     */
    boolean deleteId(Task task);
    
    /**
     * api to get the current position of the task
     * @param task
     * @return
     */
    Integer getPosition(Task task);
    
}
