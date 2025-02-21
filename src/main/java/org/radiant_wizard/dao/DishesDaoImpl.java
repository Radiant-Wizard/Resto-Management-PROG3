package org.radiant_wizard.dao;

import org.radiant_wizard.Entity.Dish;
import org.radiant_wizard.Entity.Ingredient;
import org.radiant_wizard.db.Datasource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DishesDaoImpl implements DishesDao{
    Datasource datasource = new Datasource();

    public DishesDaoImpl(Datasource datasource) {
        this.datasource = datasource;
    }

    private List<Dish> convertDishesTableRows(ResultSet resultSet, List<Ingredient> ingredients) throws SQLException {
        List<Dish> dishes = new ArrayList<>();

        while (resultSet.next()){
            dishes.add(new Dish(
                    resultSet.getInt("dish_id"),
                    resultSet.getString( "dish_name"),
                    resultSet.getInt( "dish_price"),
                    ingredients
            ));
        }
        return dishes;
    }



    @Override
    public List<Dish> getDishes() throws SQLException {

        return List.of();
    }
}
