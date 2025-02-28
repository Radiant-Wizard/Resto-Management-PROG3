package org.radiant_wizard.Entity;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@Getter
public class Ingredient {
    private final Long ingredientId;
    private final String ingredientName;
    private final LocalDateTime lastModification;
    private final Unit unit;
    private double quantity;
    private final List<Price> unitPrice;
    private List<StockMovement> stockMovements;


    public Ingredient(Long ingredientId, String ingredientName, LocalDateTime lastModification, Unit unit, List<Price> unitPrice, List<StockMovement> stockMovements) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.lastModification = lastModification;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.stockMovements = stockMovements;
    }

    public Ingredient(Long ingredientId, String ingredientName, LocalDateTime lastModification, Unit unit, List<Price> unitPrice, double quantity) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.lastModification = lastModification;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public Price getNearestPrice(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return this.unitPrice.stream().max(Comparator.comparing(Price::getModificationDate)).get();
        }

        long smallestDifference = Long.MAX_VALUE;
        Price nearestPriceToTheGivenDate = null;
        for (Price price : this.unitPrice) {
            long difference = ChronoUnit.MILLIS.between(localDateTime, price.getModificationDate());
            if (difference < smallestDifference) {
                smallestDifference = difference;
                nearestPriceToTheGivenDate = price;
            }
        }
        return nearestPriceToTheGivenDate;
    }


    public double getAvailableQuantity(LocalDateTime ofThisDate) {

        LocalDateTime localDateTime = (ofThisDate == null ? LocalDateTime.now(): ofThisDate);
        double currentAvailableQuantity = 0.0;
        for (StockMovement stockMovement : this.stockMovements) {
            if (stockMovement.getMovementDate().isBefore(localDateTime) || stockMovement.getMovementDate().isEqual(localDateTime)) {
                if (stockMovement.getMovementType().equals(MovementType.ENTRY)) {
                    currentAvailableQuantity += stockMovement.getMovementQuantity();
                } else if (stockMovement.getMovementType().equals(MovementType.EXIT)) {
                    currentAvailableQuantity -= stockMovement.getMovementQuantity();
                }
            }
        }
        return currentAvailableQuantity;
    }

}
