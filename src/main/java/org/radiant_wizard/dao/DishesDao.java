package org.radiant_wizard.dao;

import org.radiant_wizard.Entity.Dish;
import org.radiant_wizard.db.Criteria;

import java.sql.SQLException;
import java.util.List;

public interface DishesDao {
    List<Dish> getDishes(List<Criteria> criteriaList, String orderBy, Boolean ascending, Integer pageSize, Integer pageNumber) throws SQLException;
    Dish getDishesById(long dishId) throws SQLException, IllegalAccessException;
    void saveDishes(List<Dish> dish) throws SQLException;
    void deleteDish(long dishId);
}
