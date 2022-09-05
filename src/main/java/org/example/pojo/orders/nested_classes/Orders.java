package org.example.pojo.orders.nested_classes;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Orders {
    private int number;
    private List<String> ingredients;
    private String _id;
    private String status;
    private String createdAt;
    private String updatedAt;

}
