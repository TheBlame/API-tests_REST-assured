package org.example.pojo.orders.nested_classes;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Order {
    private List<Ingredients> ingredients;
    private String _id;
    private Owner owner;
    private String status;
    private String name;
    private String createdAt;
    private String updatedAt;
    private int number;
    private int price;

}
