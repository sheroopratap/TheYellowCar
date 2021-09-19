package com.workify.order.queue;

import com.workify.order.models.Task;

import java.util.Objects;

public final class Node<E extends Task>{
    
    E data;
    Node<E> next;
    Node<E> previous;
    
    Node( Node<E> prev, E data, Node<E> next){
        this.previous = prev;
        this.data = data;
        this.next = next;
    }
}
