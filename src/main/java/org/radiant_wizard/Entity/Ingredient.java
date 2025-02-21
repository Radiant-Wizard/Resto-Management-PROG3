package org.radiant_wizard.Entity;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
public abstract class Ingredient {
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

    @Override
    public String toString() {
        return "Ingredient{" +
                "\n ingredientId : " + ingredientId +
                "\n ingredientName : '" + ingredientName + '\'' +
                "\n last_modification : " + creationDateAndLastModificationTime +
                "\n unitPrice : " + unitPrice +
                "\n unit : " + unit +
                '}';
    }
}
