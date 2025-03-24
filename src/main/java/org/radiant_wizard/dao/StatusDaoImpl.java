package org.radiant_wizard.dao;

import org.radiant_wizard.Entity.Enum.StatusType;
import org.radiant_wizard.Entity.Status;
import org.radiant_wizard.db.Datasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatusDaoImpl implements StatusDao{
    Datasource datasource;

    public StatusDaoImpl(Datasource datasource){
        this.datasource = datasource;
    }

    @Override
    public List<Status> getStatusForOrder(long orderId) {
        List<Status> statusList = new ArrayList<>();
        String query =
                "SELECT order_id, order_creation_date, order_status from order_status where order_id = ?";
        try (Connection connection = datasource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setLong(1, orderId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    statusList.add(new Status(
                            StatusType.valueOf(resultSet.getString("order_status")),
                            resultSet.getTimestamp("order_creation_date").toInstant()
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return statusList;
    }

    @Override
    public List<Status> getStatusForDishOrder(long dishOrderId) {
        return List.of();
    }
}
