package org.radiant_wizard.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
public class DishWithIngredient {
    private String ingredientName;
    private Integer necessaryQuantity;
    private Unit unit;

    public DishWithIngredient(String ingredientName, Integer necessaryQuantity, Unit unit) {
        this.ingredientName = ingredientName;
        this.necessaryQuantity = necessaryQuantity;
        this.unit = unit;
    }
}
