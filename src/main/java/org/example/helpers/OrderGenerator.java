package org.example.helpers;

import com.github.javafaker.Faker;
import org.example.pojo.orders.IngredientsResponse;
import org.example.pojo.orders.OrderRequest;
import org.example.pojo.orders.nested_classes.Data;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.example.helpers.UrlAndSpec.*;

public class OrderGenerator {
    private final Faker faker = new Faker();

    public OrderRequest generateOrderWithRandomIngredient() {
        List<String> ingredients = given()
                .spec(REQUEST_SPECIFICATION)
                .get(INGREDIENTS_PATH)
                .then()
                .extract()
                .body()
                .as(IngredientsResponse.class).getData().stream()
                .map(Data::get_id)
                .collect(Collectors.toList());
        return OrderRequest.builder()
                .ingredients(List.of(ingredients.get(faker.number().numberBetween(0, ingredients.size() - 1)))).build();
    }

    public OrderRequest generateOrderWithoutIngredients() {
        return OrderRequest.builder()
                .ingredients(List.of()).
                build();
    }

    public OrderRequest generateOrderWithInvalidIngredientHash() {
        return OrderRequest.builder()
                .ingredients(List.of(faker.bothify("#?#?#?")))
                .build();
    }
}
