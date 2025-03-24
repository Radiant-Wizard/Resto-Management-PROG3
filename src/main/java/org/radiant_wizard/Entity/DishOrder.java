package org.radiant_wizard.Entity;

import lombok.Data;
import org.radiant_wizard.Entity.Enum.StatusType;

import java.util.Comparator;
import java.util.List;

@Data
public class DishOrder {
    private final long dishOrderId;
    private final Dish commendedDish;
    private final Integer dishQuantityCommanded;
    private List<Status> statusList;

    public DishOrder(long dishOrderId, Dish commendedDish, Integer dishQuantityCommanded) {
        this.dishOrderId = dishOrderId;
        this.commendedDish = commendedDish;
        this.dishQuantityCommanded = dishQuantityCommanded;
    }

    public StatusType getActualStatus(){
        return statusList
                .stream()
                .max(Comparator.comparing(Status::getCreationDate))
                .map(Status::getStatusType)
                .orElse(null);
    }
}

