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
                    resultSet.getLong("dish_id"),
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
                    resultSet.getLong("ingredientId"),
                    resultSet.getString("ingredient_name"),
                    resultSet.getObject("last_modification", LocalDateTime.class),
                    Unit.valueOf(resultSet.getString("unit")),
                    resultSet.getInt("unitPrice"),
                    resultSet.getDouble("quantity")
            ));
        }
        return ingredients;
    }

    @Override
    public List<Dish> getDishes(int pageSize, int pageNumber) throws SQLException {
        List<Dish> dishes = new ArrayList<>();
        String sqlForDishes =
                "select dish_id, dish_name, dish_price from dishes";

        try (Connection connection = datasource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlForDishes);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()){
                long dishId = resultSet.getLong("dish_id");
                String dishName = resultSet.getString("dish_name");
                Integer dishPrice = resultSet.getInt("dish_price");

                String sqlForIngredient =
                        "SELECT dishId, ingredientId, ingredient_name, last_modification, unit, unitPrice, quantity FROM see_dishes WHERE dishId = ?;";

                List<Ingredient> neededIngredients;
                try (PreparedStatement preparedStatement1 = connection.prepareStatement(sqlForIngredient)){
                    preparedStatement1.setLong(1, dishId);
                    try (ResultSet resultSet1 = preparedStatement1.executeQuery()){
                        neededIngredients = convertIngredientsTableRows(resultSet1);
                    }
                }
                dishes.add(new Dish(dishId,  dishName, dishPrice, neededIngredients));

            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return dishes;
    }

    @Override
    public Dish getDishesById(int dishId) throws SQLException, IllegalAccessException {
        List<Ingredient> neededIngredients;
        Dish dishes;
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
        ) {
            preparedStatement.setInt(1, dishId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                dishes = convertDishesTableRows(resultSet, neededIngredients).getFirst();
            }
        }

        return dishes;
    }

    @Override
    public void createDishes(Dish dish) throws SQLException {
        String queryForDish = "INSERT INTO dishes (dish_id, dish_name, dish_price) VALUES(?, ?, ?)" +
                "ON CONFLICT (dish_id)" +
                "DO update set " +
                "dish_name = EXCLUDED.dish_name," +
                "dish_price = EXCLUDED.dish_price;";


        try (Connection connection = datasource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(queryForDish);
        ) {
            preparedStatement.setLong(1, dish.getDishId());
            preparedStatement.setString(2, dish.getDishName());
            preparedStatement.setInt(3, dish.getPrice());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }

        for (Ingredient ingredient : dish.getIngredients()) {
            String queryForIngredient = "insert into dish_ingredients (dish_id, ingredient_id, quantity) values (?, ?, ?)" +
                    "ON CONFLICT (dish_id, ingredient_id)" +
                    "DO UPDATE SET " +
                    "dish_id = EXCLUDED.dish_id," +
                    "ingredient_id = EXCLUDED.ingredient_id," +
                    "quantity = EXCLUDED.quantity ;";
            try (Connection connection = datasource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(queryForIngredient)) {
                preparedStatement.setLong(1, dish.getDishId());
                preparedStatement.setLong(2, ingredient.getIngredientId());
                preparedStatement.setDouble(3, ingredient.getQuantity());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new SQLException(e);
            }
        }
    }
}
