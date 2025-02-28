package org.radiant_wizard.dao;

import org.radiant_wizard.Entity.*;
import org.radiant_wizard.db.Criteria;
import org.radiant_wizard.db.Datasource;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StockMovementDaoImpl implements  StockMovementDao{
    Datasource datasource;

    public StockMovementDaoImpl(Datasource datasource){
        this.datasource = datasource;
    }
    private List<StockMovement> convertStockMovementRows(ResultSet resultSet) throws SQLException{
        List<StockMovement> stockMovementList = new ArrayList<>();

        while (resultSet.next()){
            stockMovementList.add( new StockMovement(
                    resultSet.getLong("ingredient_id"),
                    resultSet.getDouble("quantity"),
                    Unit.valueOf(resultSet.getString("unit")),
                    MovementType.valueOf(resultSet.getString("movement_type")),
                    resultSet.getObject("movement_date", LocalDateTime.class)
            ));
        }
        return stockMovementList;
    }
    @Override
    public List<StockMovement> getAllStockMovement(List<Criteria> criteriaList, String orderBy, Boolean ascending, Integer pageSize, Integer pageNumber) {
        String query = "SELECT ingredient_id, movement_date, movement_type, quantity, unit from stock_movement where 1=1 ";
        List<StockMovement> stockMovementList = new ArrayList<>();
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
            stockMovementList = convertStockMovementRows(resultSet);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return stockMovementList;
    }

    @Override
    public void saveNewStockMovements(List<StockMovement> stockMovementList) throws SQLException {
        try (Connection connection = datasource.getConnection()){
            connection.setAutoCommit(false);
            String query = "INSERT INTO stock_movement (ingredient_id, movement_date, movement_type, quantity, unit) VALUES (?, ?, ?, ?, ?::measurement_unit)";

            try ( PreparedStatement preparedStatement = connection.prepareStatement(query)){
                for (StockMovement stockMovement : stockMovementList){
                    preparedStatement.setLong(1, stockMovement.getIngredientId());
                    preparedStatement.setTimestamp(2, Timestamp.valueOf(stockMovement.getMovementDate()));
                    preparedStatement.setString(3, stockMovement.getMovementType().toString());
                    preparedStatement.setDouble(4, stockMovement.getMovementQuantity());
                    preparedStatement.setString(5, stockMovement.getUnit().toString());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
                connection.commit();
            }catch (SQLException e){
                connection.rollback();
                throw new SQLException(e);
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }
}
