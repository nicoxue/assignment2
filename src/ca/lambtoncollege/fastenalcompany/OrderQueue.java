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
package ca.lambtoncollege.fastenalcompany;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import org.json.simple.JSONObject;
/**
 *
 * @author JIAJUN XUE <nicoxue0324@gmail.com>
 */
public class OrderQueue {

    Queue<Order> orderQueue = new ArrayDeque<>();
    List<Order> orderList = new ArrayList<>();

    public void add(Order order) {
        orderQueue.add(order);
        order.setTimeReceived(new Date());
    }

    public void arrive(Order order) {
        if (order.getCustomerId().equals("") && order.getCustomerName().equals("")) {
            throw new NoCustomerException();
        }
        if (order.getListOfPurchases() == null) {
            throw new NoPurchasesException();
        }
        orderQueue.add(order);
        order.setTimeReceived(new Date());
    }

    public Order next() {
        return orderQueue.peek();
    }

    public void process(Order order) {
        if (order.getTimeReceived() == null) {
            throw new NoTimeReceviedException();
        }
        if (!checkListOfPurchases(order.getListOfPurchases())) {
            throw new NoPurchasesInStockException();
        }
        order.setTimeProcessed(new Date());
        orderQueue.remove(order);
        orderList.add(order);
    }

    public void fulfill(Order order) {
        if (order.getTimeProcessed() == null) {
            throw new NoTimeProcessedException();
        }
        if (order.getTimeReceived() == null) {
            throw new NoTimeReceviedException();
        }
        if (!checkListOfPurchases(order.getListOfPurchases())) {
            throw new NoPurchasesInStockException();
        }
        order.setTimeFulfilled(new Date());
    }

    public String report() {
        String report = "";
        if (!orderQueue.isEmpty() || !orderList.isEmpty()) {
            JSONObject jsObj = new JSONObject();
            List<Order> list = new ArrayList(orderQueue);
            list.addAll(orderList);
            jsObj.put("orders", list);
            report = jsObj.toString();
        }
        return report;
    }

    public boolean checkListOfPurchases(List<Purchase> purchases) {
        return true;
    }

    public class NoCustomerException extends RuntimeException {
    }

    public class NoPurchasesException extends RuntimeException {
    }

    public class NoTimeReceviedException extends RuntimeException {
    }

    public class NoPurchasesInStockException extends RuntimeException {
    }

    public class NoTimeProcessedException extends RuntimeException {
    }
}
