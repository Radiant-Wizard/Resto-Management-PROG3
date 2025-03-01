package org.radiant_wizard.dao;

import org.radiant_wizard.Entity.*;
import org.radiant_wizard.db.Criteria;
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

//    private List<Ingredient> convertIngredientsTableRows(ResultSet resultSet, List<Price> priceList) throws SQLException {
//        List<Ingredient> ingredients = new ArrayList<>();
//        while (resultSet.next()) {
//            ingredients.add(new Ingredient(
//                    resultSet.getLong("ingredientId"),
//                    resultSet.getString("ingredient_name"),
//                    resultSet.getObject("last_modification", LocalDateTime.class),
//                    Unit.valueOf(resultSet.getString("unit")),
//                    priceList,
//                    resultSet.getDouble("quantity")
//            ));
//        }
//        return ingredients;
//    }
//
//    private List<Price> convertIngredientsPriceTableRows(ResultSet resultSet) throws SQLException {
//        List<Price> priceList = new ArrayList<>();
//        while (resultSet.next()) {
//            priceList.add(new Price(
//                    resultSet.getObject("creation_date_and_last_modification_time", LocalDateTime.class),
//                    resultSet.getDouble("unit_price")
//                    ));
//        }
//        return priceList;
//    }

    private List<StockMovement> getStockForIngredient(long ingredientId) {
        List<StockMovement> stockList = new ArrayList<>();
        String sql = "SELECT ingredient_id, movement_date, movement_type, quantity, unit from stock_movement where ingredient_id = ?";

        try (Connection connection = datasource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, ingredientId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    stockList.add(new StockMovement(
                            ingredientId,
                            resultSet.getDouble("quantity"),
                            Unit.valueOf(resultSet.getString("unit")),
                            MovementType.valueOf(resultSet.getString("movement_type")),
                            resultSet.getObject("movement_date", LocalDateTime.class)
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stockList;
    }

    private List<Price> getPricesForIngredient(long ingredientId) {
        List<Price> prices = new ArrayList<>();
        String sqlForPrice =
                "select ingredient_id, last_modification, unit_price from ingredients where ingredient_id = ?";

        try (Connection connection = datasource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlForPrice)) {
            preparedStatement.setLong(1, ingredientId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    prices.add(new Price(
                            resultSet.getObject("last_modification", LocalDateTime.class),
                            resultSet.getDouble("unit_price")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prices;
    }


    private List<Ingredient> getIngredientForDishes(long dishId) {
        List<Ingredient> ingredients = new ArrayList<>();
        String sqlForIngredient =
                "select dish_id, ingredient_id, ingredient_name, last_modification, unit, unit_price,quantity from dishes_with_ingredients where dish_id = ? ";

        try (Connection connection = datasource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlForIngredient)) {
            preparedStatement.setLong(1, dishId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    long ingredientId = resultSet.getLong("ingredient_id");
                    Ingredient ingredient = new Ingredient(
                            resultSet.getLong("ingredient_id"),
                            resultSet.getString("ingredient_name"),
                            resultSet.getObject("last_modification", LocalDateTime.class),
                            Unit.valueOf(resultSet.getString("unit")),
                            getPricesForIngredient(ingredientId),
                            getStockForIngredient(ingredientId),
                            resultSet.getDouble("quantity")
                    );
                    ingredients.add(ingredient);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredients;
    }

    @Override
    public List<Dish> getDishes(List<Criteria> criteriaList, String orderBy, Boolean ascending, Integer pageSize, Integer pageNumber) throws SQLException {
        List<Dish> dishes = new ArrayList<>();
        String query = "select dish_id, dish_name, dish_price from dishes where 1=1 ";

        for (Criteria criteria : criteriaList) {
            String columnName = criteria.getColumnName();
            Object columnValue = criteria.getColumnValue();
            String operator = criteria.getOperator();
            LogicalOperator logicalOperator = criteria.getLogicalOperator();

            query += " " + logicalOperator.toString() + " ";
            if ("BETWEEN".equalsIgnoreCase(operator)) {
                Object secondValue = criteria.getSecondValue();
                query += String.format(" %s BETWEEN '%s' AND '%s' ", columnName, columnValue, secondValue);
            } else if ("LIKE".equalsIgnoreCase(operator)) {
                query += String.format(" %s ILIKE '%%s%%' ", columnName, columnValue);
            } else {
                query += String.format(" %s %s %s ", columnName, operator, columnValue);
            }
        }

        if (orderBy != null && !orderBy.isEmpty()) {
            query += " ORDER BY " + orderBy + (ascending ? " ASC " : " DESC ");
        }
        if (pageSize != null && pageNumber != null) {
            int offset = pageSize * (pageNumber - 1);
            query += " LIMIT " + pageSize + " OFFSET " + offset;
        }

        try (Connection connection = datasource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                dishes.add(new Dish(
                        resultSet.getLong("dish_id"),
                        resultSet.getString("dish_name"),
                        resultSet.getInt("dish_price"),
                        getIngredientForDishes(resultSet.getLong("dish_id"))
                ));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return dishes;
    }

    @Override
    public Dish getDishesById(long dishId) throws SQLException, IllegalAccessException {
        Dish dish;
        String sqlForDishes =
                "select dish_id, dish_name, dish_price from dishes where dish_id = ?";

        try (Connection connection = datasource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlForDishes)
        ) {
            preparedStatement.setLong(1, dishId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                dish = convertDishesTableRows(resultSet, getIngredientForDishes(dishId)).getFirst();
            }
        }
        return dish;
    }

    @Override
    public void saveDishes(List<Dish> dishList) throws SQLException {
        try (Connection connection = datasource.getConnection()) {
            String queryForDish = "INSERT INTO dishes (dish_id, dish_name, dish_price) VALUES(?, ?, ?)" +
                    "ON CONFLICT (dish_id)" +
                    "DO update set " +
                    "dish_name = EXCLUDED.dish_name," +
                    "dish_price = EXCLUDED.dish_price;";


            try (PreparedStatement preparedStatement = connection.prepareStatement(queryForDish);
            ) {
                for (Dish dish : dishList) {
                    preparedStatement.setLong(1, dish.getDishId());
                    preparedStatement.setString(2, dish.getDishName());
                    preparedStatement.setInt(3, dish.getPrice());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            } catch (SQLException e) {
                throw new SQLException(e);
            }

            for (Dish dish : dishList) {
                for (Ingredient ingredient : dish.getIngredients()) {
                    String queryForIngredient = "insert into dish_ingredients (dish_id, ingredient_id, quantity) values (?, ?, ?)" +
                            "ON CONFLICT (dish_id, ingredient_id)" +
                            "DO UPDATE SET " +
                            "dish_id = EXCLUDED.dish_id," +
                            "ingredient_id = EXCLUDED.ingredient_id," +
                            "quantity = EXCLUDED.quantity ;";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(queryForIngredient)) {
                        preparedStatement.setLong(1, dish.getDishId());
                        preparedStatement.setLong(2, ingredient.getIngredientId());
                        preparedStatement.setDouble(3, ingredient.getNeededQuantity());
                        preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        throw new SQLException(e);
                    }
                }
            }
        }

    }

    @Override
    public void deleteDish(long dishId) {
        String queryForDelete = "DELETE FROM dishes where dish_id = ?";

        try (Connection connection = datasource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(queryForDelete)
        ) {
            preparedStatement.setLong(1, dishId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
