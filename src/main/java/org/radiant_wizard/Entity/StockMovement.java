package org.radiant_wizard.Entity;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class StockMovement {
    private final long ingredientId;
    private final double movementQuantity;
    private final MovementType movementType;
    private final LocalDateTime movementDate;
    private final Unit unit;

    public StockMovement(long ingredientId, double movementQuantity, Unit unit, MovementType movementType, LocalDateTime movementDate) {
        this.ingredientId = ingredientId;
        this.movementQuantity = movementQuantity;
        this.unit = unit;
        this.movementType = movementType;
        this.movementDate = movementDate;
    }

}
