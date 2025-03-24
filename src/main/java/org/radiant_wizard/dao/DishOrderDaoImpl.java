package org.radiant_wizard.dao;

import org.radiant_wizard.Entity.DishOrder;
import org.radiant_wizard.Entity.Status;
import org.radiant_wizard.Entity.Enum.StatusType;
import org.radiant_wizard.db.Datasource;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DishOrderDaoImpl implements DishOrderDao {
    Datasource datasource = new Datasource();
    DishesDaoImpl dishesDao = new DishesDaoImpl(datasource);
    StatusDaoImpl statusDao = new StatusDaoImpl(datasource);

    public DishOrderDaoImpl(Datasource datasource) {
        this.datasource = datasource;
    }


    @Override
    public void updateDishOrderStatus(StatusType statusType, long orderDishId) {
        statusDao.insertStatusForDishOrder(orderDishId, statusType);
    }

    @Override
    public void addDishOrder(DishOrder dishOrder, long orderId) {
        try (Connection connection = datasource.getConnection()) {
            String sql =
                    "INSERT INTO order_dish (order_dish_id, dish_id, order_id, ordered_dish_quantity) VALUES (?, ?, ?, ?) ON CONFLICT DO NOTHING";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, dishOrder.getDishOrderId());
                statement.setLong(2, dishOrder.getCommendedDish().getDishId());
                statement.setLong(3, orderId);
                statement.setInt(4, dishOrder.getDishQuantityCommanded());
                statement.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Error inserting into order_dish: " + e.getMessage());
                throw new RuntimeException(e);
            }
            statusDao.insertStatusForDishOrder(dishOrder.getDishOrderId(), StatusType.CREATED);
        } catch (SQLException e) {
            System.err.println("Error opening connection or preparing statement: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<DishOrder> getDishOrdersByOrderId(long orderId) {
        List<DishOrder> dishOrders = new ArrayList<>();
        String sql = "SELECT order_dish_id, dish_id, order_id, ordered_dish_quantity FROM order_dish WHERE order_id = ?";

        try (Connection connection = datasource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    long orderDishId = resultSet.getLong("order_dish_id");
                    long dishId = resultSet.getLong("dish_id");
                    int quantity = resultSet.getInt("ordered_dish_quantity");
                    DishOrder dishOrder = new DishOrder(orderDishId, dishesDao.getDishesById(dishId), quantity);
                    dishOrders.add(dishOrder);
                    dishOrder.setStatusList(statusDao.getStatusForDishOrder(orderDishId));
                }
            }
        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return dishOrders;
    }

    @Override
    public void deleteDishOrder(long dishOrderId) {
        String sql = "DELETE FROM command_dish WHERE commanded_dish_id = ?";
        try (Connection connection = datasource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, dishOrderId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
