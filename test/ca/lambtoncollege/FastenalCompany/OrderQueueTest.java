/*
 * Copyright 2015 Len Payne <len.payne@lambtoncollege.ca>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ca.lambtoncollege.FastenalCompany;

import ca.lambtoncollege.fastenalcompany.Order;
import ca.lambtoncollege.fastenalcompany.OrderQueue;
import ca.lambtoncollege.fastenalcompany.OrderQueue.NoCustomerException;
import ca.lambtoncollege.fastenalcompany.OrderQueue.NoPurchasesException;
import ca.lambtoncollege.fastenalcompany.OrderQueue.NoTimeProcessedException;
import ca.lambtoncollege.fastenalcompany.OrderQueue.NoTimeReceviedException;
import ca.lambtoncollege.fastenalcompany.Purchase;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author JIAJUN XUE <nicoxue0324@gmail.com>
 */
public class OrderQueueTest {
    
    public OrderQueueTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testWhenCustomerExistsAndPurchasesExistThenTimeReceivedIsNow() {
        OrderQueue orderQueue = new OrderQueue();
        Order order = new Order("CUST00001", "ABC Construction");
        order.addPurchase(new Purchase("PROD0004", 450));
        order.addPurchase(new Purchase("PROD0006", 250));
        orderQueue.add(order);
        
        long expResult = new Date().getTime();
        long result = order.getTimeReceived().getTime();
        assertTrue(Math.abs(result - expResult) < 1000);
    }

    
    @Test
    public void testWhenNoListPurchasesThenThrowException() {
        OrderQueue orderQueue = new OrderQueue();
        Order order = new Order("CUST00001", "ABC Construction");
        if (order.getListOfPurchases() == null) {
           throw new EmptyStackException();
        };
            
    }
    
    @Test
    public void testWhenCustomerIdAndCustomerNameNotExistThenThrowException() {
        OrderQueue orderQueue = new OrderQueue();
        Order order = new Order();
        order.addPurchase(new Purchase("1", 1));
        boolean whetherThrow=false;
        try{
           orderQueue.arrive(order);
        }catch(NoCustomerException ex){
            whetherThrow=true;
        }
        assertTrue(whetherThrow);
    }
    
    @Test
    public void testOrderArrivedNoPurchasesThenThrowException() {
        OrderQueue orderQueue = new OrderQueue();
        Order order = new Order("CUST00001", "ABC Construction");
        boolean whetherThrow=false;
        try{
           orderQueue.arrive(order);
        }catch(NoPurchasesException ex){
            whetherThrow=true;
        }
        assertTrue(whetherThrow);
    }

    @Test
    public void testWhenRequestForNextOrderThenReturnEaliestOrder() {
        OrderQueue orderQueue = new OrderQueue();
        Order earliest = null;
        for(int i=1;i<10;i++){
            Order order = new Order("CUST0000"+i, "ABC Construction"+i);
            order.addPurchase(new Purchase("%d"+i, i));
            orderQueue.arrive(order);
            if(i==1)earliest=order;
        }
        Order order = orderQueue.next();
        assertTrue(earliest.equals(order));
    }
    
    @Test
    public void testWhenNoNextOrderThenReturnNull() {
        OrderQueue orderQueue = new OrderQueue();
        Order order = orderQueue.next();
        assertTrue(order==null);
    }
    
    @Test
    public void testWhenOrderProcessThenSetTheTimeProcessedToNow() {
        OrderQueue orderQueue = new OrderQueue();
        for(int i=1;i<10;i++){
            Order order = new Order("CUST0000"+i, "ABC Construction"+i);
            order.addPurchase(new Purchase("%d"+i, i));
            orderQueue.arrive(order);
        }
        Order order = orderQueue.next();
        orderQueue.process(order);
        assertTrue(order.getTimeProcessed().equals(new Date()));
    }
    
    @Test
    public void testWhenOrderProcessNoTimeReceviedThenThrowException() {
        OrderQueue orderQueue = new OrderQueue();
        for(int i=1;i<10;i++){
            Order order = new Order("CUST0000"+i, "ABC Construction"+i);
            order.addPurchase(new Purchase("%d"+i, i));
            orderQueue.arrive(order);
        }
        Order order = orderQueue.next();
        order.setTimeReceived(null);
        boolean whetherThrow=false;
        try{
           orderQueue.process(order);
        }catch(NoTimeReceviedException ex){
            whetherThrow=true;
        }
        assertTrue(whetherThrow);
    }
    
    @Test
    public void testWhenOrderFulfillThenSetTheTimeFulfilledToNow() {
        OrderQueue orderQueue = new OrderQueue();
        for(int i=1;i<10;i++){
            Order order = new Order("CUST0000"+i, "ABC Construction"+i);
            order.addPurchase(new Purchase("%d"+i, i));
            orderQueue.arrive(order);
        }
        Order order = orderQueue.next();
        orderQueue.process(order);
        orderQueue.fulfill(order);
        assertTrue(order.getTimeFulfilled().equals(new Date()));
    }
    
    @Test
    public void testWhenOrderFulfillNoTimeProcessedThenThrowException() {
        OrderQueue orderQueue = new OrderQueue();
        for(int i=1;i<10;i++){
            Order order = new Order("CUST0000"+i, "ABC Construction"+i);
            order.addPurchase(new Purchase("%d"+i, i));
            orderQueue.arrive(order);
        }
        Order order = orderQueue.next();
        boolean whetherThrow=false;
        try{
           orderQueue.fulfill(order);
        }catch(NoTimeProcessedException ex){
            whetherThrow=true;
        }
        assertTrue(whetherThrow);
    }
    
    @Test
    public void testWhenOrderFulfillNoTimeReceviedThenThrowException() {
        OrderQueue orderQueue = new OrderQueue();
        for(int i=1;i<10;i++){
            Order order = new Order("CUST0000"+i, "ABC Construction"+i);
            order.addPurchase(new Purchase("%d"+i, i));
            orderQueue.arrive(order);
        }
        Order order = orderQueue.next();
        orderQueue.process(order);
        order.setTimeReceived(null);
        boolean whetherThrow=false;
        try{
           orderQueue.fulfill(order);
        }catch(NoTimeReceviedException ex){
            whetherThrow=true;
        }
        assertTrue(whetherThrow);
    }
    
    @Test
    public void testOrderReportWithNoOrders() {
        OrderQueue orderQueue = new OrderQueue();
        String report = orderQueue.report();
        System.out.println(report);
        assertTrue(report.equals(""));
    }
    
    @Test
    public void testOrderReportToJsonWithOrders() {
        OrderQueue orderQueue = new OrderQueue();
        for(int i=1;i<7;i++){
            Order order = new Order("CUST0000"+i, "ABC Construction"+i);
            order.addPurchase(new Purchase("%d"+i, i));
            orderQueue.arrive(order);        
        }
        Order order = orderQueue.next();
        orderQueue.process(order);
        orderQueue.fulfill(order);
        String report = orderQueue.report();
        String expResult = "{\"orders\":[{customerId : CUST00006, customerName : ABC Construction6,"
                + " timeRecevied : "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())
                +", timeProcessed : , timeFulfilled : , purchase : [{ productID : 2, quantity : 2 }],"
                + " note : },{customerID : C00001, customerName : Fast and Light1, timeRecevied : "
                +new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())
                +", timeProcessed : "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())
                +", timeFulfilled : "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())
                +", purchase : [{ productID : 1, quantity : 1 }], note : }]}";
        System.out.println(expResult);
        System.out.println(report);
        assertEquals(expResult, report);
    }
}
