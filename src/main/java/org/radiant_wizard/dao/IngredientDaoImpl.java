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

public class IngredientDaoImpl implements IngredientDao{
    Datasource datasource = new Datasource();

    public IngredientDaoImpl(Datasource datasource){
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
                    resultSet.getObject("creation_date_and_last_modification_time", LocalDateTime.class),
                    resultSet.getDouble("unit_price")
            ));
        }
        return priceList;
    }
    @Override
    public List<Ingredient> getIngredientByCriteria(List<Criteria> criteriaList, String orderBy, Boolean ascending, Integer pageSize, Integer pageNumber) throws SQLException {
        List<Ingredient> ingredients;
        List<Price> priceList;
        String sql =
            "SELECT ingredient_id, ingredient_name, creation_date_and_last_modification_time, unit_price, unit from ingredients where 1=1";
        String sqlForPrices =
                " SELECT ingredient_id, creation_date_and_last_modification_time, unit_price FROM ingredients WHERE ingredient_id = ?;";

        for (Criteria criteria : criteriaList){
            if (criteria.getColumnName().equals("ingredient_name")){
                sql += " AND " + criteria.getColumnName() + " ilike '%" + criteria.getColumnValue().toString() + "%'";
            } else {
                sql += " AND " + criteria.getColumnName() + " = '" + criteria.getColumnValue().toString() + "'";
            }
        }

        if (orderBy != null && !orderBy.isEmpty()){
            sql += " ORDER BY " + orderBy +  (ascending ? "ASC" : "DESC") ;
        }
        if (pageSize != null && pageNumber != null ){
            int offset = pageSize * (pageNumber - 1);
            sql += " LIMIT " + pageSize + " OFFSET " + offset;
        }

        try (Connection connection = datasource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)
        ) {
            try (PreparedStatement preparedStatementPrice = connection.prepareStatement(sqlForPrices)) {
                preparedStatementPrice.setLong(1, resultSet.getLong("ingredient_id"));
                try (ResultSet resultSetPrice = preparedStatementPrice.executeQuery()) {
                    priceList = convertIngredientsPriceTableRows(resultSetPrice);
                }
            }

            ingredients = convertIngredientsTableRows(resultSet, priceList);
        }
        return ingredients;
    }
}
