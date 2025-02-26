package org.radiant_wizard.dao;

import org.radiant_wizard.Entity.Ingredient;
import org.radiant_wizard.Entity.Price;
import org.radiant_wizard.Entity.Unit;
import org.radiant_wizard.db.Criteria;
import org.radiant_wizard.db.Datasource;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class IngredientDaoImpl implements IngredientDao {
    Datasource datasource = new Datasource();

    public IngredientDaoImpl(Datasource datasource) {
        this.datasource = datasource;
    }

    private List<Ingredient> convertIngredientsTableRows(ResultSet resultSet, List<Price> priceList) throws SQLException {
        List<Ingredient> ingredients = new ArrayList<>();

        while (resultSet.next()) {
            ingredients.add(new Ingredient(
                    resultSet.getLong("ingredient_id"),
                    resultSet.getString("ingredient_name"),
                    resultSet.getObject("creation_date_and_last_modification_time", LocalDateTime.class),
                    priceList,
                    Unit.valueOf(resultSet.getString("unit"))
            ));
        }
        return ingredients;
    }

    private List<Price> convertIngredientsPriceTableRows(ResultSet resultSet) throws SQLException {
        List<Price> priceList = new ArrayList<>();
        while (resultSet.next()) {
            priceList.add(new Price(
                    resultSet.getObject("last_modification", LocalDateTime.class),
                    resultSet.getDouble("unit_price")
            ));
        }
        return priceList;
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

    @Override
    public List<Ingredient> getIngredientByCriteria(List<Criteria> criteriaList, String orderBy, Boolean ascending, Integer pageSize, Integer pageNumber) throws SQLException {
        List<Ingredient> ingredients = new ArrayList<>();

        String sql =
                "SELECT ingredient_id, ingredient_name, last_modification, unit_price, unit from ingredients where 1=1";

        for (Criteria criteria : criteriaList) {
            if (criteria.getColumnName().equals("ingredient_name")) {
                sql += " AND " + criteria.getColumnName() + " ilike '%" + criteria.getColumnValue().toString() + "%'";
            } else {
                sql += " AND " + criteria.getColumnName() + " = '" + criteria.getColumnValue().toString() + "'";
            }
        }

        if (orderBy != null && !orderBy.isEmpty()) {
            sql += " ORDER BY " + orderBy + (ascending ? "ASC" : "DESC");
        }
        if (pageSize != null && pageNumber != null) {
            int offset = pageSize * (pageNumber - 1);
            sql += " LIMIT " + pageSize + " OFFSET " + offset;
        }

        try (Connection connection = datasource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)
        ) {
            while (resultSet.next()){
                long ingredientId = resultSet.getLong("ingredient_id");

                ingredients.add(new Ingredient(
                        resultSet.getLong("ingredient_id"),
                        resultSet.getString("ingredient_name"),
                        resultSet.getObject("last_modification", LocalDateTime.class),
                        getPriceForIngredient(ingredientId),
                        Unit.valueOf(resultSet.getString("unit"))
                ));
            }
        }
        return ingredients;
    }
}
