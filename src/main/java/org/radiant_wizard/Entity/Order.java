package org.radiant_wizard.Entity;

import lombok.Data;
import lombok.ToString;
import org.radiant_wizard.Entity.Enum.StatusType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@ToString
public class Order {
    private final long orderID;
    private final String reference;
    private List<DishOrder> orderedDish;
    private List<Status> status;

    public Order(long orderID, String reference) {
        this.orderID = orderID;
        this.reference = reference;
    }

    public boolean validateCommand() {
        for (DishOrder dishOrder : this.orderedDish) {
            if (dishOrder.getDishQuantityCommanded() > dishOrder.getCommendedDish().getAvailableQuantity()) {
                System.out.println(
                        "Insufficient quantity for dish : " + dishOrder.getCommendedDish().getDishName() +
                                "\n Available quantity : " + dishOrder.getCommendedDish().getAvailableQuantity() +
                                "\n Ordered quantity : " + dishOrder.getDishQuantityCommanded()
                        );
                return false;
            }
        }
        return true;
    }

    public StatusType getActualStatus(){
        return status
                .stream()
                .max(Comparator.comparing(Status::getCreationDate))
                .get()
                .getStatusType();
    }
    public void addDishToOrder(List<DishOrder> dishOrder){
        setOrderedDish(dishOrder);
    }

    public Double getTotalAmount(){
        return this.orderedDish
                .stream()
                .mapToDouble(dish -> (dish.getCommendedDish().getPrice() * dish.getDishQuantityCommanded()))
                .reduce(0.0, Double::sum );
    }

    public boolean allTheDishesFinished(){
        for (DishOrder dishOrder : this.orderedDish){
            if (dishOrder.getActualStatus() != StatusType.FINISHED){
                return false;
            }
        }
        return true;
    }

}
