package org.radiant_wizard.dao;

import org.radiant_wizard.Entity.Ingredient;
import org.radiant_wizard.Entity.Unit;
import org.radiant_wizard.db.Criteria;
import org.radiant_wizard.db.Datasource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class IngredientDaoImpl implements IngredientDao{
    Datasource datasource = new Datasource();

    public IngredientDaoImpl(Datasource datasource){
        this.datasource = datasource;
    }

    private List<Ingredient> convertIngredientsTableRows(ResultSet resultSet) throws SQLException {
        List<Ingredient> ingredients = new ArrayList<>();

        while (resultSet.next()) {
            ingredients.add(new Ingredient(
                    resultSet.getLong("ingredient_id"),
                    resultSet.getString("ingredient_name"),
                    resultSet.getObject("creation_date_and_last_modification_time", LocalDateTime.class),
                    resultSet.getInt("unit_price"),
                    Unit.valueOf(resultSet.getString("unit"))
                    ));
        }
        return ingredients;
    }
    @Override
    public List<Ingredient> getIngredientByCriteria(List<Criteria> criteriaList, String orderBy, Boolean ascending, Integer pageSize, Integer pageNumber) throws SQLException {
        List<Ingredient> ingredients;
        String sql =
            "SELECT ingredient_id, ingredient_name, creation_date_and_last_modification_time, unit_price, unit from ingredients where 1=1";

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
            ingredients = convertIngredientsTableRows(resultSet);
        }
        return ingredients;
    }
}
