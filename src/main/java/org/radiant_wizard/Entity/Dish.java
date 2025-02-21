package org.radiant_wizard.Entity;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Dish {
    private int dishId;
    private String dishName;
    private Integer price;
    private List<Ingredient> ingredients;

    public Dish(int dishId, String dishName, Integer price, List<Ingredient> ingredients) {
        this.dishId = dishId;
        this.dishName = dishName;
        this.price = price;
        this.ingredients = ingredients;
    }

    public int getTotalCostIngredient(){
        return 1;
    }

}
