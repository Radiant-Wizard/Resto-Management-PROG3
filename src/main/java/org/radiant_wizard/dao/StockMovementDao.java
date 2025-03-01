package org.radiant_wizard.dao;

import org.radiant_wizard.Entity.StockMovement;
import org.radiant_wizard.db.Criteria;

import java.sql.SQLException;
import java.util.List;

public interface StockMovementDao {
    List<StockMovement> getAllStockMovement(List<Criteria> criteriaList, String orderBy, Boolean ascending, Integer pageSize, Integer pageNumber);
    void saveNewStockMovements(List<StockMovement> stockMovementList) throws SQLException;
    void deleteStockMovements(long stockId);
}
