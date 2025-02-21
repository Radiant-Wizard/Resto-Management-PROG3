import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.radiant_wizard.Entity.Dish;
import org.radiant_wizard.Entity.Ingredient;
import org.radiant_wizard.Entity.Unit;
import org.radiant_wizard.dao.DishesDaoImpl;
import org.radiant_wizard.db.Datasource;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DishDaoImplTest {
    Datasource datasource ;
    DishesDaoImpl dishesDao;

    @BeforeEach
    void setup() throws SQLException{
        datasource = new Datasource();
        dishesDao = new DishesDaoImpl(datasource);
        }

    @Test
    public void testGetDishes() throws SQLException, IllegalAccessException {
        Dish dish = dishesDao.getDishesById(16).get(0);
        assertEquals(5500, dish.getTotalCostIngredient());
    }
    @Test
    public void testCreateDishesWithoutHotdog() throws SQLException, IllegalAccessException {
        // Prepare a dish without a hotdog
        Dish dish = new Dish(17, "Burger", 5000, List.of(
                new Ingredient(1, "Tomato", LocalDateTime.now(), 10, Unit.G),
                new Ingredient(2, "Cheese", LocalDateTime.now(), 20, Unit.G)
        ));

        // Create the dish in the database
        dishesDao.createDishes(dish);

        // Verify the dish was correctly inserted into the database
        List<Dish> dishes = dishesDao.getDishesById(17);
        assertNotNull(dishes);
        assertFalse(dishes.isEmpty());
        Dish retrievedDish = dishes.get(0);
        assertEquals(17, retrievedDish.getDishId());
        assertEquals("Burger", retrievedDish.getDishName());
        assertEquals(5000, retrievedDish.getPrice());
    }
}
