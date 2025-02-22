package org.radiant_wizard.Entity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Ingredient {
    private final Long ingredientId;
    private final String ingredientName;
    private final LocalDateTime creationDateAndLastModificationTime;
    private double unitPrice, quantity;
    private final Unit unit;


    public Ingredient(Long ingredientId, String ingredientName, LocalDateTime creationDateAndLastModificationTime, double unitPrice, Unit unit) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.creationDateAndLastModificationTime = creationDateAndLastModificationTime;
        this.unitPrice = unitPrice;
        this.unit = unit;
    }

    public Ingredient(Long ingredientId, String ingredientName, LocalDateTime creationDateAndLastModificationTime, Unit unit, double unitPrice, double quantity) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.creationDateAndLastModificationTime = creationDateAndLastModificationTime;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Ingredient {\n" +
                "  ingredientName  : '" + ingredientName + "',\n" +
                "  unit           : " + unit + ",\n" +
                "  quantity       : " + quantity + ",\n" +
                "  unitPrice      : " + unitPrice + ",\n" +
                "  lastModified   : " + creationDateAndLastModificationTime + "\n" +
                "}";
    }

}
