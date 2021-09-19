#Assumptions
1) No persistence considered, orders submitted will be lost once server shuts down.
2) Assuming server and client are in same timezone.
   1) Have a point of override if required.
3) A lightweight traffic, usable internal to an organization.
4) The naming convention of packages, classes used in a way so that they 
    shall not show the project details as given in the guideline. 
   

#Exemptions Required
1) Due to time constraint do not have much test cases but validated all the scenarios.
   1) Can have a discussion to explain the test cases and what more required.
2) The package and classes may show name which may out the context 
   of the project name, considered as per the guidance.
3) Have less exposure of MVC test cases hence some validation used as straight approach.
4) Focused on the base functionalities, some error/exceptions handled in very generic way.


#Approach
1) A custom linklist used to store the incoming messages. New message is always added to the end of the list.
2) To Find the highest priority task, have a pointer to the first task of each type in the Q, since the processing is based
    on time spent in the Q hence need to validate among the first occurrence of each type of order.
    * **Note**: The management IDs always have the precedence and deQed from front of the Q
      since, since order are always added from the rear.
    * Ranks are calculated as per the guidelines given
3) Start time are displayed in the UI for any ID in human-readable format.
4) Logs are available in the project directory logs/app.log location can be change from log4j2.xml configuration.
    A generic name chosen for the logs as per the guidance.

#Points of Override.
1) Timezone considerations, can be added in UI dto (data transfer object).
2) Another type of order can be added by extending the base model **com.workify.order.models.Task**
3) For very High traffic scenario the Q could be replaced with disruptor e.g L-MAX

#Prerequisite 

1) Java 11
2) Maven 3.+

# How to run
Command to build  `` mvn clean compile build``

Command to run  ``mvn spring-boot:run``

#Rest Api 

1) POST: Rest End point to add orders 
   * http://localhost:8080/process/addOrder/{id}
2) DELETE: Rest End point to remove the highest rank order from Q
   * http://localhost:8080/process/deleteTopRankId
3) GET: Rest End point to get all Order sorted from highest ranked to lowest 
    * http://localhost:8080/process/getAllOrders
4) GET: Rest End point to get position of any id from Q start from 0,if the Q is empty it retrun -1
   * http://localhost:8080/process/getPosition/{id}
5) DELETE: Rest End point to delete an id from Q
   * http://localhost:8080/process/deleteId/{id}
6) GET: Rest End point to get the average time spent for all IDs in Q from current time.
    This api doesn't take any argument, it assumes from the current time when the request is initalted.
   * http://localhost:8080/process/getAvgTime