package org.example.pojo.orders;

import java.util.List;

public class OrderRequest {
    private List<String> ingredients;

    public OrderRequest(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public OrderRequest() {
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}