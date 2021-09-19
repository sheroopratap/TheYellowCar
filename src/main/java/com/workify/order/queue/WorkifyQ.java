package com.workify.order.queue;

import com.workify.order.models.Task;
import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;

public class WorkifyQ<E> implements Cloneable, Serializable {
    
    private Node<Task> head;
    private Node<Task> last;
    private int size;
    
    public boolean isEmpty () {
        return size () == 0;
    }
    
    public int size () {
        return size;
    }
    
    public void addLast ( Task data ) {
        linkLast (data);
    }
    
    private void linkLast ( Task data ) {
        
        final Node<Task> l = last;
        final Node<Task> newNode = new Node<Task> (l, data, null);
        last = newNode;
        if (l == null)
            head = newNode;
        else
            l.next = newNode;
        size++;
    }
    
    public Task removeFirst () {
        final Node<Task> f = head;
        if (f == null)
            throw new NoSuchElementException ();
        return unlinkFirst (f);
    }
    
    private Task unlinkFirst ( Node<Task> first ) {
        
        final Task element = first.data;
        final Node<Task> next = first.next;
        first.data = null;
        first.next = null;
        head = next;
        if (next == null)
            last = null;
        else
            next.previous = null;
        size--;
        return element;
    }
    
    public boolean deleteNode(Task  task){
       Node<Task> taskNode = findNodeOnId(task);
       if(taskNode == null)
           return false;
       Task temp =  deleteNode (taskNode);
       return temp != null;
    }
    
    
    public Task deleteNode ( Node<Task> x ) {
        
        final Task element = x.data;
        final Node<Task> next = x.next;
        final Node<Task> prev = x.previous;
        
        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            x.previous = null;
        }
        
        if (next == null) {
            last = prev;
        } else {
            next.previous = prev;
            x.next = null;
        }
        size--;
        return element;
    }
    
    public Task removeLast () {
        final Node<Task> l = last;
        if (l == null)
            throw new NoSuchElementException();
        return unlinkLast (l);
    }
    
    private Task unlinkLast ( Node<Task> l ) {
        final Task element = l.data;
        final Node<Task> prev = l.previous;
        l.data = null;
        l.previous = null; // help GC
        last = prev;
        if (prev == null)
            head = null;
        else
            prev.next = null;
        size--;
        return element;
    }
    
    public Node<Task> findNodeOnId ( Task task ) {
        
        Node<Task> temp = head;
        while (temp != null) {
            if (task.getId () == temp.data.getId ()) {
                return temp;
            }
            temp = temp.next;
        }
        return null;
    }
    
    public Node<Task> findNode ( Predicate<Node> predicate ) {
        
        Node<Task> temp = head;
        while (temp != null) {
            if (predicate.test (temp)) {
                return temp;
            }
            temp = temp.next;
        }
        return null;
    }
    
    public List<Task> getAllElement () {
        
        List<Task> retList = new ArrayList<> ();
        Node<Task> temp = head;
        while (temp != null) {
            try {
                retList.add (temp.data.clone ());
            } catch (Exception e) {
             e.printStackTrace ();
            }
            temp = temp.next;
        }
        return retList;
    }
    
    public long findPosition(Task task){
        long position=-1;
        
        Node<Task> temp = head;
        while (temp != null) {
            if (task.getId () == temp.data.getId ()) {
                return position;
            }
            position++;
            temp = temp.next;
        }
        return position;
    }
}
