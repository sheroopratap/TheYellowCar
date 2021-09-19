package com.workify.order.controller;


import com.workify.order.dto.OrderDTO;
import com.workify.order.service.PriorityQueueService;
import com.workify.order.util.GeneralValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/process")
public class OrderProcessorController {
    
    private static final Logger logger = LogManager.getLogger(OrderProcessorController.class);
    
    @Autowired
    private PriorityQueueService priorityQueueService;
    
    @PostMapping("/addOrder/{id}")
    public ResponseEntity<String> addOrder( @PathVariable double id ){
        logger.info ("processing id for : {}", id);
        boolean isSubmited  = priorityQueueService.submitTask (id);
        return new ResponseEntity<>("Order Submitted Successfully", isSubmited ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @DeleteMapping("/deleteTopRankId")
    public ResponseEntity<OrderDTO> RemoveHighestRankId(){
        try {
            OrderDTO orderDTO = priorityQueueService.deleteHighestRankId ();
            return new ResponseEntity<> (orderDTO, HttpStatus.OK);
        }catch (Exception e){
            logger.error ("No Order to process ");
            return new ResponseEntity<>( new OrderDTO (-1),HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/getAllOrders")
    public ResponseEntity<List<OrderDTO>> getAllOrders(){
        List<OrderDTO>  result = priorityQueueService.getAllOrderFromQ ();
        logger.debug ("All pending orders {} ", result);
        return new ResponseEntity<> (result, HttpStatus.OK);
    }
    
    @GetMapping("/getPosition/{id}")
    @ResponseBody
    public  ResponseEntity<Integer> getPositionOfID(@PathVariable double id){
        int position = priorityQueueService.getPosition (id);
        return new ResponseEntity<> (position,HttpStatus.OK);
    }
    
    @DeleteMapping("/deleteId/{id}")
    public ResponseEntity deleteId(@PathVariable long id){
        boolean isDeleted = priorityQueueService.deleteIdFromQ (id);
        return new ResponseEntity<>("ID "+id+" deleted Successfully", isDeleted ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    /**
     * Assumption:
     * api to calculate the avg waiting time from current time hence the user need to ask only avg waiting time
     * hence this api doesn't accept any argument.
     * @return
     */
    @GetMapping("/getAvgTime")
    public ResponseEntity<String> findAverageProcessTime(){
        try{
            long averageWaitingTime  = priorityQueueService.findElapsTimeFromTimeGiven (System.currentTimeMillis ());
            
            if(averageWaitingTime < 0.0)
                return new ResponseEntity ("There is no pending order to process ", HttpStatus.OK);
            else
                return new ResponseEntity ("Average waiting time is [ "+averageWaitingTime+" ] seconds", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity (e.getMessage (), HttpStatus.BAD_REQUEST);
        }
    }
    
    @Deprecated
    //@GetMapping("/getAvgTime/{time}")
    /**
     * This api was developed on assumption that user could supply a time from where need to calculate the mean time.
     * Since the question was not clear and had time constraint the api was not fully functional
     */
    public ResponseEntity<String> findAverageProcessTime(@PathVariable String time){
        try{
            logger.debug ("Time supplied is {} ", time);
            long averageWaitingTime  = priorityQueueService.findElapsTimeFromTimeGiven (GeneralValidator.formatDateString (time));
           
            if(averageWaitingTime < 0.0){
                return new ResponseEntity ("There is no pending order to process ", HttpStatus.OK);
            }
            else{
                return new ResponseEntity ("Average waiting time is "+averageWaitingTime, HttpStatus.OK);
            }
        }catch (Exception e){
          return new ResponseEntity (e.getMessage (), HttpStatus.BAD_REQUEST);
        }
    }
    
    
}
