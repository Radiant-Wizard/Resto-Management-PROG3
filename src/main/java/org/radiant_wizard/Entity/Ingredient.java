package org.radiant_wizard.Entity;

import lombok.Getter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@Getter
public class Ingredient {
    private final Long ingredientId;
    private final String ingredientName;
    private final LocalDateTime creationDateAndLastModificationTime;
    private final Unit unit;
    private double quantity;
    private final List<Price> unitPrice;


    public Ingredient(Long ingredientId, String ingredientName, LocalDateTime creationDateAndLastModificationTime, List<Price> unitPrice, Unit unit) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.creationDateAndLastModificationTime = creationDateAndLastModificationTime;
        this.unitPrice = unitPrice;
        this.unit = unit;
    }

    public Ingredient(Long ingredientId, String ingredientName, LocalDateTime creationDateAndLastModificationTime, Unit unit, List<Price> unitPrice, double quantity) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.creationDateAndLastModificationTime = creationDateAndLastModificationTime;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public Price getNearestPrice(LocalDateTime localDateTime){
        if (localDateTime == null ){
            return this.unitPrice.stream().max(Comparator.comparing(Price::getModificationDate)).get();
        }

        long smallestDifference = Long.MAX_VALUE;
        Price nearestPriceToTheGivenDate = null;
        for (Price price : this.unitPrice ){
            long difference = ChronoUnit.MILLIS.between(localDateTime, price.getModificationDate());
            if (difference < smallestDifference){
                smallestDifference = difference;
                nearestPriceToTheGivenDate = price;
            }
        }
        return nearestPriceToTheGivenDate;
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
