package org.radiant_wizard.Entity;

import lombok.Getter;
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


    public double getTotalCostIngredient(){
        List<Ingredient> ingredientList = this.ingredients;
        double cost = 0;

        for (Ingredient ingredient : ingredientList){
            cost += (ingredient.getUnitPrice() * ingredient.getQuantity()) ;
        }
        return cost;
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
