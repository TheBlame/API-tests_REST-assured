package org.example.pojo.orders;

import lombok.Getter;
import lombok.Setter;
import org.example.pojo.orders.nested_classes.Order;
import org.example.pojo.orders.nested_classes.Orders;

import java.util.List;

@Getter
@Setter
public class OrdersResponse {
    private String name;
    private Order order;
    private boolean success;
    private String message;
    private List<Orders> orders;
    private int total;
    private int totalToday;
}
