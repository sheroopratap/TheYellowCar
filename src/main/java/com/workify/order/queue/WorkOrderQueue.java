package com.workify.order.queue;

import com.workify.order.constants.OrderType;
import com.workify.order.models.Task;
import com.workify.order.service.PriorityQueueService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class WorkOrderQueue implements WorkOrderQueueIntf {
    
    private static final Logger logger = LogManager.getLogger(WorkOrderQueue.class);
    
    private static final WorkifyQ<Task> workOrderQ = new WorkifyQ<> ();
    private Map<OrderType, Node<Task>> pointerToTop = new ConcurrentHashMap<> ();
    
    public WorkOrderQueue () {
    }
    
    @Override
    public void enqueue ( Task task ) {
      
        synchronized (workOrderQ) {
            workOrderQ.addLast (task);
            pointToTopTask (task);
        }
        logger.debug ("Task submitted {}  and Q size {} ", task, workOrderQ.size ());
    }
    
    @Override
    public Task deQueue () throws NoSuchElementException {
        
        synchronized (workOrderQ) {
            if (!workOrderQ.isEmpty ()) {
                return findHighestPriorityTask ();
            }
        }
        throw new NoSuchElementException ();
    }
    
    @Override
    public List<Task> getAllTask () {
       return  workOrderQ.getAllElement ();
    }
    
    @Override
    public boolean deleteId ( long id ) {
        return false;
    }
    
    @Override
    public boolean deleteId ( Task task ) {
    
        synchronized (workOrderQ) {
            if(workOrderQ.deleteNode (task)){
                removeFromMap(task);
                pointToTopTask (task);
                return true;
            }else{
                return false;
            }
        }
    }
    
    @Override
    public Integer getPosition ( Task task ) {
        List<Task> list = workOrderQ.getAllElement ();
        Collections.sort (list);
        logger.debug ("Get Position from sorted order {} ", list);
        return  list.indexOf (task);
    }
    
    private void removeFromMap( Task task){
        if(pointerToTop.containsKey (task.getTaskType ())){
    
            boolean shallDelete=false;
           for(Map.Entry<OrderType, Node<Task>> entry : pointerToTop.entrySet ()){
                if(entry.getValue ().data.getTaskType () == task.getTaskType ()
                        && entry.getValue ().data.getId () == task.getId ())
                    shallDelete = true;
           }
            if(shallDelete){
                pointerToTop.remove (task.getTaskType ());
            }
        }
        logger.debug ("Pointer Map after removing task {} Map {}",task,pointerToTop);
    }
    private void pointToTopTask ( Task task ) {
        
        if (task == null)
            return;
        if (pointerToTop.containsKey (task.getTaskType ()))
            return; // everything is well and good, we have the top task as pointer.
        
        Predicate<Node> predicate = ( node ) -> node.data.getTaskType ().equals (task.getTaskType ());
        Node<Task> currentNode = workOrderQ.findNode (predicate);
        if (currentNode != null){
            pointerToTop.put (currentNode.data.getTaskType (), currentNode);
        }
       
        logger.debug ("Pointer map {} ", pointerToTop);
    }
    
    
    public Task findHighestPriorityTask () throws NoSuchElementException {
        
        double rank = Integer.MAX_VALUE;
        Node<Task> refToDelete = null;
        
        if(pointerToTop.containsKey (OrderType.MANAGEMENT)){
            refToDelete = pointerToTop.get (OrderType.MANAGEMENT);
        }else {
            for (Map.Entry<OrderType, Node<Task>> entry : pointerToTop.entrySet ()) {
                if (entry.getValue ().data.getRank () < rank) {
                    rank = entry.getValue ().data.getRank ();
                    refToDelete = entry.getValue ();
                }
            }
        }
        
        if (refToDelete == null)
            throw new NoSuchElementException ();
        
        synchronized (workOrderQ) {
            //remove the task from the Q
            Task task = workOrderQ.deleteNode (refToDelete);
            //remove the type from the map
            pointerToTop.remove (task.getTaskType ());
            // then point the next immediate task for same type;
            pointToTopTask (task);
            return task;
        }
        
    }
}
