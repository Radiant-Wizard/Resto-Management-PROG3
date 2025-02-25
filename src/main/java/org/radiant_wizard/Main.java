package org.radiant_wizard;
import org.radiant_wizard.Entity.Dish;
import org.radiant_wizard.Entity.Ingredient;
import org.radiant_wizard.Entity.Price;
import org.radiant_wizard.Entity.Unit;
import org.radiant_wizard.dao.DishesDaoImpl;
import org.radiant_wizard.db.Datasource;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException, IllegalAccessException{
        Datasource datasource = new Datasource();
        DishesDaoImpl dishesDao = new DishesDaoImpl(datasource);
        Ingredient ingredient = new Ingredient(18L, "tomato", LocalDateTime.now(), List.of(new Price(LocalDateTime.now(), 20.0), new Price(LocalDateTime.of(2025, 02, 21, 8, 0), 35.0)), Unit.U);
        Double nearestPrice = ingredient.getNearestPrice(null).getValue();

        System.out.println(nearestPrice);
        List<Dish> dishList = dishesDao.getDishes(1, 10);
        Dish dish   = dishesDao.getDishesById(16L);
        System.out.println(dish.getTotalCostIngredient(LocalDateTime.now()));
        }
}