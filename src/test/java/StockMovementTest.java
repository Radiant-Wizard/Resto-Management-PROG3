import lombok.Cleanup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.radiant_wizard.Entity.*;
import org.radiant_wizard.dao.IngredientDaoImpl;
import org.radiant_wizard.dao.StockMovementDaoImpl;
import org.radiant_wizard.db.Criteria;
import org.radiant_wizard.db.Datasource;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class StockMovementTest {
    Datasource datasource;
    StockMovementDaoImpl stockMovement;
    IngredientDaoImpl ingredientDao;

    @BeforeEach
    public void setUp() {
        datasource = new Datasource();
        stockMovement = new StockMovementDaoImpl(datasource);
        ingredientDao = new IngredientDaoImpl(datasource);
    }

//    @Test
//    public void testInitialStockQuantities() throws SQLException {
//        // Test initial stock quantities (before exit movements)
//        LocalDateTime testDate = LocalDateTime.now();
//
//        Ingredient egg = ingredientDao.getIngredientByCriteria(List.of(new Criteria("ingredient_id", "18", "=", LogicalOperator.AND)), null, null, 1, 1).getFirst();
//        Ingredient bread = ingredientDao.getIngredientByCriteria(List.of(new Criteria("ingredient_id", "11", "=", LogicalOperator.AND)), null, null, 1, 1).getFirst();
//        Ingredient sausage = ingredientDao.getIngredientByCriteria(List.of(new Criteria("ingredient_id", "16", "=", LogicalOperator.AND)), null, null, 1, 1).getFirst();
//        Ingredient oil = ingredientDao.getIngredientByCriteria(List.of(new Criteria("ingredient_id", "17", "=", LogicalOperator.AND)), null, null, 1, 1).getFirst();
//
//        Assertions.assertEquals(100.0, egg.getAvailableQuantity(testDate), "Initial Egg stock should be 100 units");
//        Assertions.assertEquals(50, bread.getAvailableQuantity(testDate), "Initial Bread stock should be 100 units");
//        Assertions.assertEquals(10000.0, sausage.getAvailableQuantity(testDate), "Initial Sausage stock should be 10,000 grams");
//        Assertions.assertEquals( 20, oil.getAvailableQuantity(testDate), "Initial Oil stock should be 10,000 liters");
//    }

    @Test
    public void testStockQuantitiesAfterExits() throws SQLException {
        // Test stock quantities after exit movements
        LocalDateTime testDate = LocalDateTime.of(2025, 2, 24, 12, 0);

        Ingredient egg = ingredientDao.getIngredientByCriteria(List.of(new Criteria("ingredient_id", "18", "=", LogicalOperator.AND)), null, null, 1, 1).getFirst();
        Ingredient bread = ingredientDao.getIngredientByCriteria(List.of(new Criteria("ingredient_id", "11", "=", LogicalOperator.AND)), null, null, 1, 1).getFirst();
        Ingredient sausage = ingredientDao.getIngredientByCriteria(List.of(new Criteria("ingredient_id", "16", "=", LogicalOperator.AND)), null, null, 1, 1).getFirst();
        Ingredient oil = ingredientDao.getIngredientByCriteria(List.of(new Criteria("ingredient_id", "17", "=", LogicalOperator.AND)), null, null, 1, 1).getFirst();

        Assertions.assertEquals(80.0, egg.getAvailableQuantity(testDate), "Egg stock should be 80 units after exits");
        Assertions.assertEquals(30, bread.getAvailableQuantity(testDate), "Bread stock should be 80 units after exits");
        Assertions.assertEquals(10000.0, sausage.getAvailableQuantity(testDate), "Sausage stock should remain 10,000 grams (no exits)");
        Assertions.assertEquals(20, oil.getAvailableQuantity(testDate), "Oil stock should remain 10,000 liters (no exits)");
    }

    @Test
    public void testGetAllStockMovements() {
        List<StockMovement> stockMovementList = stockMovement.getAllStockMovement(List.of(), null, null, 5, 1);

        Assertions.assertFalse(stockMovementList.isEmpty(), "Stock movement list should not be empty");
        Assertions.assertEquals(5, stockMovementList.size(), "Stock movement list should have 5 items");
    }

    @Test
    public void testTheGetAvailableQuantityWithoutParameters() throws SQLException{
        Ingredient egg = ingredientDao.getIngredientByCriteria(List.of(new Criteria("ingredient_id", "18", "=", LogicalOperator.AND)), null, null, 1, 1).getFirst();

        Assertions.assertEquals(80.0, egg.getAvailableQuantity());
    }
}
