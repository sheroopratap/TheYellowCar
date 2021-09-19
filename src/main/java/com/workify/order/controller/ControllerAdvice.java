package com.workify.order.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {
    
   private static final Logger logger = LogManager.getLogger(ControllerAdvice.class);
   
  @ExceptionHandler(Exception.class)
  public void handleException(Exception e){
      logger.error ("Error occurred ",e);
  }

}



