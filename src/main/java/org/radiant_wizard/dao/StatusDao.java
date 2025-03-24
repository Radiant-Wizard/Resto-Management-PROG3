package org.radiant_wizard.dao;

import org.radiant_wizard.Entity.Status;

import java.util.List;

public interface StatusDao {
    List<Status> getStatusForOrder(long orderId);
    List<Status> getStatusForDishOrder(long dishOrderId);
}
