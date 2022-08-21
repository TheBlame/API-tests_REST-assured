package org.example.pojo.orders;

import lombok.Getter;
import lombok.Setter;
import org.example.pojo.orders.nested_classes.Data;

import java.util.List;

@Getter
@Setter
public class IngredientsResponse {
    private boolean success;
    private List<Data> data;
}
