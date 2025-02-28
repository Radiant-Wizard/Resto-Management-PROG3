package org.radiant_wizard.dao;

import org.radiant_wizard.Entity.*;
import org.radiant_wizard.db.Criteria;
import org.radiant_wizard.db.Datasource;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class IngredientDaoImpl implements IngredientDao {
    Datasource datasource;

    public IngredientDaoImpl(Datasource datasource) {
        this.datasource = datasource;
    }

    private List<Price> getPriceForIngredient(long ingredientId) {
        List<Price> priceList = new ArrayList<>();
        String sql = "SELECT ingredient_id, last_modification, unit_price from ingredients where ingredient_id = ?";

        try (Connection connection = datasource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, ingredientId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    priceList.add(new Price(
                            resultSet.getObject("last_modification", LocalDateTime.class),
                            resultSet.getDouble("unit_price")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return priceList;
    }

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


    @Override
    public List<Ingredient> getIngredientByCriteria(List<Criteria> criteriaList, String orderBy, Boolean ascending, Integer pageSize, Integer pageNumber) throws SQLException {
        List<Ingredient> ingredients = new ArrayList<>();

        String query =
                "SELECT ingredient_id, ingredient_name, last_modification, unit_price, unit from ingredients where 1=1";

        for (Criteria criteria : criteriaList) {
            String columnName = criteria.getColumnName();
            Object columnValue = criteria.getColumnValue();
            String operator = criteria.getOperator();
            LogicalOperator logicalOperator = criteria.getLogicalOperator();

            query += " " + logicalOperator.toString() + " " ;
            if ("BETWEEN".equalsIgnoreCase(operator)){
                Object secondValue = criteria.getSecondValue();
                query += String.format(" %s BETWEEN '%s' AND '%s' ", columnName, columnValue, secondValue);
            }else if("LIKE".equalsIgnoreCase(operator)){
                query += String.format(" %s ILIKE '%%s%%' ", columnName, columnValue);
            } else {
                query += String.format(" %s '%s' '%s' ", columnName, operator, columnValue);
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
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                long ingredientId = resultSet.getLong("ingredient_id");

                ingredients.add(new Ingredient(
                        resultSet.getLong("ingredient_id"),
                        resultSet.getString("ingredient_name"),
                        resultSet.getObject("last_modification", LocalDateTime.class),
                        Unit.valueOf(resultSet.getString("unit")),
                        getPriceForIngredient(ingredientId),
                        getStockForIngredient(ingredientId)
                ));
            }
        }
        return ingredients;
    }



}
