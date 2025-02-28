package org.radiant_wizard;

import org.radiant_wizard.Entity.Dish;
import org.radiant_wizard.Entity.Ingredient;
import org.radiant_wizard.Entity.Price;
import org.radiant_wizard.Entity.Unit;
import org.radiant_wizard.dao.DishesDaoImpl;
import org.radiant_wizard.dao.IngredientDaoImpl;
import org.radiant_wizard.db.Criteria;
import org.radiant_wizard.db.Datasource;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException, IllegalAccessException {
        Datasource datasource = new Datasource();
        DishesDaoImpl dishesDao = new DishesDaoImpl(datasource);
        IngredientDaoImpl ingredientDao = new IngredientDaoImpl(datasource);
        Ingredient ingredient = ingredientDao.getIngredientByCriteria(List.of(new Criteria("18", "ingredient_id")), null, null, 1, 1).getFirst();
        Double nearestPrice = ingredient.getNearestPrice(null).getValue();
        Double availableQuantity = ingredient.getAvailableQuantity(LocalDateTime.now());
        System.out.println(nearestPrice);
        System.out.println(availableQuantity);
        Dish dish = dishesDao.getDishesById(16L);
        System.out.println(dish.getTotalCostIngredient(LocalDateTime.now()));
    }
}