package org.radiant_wizard.dao;

import org.radiant_wizard.Entity.Dish;
import org.radiant_wizard.Entity.Ingredient;
import org.radiant_wizard.Entity.Unit;
import org.radiant_wizard.db.Datasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DishesDaoImpl implements DishesDao {
    Datasource datasource = new Datasource();

    public DishesDaoImpl(Datasource datasource) {
        this.datasource = datasource;
    }

    private List<Dish> convertDishesTableRows(ResultSet resultSet, List<Ingredient> ingredients) throws SQLException, IllegalAccessException {
        List<Dish> dishes = new ArrayList<>();

        while (resultSet.next()) {
            dishes.add(new Dish(
                    resultSet.getInt("dish_id"),
                    resultSet.getString("dish_name"),
                    resultSet.getInt("dish_price"),
                    ingredients
            ));
        }
        return dishes;
    }

    private List<Ingredient> convertIngredientsTableRows(ResultSet resultSet) throws SQLException {
        List<Ingredient> ingredients = new ArrayList<>();

        while (resultSet.next()) {
            ingredients.add(new Ingredient(
                    resultSet.getInt("ingredientId"),
                    resultSet.getString("ingredient_name"),
                    resultSet.getObject("last_modification", LocalDateTime.class),
                    Unit.valueOf(resultSet.getString("unit")),
                    resultSet.getInt("unitPrice"),
                    resultSet.getInt("quantity")
            ));
        }
        return ingredients;
    }

    @Override
    public List<Dish> getDishesById(int dishId) throws SQLException, IllegalAccessException {
        List<Ingredient> neededIngredients ;
        List<Dish> dishes = new ArrayList<>();
        String sqlForIngredient =
                "select dishId, ingredientId, ingredient_name, last_modification, unit, unitPrice,quantity from see_dishes where dishId = ? ";
        String sqlForDishes =
                "select dish_id, dish_name, dish_price from dishes where dish_id = ?";

        try (Connection connection = datasource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlForIngredient)
        ) {
            preparedStatement.setInt(1, dishId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                 neededIngredients = convertIngredientsTableRows(resultSet);
            }
        }

        try (Connection connection = datasource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlForDishes)
        ){
            preparedStatement.setInt(1, dishId);

            try  (ResultSet resultSet = preparedStatement.executeQuery()){
                dishes = convertDishesTableRows(resultSet, neededIngredients);
            }
        }

        return dishes;
    }
}
