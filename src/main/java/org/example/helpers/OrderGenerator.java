package org.example.helpers;

import com.github.javafaker.Faker;
import org.example.pojo.orders.IngredientsResponse;
import org.example.pojo.orders.nested_classes.Data;
import org.example.pojo.orders.OrderRequest;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class OrderGenerator extends AbstractHelpers{
    private static final Faker FAKER = new Faker();

    public static OrderRequest generateOrderWithRandomIngredient() {
        List<String> ingredients = given()
                .baseUri(BASE_URI)
                .get("/api/ingredients")
                .then()
                .extract()
                .body()
                .as(IngredientsResponse.class).getData().stream()
                .map(Data::get_id)
                .collect(Collectors.toList());
        return new OrderRequest(List.of(ingredients.get(FAKER.number().numberBetween(0, ingredients.size() - 1))));
    }

    public static OrderRequest generateOrderWithoutIngredients() {
        return new OrderRequest(List.of());
    }

    public static OrderRequest generateOrderWithInvalidIngredientHash() {
        return new OrderRequest(List.of(FAKER.bothify("#?#?#?")));
    }
}
