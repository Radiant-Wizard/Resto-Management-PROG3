package org.radiant_wizard.dao;

import org.radiant_wizard.Entity.DishOrder;
import org.radiant_wizard.Entity.Enum.StatusType;
import org.radiant_wizard.Entity.Order;
import org.radiant_wizard.Entity.Status;
import org.radiant_wizard.db.Datasource;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements OrderDao {
    Datasource datasource;
    DishesDaoImpl dishesDao;
    DishOrderDaoImpl dishOrderDao;
    StatusDaoImpl statusDao;

    public OrderDaoImpl(Datasource datasource) {
        this.datasource = datasource;
        this.dishOrderDao = new DishOrderDaoImpl(datasource);
        this.statusDao = new StatusDaoImpl(datasource);
    }

    @Override
    public List<Order> getAll(int pageSize, int pageNumber) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT order_id, order_reference from orders LIMIT ? OFFSET ?;";
        try (Connection connection = datasource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            int offset = pageSize * (pageNumber - 1);
            preparedStatement.setInt(1, pageSize);
            preparedStatement.setInt(2, offset);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = new Order(
                            resultSet.getLong("order_id"),
                            resultSet.getString("order_reference")

                    );
                    order.setOrderedDish(dishOrderDao.getDishOrdersByOrderId(resultSet.getLong("order_id")));
                    order.setStatus(statusDao.getStatusForOrder(resultSet.getLong("order_id")));
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return orders;
    }

    @Override
    public Order getByReference(String reference) {
        String query = "SELECT order_id, order_reference from orders where order_reference ILIKE ?";
        Order order = null;
        try (Connection connection = datasource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, "%" + reference + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                order = new Order(
                        resultSet.getLong("order_id"),
                        resultSet.getString("order_reference")
                );
                order.setOrderedDish(dishOrderDao.getDishOrdersByOrderId(resultSet.getLong("order_id")));
                order.setStatus(statusDao.getStatusForOrder(resultSet.getLong("order_id")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return order;
    }

    @Override
    public Order getByID(long orderId) {
        String query = "SELECT order_id, order_reference from orders where order_id = ?";
        Order order = null;
        try (Connection connection = datasource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setLong(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                order = new Order(
                        resultSet.getLong("order_id"),
                        resultSet.getString("order_reference")
                );
                order.setOrderedDish(dishOrderDao.getDishOrdersByOrderId(resultSet.getLong("order_id")));
                order.setStatus(statusDao.getStatusForOrder(resultSet.getLong("order_id")));
            } else {
                System.out.println("No order found with ID: " + orderId);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (order != null) {
            order.setStatus(statusDao.getStatusForOrder(orderId));
        }
        return order;
    }

    @Override
    public void createOrder(Order order) throws SQLException {
        String sqlForOrders = "INSERT INTO orders (order_id, order_reference) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (Connection connection = datasource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sqlForOrders)) {
                statement.setLong(1, order.getOrderID());
                statement.setString(2, order.getReference());
                statement.executeUpdate();
            }
        }
        statusDao.insertStatusForOrder(order.getOrderID(), StatusType.CREATED);
    }

    @Override
    public void save(Order order) throws SQLException {
        if (!order.validateCommand()) {
            throw new RuntimeException("Not enough Ingredients");
        }

        if (!order.getOrderedDish().isEmpty()) {
            statusDao.insertStatusForOrder(order.getOrderID(), StatusType.CONFIRMED);
            for (DishOrder dishOrder : order.getOrderedDish()) {
                dishOrderDao.addDishOrder(dishOrder, order.getOrderID());
            }
        }
    }

    @Override
    public void updateStatus(StatusType statusType, long orderId) {
        Order order = getByID(orderId);
        if (statusType == StatusType.CONFIRMED ){
            throw new RuntimeException("Cannot perform that action");
        }
        if (statusType == StatusType.FINISHED && !order.allTheDishesFinished()) {
            throw new RuntimeException("All the dishes are not finished!");
        }
        if (statusType == StatusType.SERVED && order.getActualStatus() != StatusType.FINISHED && !order.getOrderedDish().isEmpty()) {
            throw new RuntimeException("All the dishes are not finished yet!");
        } else {
            statusDao.insertStatusForOrder(orderId, statusType);
        }
    }
}



