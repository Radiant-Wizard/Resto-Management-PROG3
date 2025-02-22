package org.radiant_wizard.dao;

import org.radiant_wizard.Entity.Ingredient;
import org.radiant_wizard.db.Criteria;

import java.sql.SQLException;
import java.util.List;

public interface IngredientDao {
    List<Ingredient> getIngredientByCriteria(List<Criteria> criteriaList, String orderBy,Boolean ascending,Integer pageSize,Integer pageNumber) throws SQLException;
}
