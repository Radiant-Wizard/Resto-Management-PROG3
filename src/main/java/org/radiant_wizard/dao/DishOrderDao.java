package org.radiant_wizard.dao;

import org.radiant_wizard.Entity.DishOrder;
import org.radiant_wizard.Entity.Enum.StatusType;

import java.util.List;

public interface DishOrderDao {

    void updateDishOrderStatus(StatusType statusType, long orderId);

    void addDishOrder(DishOrder dishOrder, long orderId);

    List<DishOrder> getDishOrdersByOrderId(long orderId);


    void deleteDishOrder(long dishOrderId);
}
