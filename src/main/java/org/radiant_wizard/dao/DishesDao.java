package org.radiant_wizard.dao;

import org.radiant_wizard.Entity.Dish;

import java.sql.SQLException;
import java.util.List;

public interface DishesDao {
    List<Dish> getDishes()  throws SQLException;
}
