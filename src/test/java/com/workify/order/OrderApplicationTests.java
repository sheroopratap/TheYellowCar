package com.workify.order;

import com.workify.order.controller.OrderProcessorController;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OrderApplicationTests {
    
    private static String BASE_URL="http://localhost:8080/process";
    @Autowired
    private OrderProcessorController controller;
    
    @Autowired
    private MockMvc mockMvc;
    
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    
    public OrderApplicationTests(){
    
    }
    
    @Before
    public void beforeTest(){
        mockMvc = MockMvcBuilders.standaloneSetup (controller).build ();
        
    }
    @After
    public void aferTest(){
    }
    @Test
    public void shouldAddOrder() throws Exception {
        this.mockMvc.perform(post (BASE_URL+"/addOrder/10")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("Order Submitted Successfully"));
    }
    
    @Test
    public void shouldDeleteTopId() throws Exception {
    
        this.mockMvc.perform(post (BASE_URL+"/addOrder/1")).andExpect(status().isOk());
        this.mockMvc.perform(post (BASE_URL+"/addOrder/5")).andExpect(status().isOk());
        this.mockMvc.perform(post (BASE_URL+"/addOrder/15")).andExpect(status().isOk());
        //15 is the mgmt id shall delete 15 first
        String responseBody = this.mockMvc.perform(delete (BASE_URL+"/deleteTopRankId/")).andReturn ().getResponse ().getContentAsString ();
        Assert.assertTrue (responseBody.contains ("\"id\":15.0"));
    }
    
    @Test
    public void testGetAllOrders() throws Exception {
        
        this.mockMvc.perform(post (BASE_URL+"/addOrder/1")).andExpect(status().isOk());
        this.mockMvc.perform(post (BASE_URL+"/addOrder/5")).andExpect(status().isOk());
        this.mockMvc.perform(post (BASE_URL+"/addOrder/15")).andExpect(status().isOk());
        //15 is the mgmt id shall delete 15 first
        String responseBody = this.mockMvc.perform(get (BASE_URL+"/getAllOrders/")).andReturn ().getResponse ().getContentAsString ();
        Assert.assertTrue (responseBody.contains ("\"id\":15.0"));
        Assert.assertTrue (responseBody.contains ("\"id\":5.0"));
        Assert.assertTrue (responseBody.contains ("\"id\":1.0"));
    }
    
    @Test
    public void testPosition() throws Exception {
    
       
        this.mockMvc.perform(post (BASE_URL+"/addOrder/1")).andExpect(status().isOk());
        this.mockMvc.perform(post (BASE_URL+"/addOrder/5")).andExpect(status().isOk());
        this.mockMvc.perform(post (BASE_URL+"/addOrder/15")).andExpect(status().isOk());
        //15 is the mgmt id shall delete 15 first
        this.mockMvc.perform(get (BASE_URL+"/getPosition/15")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("1"));
        this.mockMvc.perform(get (BASE_URL+"/getPosition/1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("2"));
    
    }
    
    @Test
    public void testDeleteID() throws Exception {
    
        this.mockMvc.perform(post (BASE_URL+"/addOrder/1")).andExpect(status().isOk());
        this.mockMvc.perform(post (BASE_URL+"/addOrder/5")).andExpect(status().isOk());
        this.mockMvc.perform(post (BASE_URL+"/addOrder/15")).andExpect(status().isOk());
        //15 is the mgmt id shall delete 15 first
        this.mockMvc.perform(delete (BASE_URL+"/deleteId/15")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("ID 15 deleted Successfully"));
    
        String responseBody = this.mockMvc.perform(get (BASE_URL+"/getAllOrders/")).andReturn ().getResponse ().getContentAsString ();
        Assert.assertFalse (responseBody.contains ("\"id\":15.0"));
    }
    
    @Test
    public void testAvgTime() throws Exception {
        this.mockMvc.perform(post (BASE_URL+"/addOrder/1")).andExpect(status().isOk());
        this.mockMvc.perform(post (BASE_URL+"/addOrder/5")).andExpect(status().isOk());
        this.mockMvc.perform(post (BASE_URL+"/addOrder/15")).andExpect(status().isOk());
        
        String response = this.mockMvc.perform(get (BASE_URL+"/getAvgTime/")).andReturn ().getResponse ().getContentAsString ();
        //the response will be dynamic hence cannot assert with a specific number
        Assert.assertTrue (response.contains ("Average waiting time is"));
    }

    @After
    public void cleanUP() throws Exception{
        //if present
        this.mockMvc.perform(delete (BASE_URL+"/deleteId/1"));
        this.mockMvc.perform(delete (BASE_URL+"/deleteId/5"));
        this.mockMvc.perform(delete (BASE_URL+"/deleteId/15"));
    
    }
}
