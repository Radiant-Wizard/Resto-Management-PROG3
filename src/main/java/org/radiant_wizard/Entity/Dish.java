package org.radiant_wizard.Entity;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Dish {
    private final Long dishId;
    private final String dishName;
    private final Integer price;
    private final List<Ingredient> ingredients;

    public Dish(Long dishId, String dishName, Integer price, List<Ingredient> ingredients) throws IllegalAccessException {
        if (ingredients == null ){
            throw new IllegalAccessException();
        }

        this.dishId = dishId;
        this.dishName = dishName;
        this.price = price;
        this.ingredients = ingredients;
    }


    public double getTotalCostIngredient(LocalDateTime dateTime){
        List<Ingredient> ingredients = this.ingredients;
        LocalDateTime now = LocalDateTime.now();
        double cost = 0;


        for (Ingredient ingredient : ingredients){
            Double nearestValue = ingredient.getNearestPrice(dateTime).getValue();
            cost += (ingredient.getQuantity() * nearestValue);
        }
        return cost;
    }

    public double getGrossMargin(LocalDateTime localDateTime){
        double totalProductionCost = getTotalCostIngredient(localDateTime);
        double salePrice = this.price;

        return totalProductionCost - salePrice ;
    }
    @Override
    public String toString() {
        return "Dish {\n" +
                "  dishId      : " + dishId + ",\n" +
                "  dishName    : '" + dishName + "',\n" +
                "  price       : " + price + ",\n" +
                "  ingredients : [\n" +
                "    " + ingredients.stream()
                .map(Ingredient::toString)
                .reduce((a, b) -> a + ",\n    " + b)
                .orElse("") +
                "\n  ]\n" +
                "}";
    }

}
