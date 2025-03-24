import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.radiant_wizard.Entity.DishOrder;
import org.radiant_wizard.Entity.Enum.StatusType;
import org.radiant_wizard.Entity.Order;
import org.radiant_wizard.dao.DishOrderDaoImpl;
import org.radiant_wizard.dao.DishesDaoImpl;
import org.radiant_wizard.dao.OrderDaoImpl;
import org.radiant_wizard.db.Datasource;

import java.sql.SQLException;
import java.util.List;

public class OrderTest {
    Datasource datasource;
    OrderDaoImpl orderDao;
    DishesDaoImpl dishesDao;
    DishOrderDaoImpl dishOrderDao;


    @BeforeEach
    void setup() throws SQLException {
        datasource = new Datasource();
        dishesDao = new DishesDaoImpl(datasource);
        orderDao = new OrderDaoImpl(datasource);
        dishOrderDao = new DishOrderDaoImpl(datasource);
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    void testGetAllOrders() {
        List<Order> orders = orderDao.getAll(2, 1);

        Assertions.assertNotNull(orders);
        Assertions.assertEquals(2, orders.size());

        System.out.println("✅ Getting all orders PASSED");
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void testGetOrder() {
        Order retrievedOrderById = orderDao.getByID(2);
        Order retrievedOrderByRef = orderDao.getByReference("CMD004");

        Assertions.assertNotNull(retrievedOrderById);
        Assertions.assertNotNull(retrievedOrderByRef);
        Assertions.assertEquals("CMD002", retrievedOrderById.getReference());
        Assertions.assertEquals(4L, retrievedOrderByRef.getOrderID());

        System.out.println("✅ Getting order by reference and id PASSED");
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    void testCreateOrder() throws SQLException {
        // Arrange
        Order order = new Order(4, "CMD004");

        // Act
        orderDao.createOrder(order);
        Order retrieveSavedOrder = orderDao.getByReference("CMD004");

        // Assert
        Assertions.assertNotNull(retrieveSavedOrder);
        Assertions.assertEquals("CMD004", retrieveSavedOrder.getReference());

        System.out.println("✅ Creating new Order PASSED");
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    void testConfirmOrder() throws SQLException, IllegalAccessException {
        List<DishOrder> orderedDishes = List.of(
                new DishOrder(5, dishesDao.getDishesById(17), 4),
                new DishOrder(6, dishesDao.getDishesById(1), 2)
        );

        Order order = new Order(5L, "CMD005");
        orderDao.createOrder(order);

        Order retrieveOrder5 = orderDao.getByID(5L);
        retrieveOrder5.addDishToOrder(orderedDishes);

        orderDao.save(retrieveOrder5);

        List<DishOrder> dishOrders = dishOrderDao.getDishOrdersByOrderId(5L);
        Assertions.assertFalse(dishOrders.isEmpty());
        System.out.println("✅ Creating new Order PASSED");

    }

    @Test
    @org.junit.jupiter.api.Order(5)
    void testGetStatus() throws SQLException, IllegalAccessException {
        // Arrange
        Order order = orderDao.getByReference("CMD004");

        // Act
        StatusType orderStatus = order.getActualStatus();

        // Assert
        Assertions.assertEquals(StatusType.IN_PROGRESS, orderStatus);

        System.out.println("✅ Getting actual Status PASSED");
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    void testUpdateStatus() {
        // Arrange
        Order order = orderDao.getByID(4L);

        // Act
        orderDao.updateStatus(StatusType.IN_PROGRESS, order.getOrderID());

        Order orderAfterUpdate = orderDao.getByID(4L);

        Assertions.assertEquals(StatusType.IN_PROGRESS, orderAfterUpdate.getActualStatus());
    }

    @Test
    @org.junit.jupiter.api.Order(7)
    void testUpdateStatusFinished() {
        // Arrange
        Order order = orderDao.getByID(4L);

        // Act
        Assertions.assertThrows(RuntimeException.class, () -> {
            orderDao.updateStatus(StatusType.FINISHED, order.getOrderID());
        });
    }

    @Test
    @org.junit.jupiter.api.Order(8)
    void testValidateOrder() throws SQLException, IllegalAccessException {
        Order impOrder = new Order(6, "CMD006");
        orderDao.createOrder(impOrder);

        List<DishOrder> orderedDishes = List.of(
                new DishOrder(7, dishesDao.getDishesById(17), 4000),
                new DishOrder(8, dishesDao.getDishesById(1), 2000)
        );
        Order retrieveOrder6 = orderDao.getByID(6L);
        retrieveOrder6.addDishToOrder(orderedDishes);

        Assertions.assertThrows(RuntimeException.class,
                () -> {
                    orderDao.save(retrieveOrder6);
                });
    }

    @Test
    @org.junit.jupiter.api.Order(9)
    void testOrderTotalPrice() throws SQLException, IllegalAccessException {
        Order order4 = orderDao.getByID(4L);
        Double price = order4.getTotalAmount();
        Assertions.assertEquals(36000.00, price);
    }
}
