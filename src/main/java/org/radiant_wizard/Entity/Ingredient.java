package org.radiant_wizard.Entity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Ingredient {
    private int ingredientId;
    private String ingredientName;
    private LocalDateTime creationDateAndLastModificationTime;
    private Integer unitPrice;
    private Unit unit;

    public Ingredient(int ingredientId, String ingredientName, LocalDateTime creationDateAndLastModificationTime, Integer unitPrice, Unit unit) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.creationDateAndLastModificationTime = creationDateAndLastModificationTime;
        this.unitPrice = unitPrice;
        this.unit = unit;
    }
}
