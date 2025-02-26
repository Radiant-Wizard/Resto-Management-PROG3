import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.radiant_wizard.Entity.Dish;
import org.radiant_wizard.Entity.Ingredient;
import org.radiant_wizard.Entity.Price;
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
        Dish dish = dishesDao.getDishesById(16);
        assertEquals(5500, dish.getTotalCostIngredient(LocalDateTime.now()));
    }
    @Test
    public void testCreateDishesWithoutHotdog() throws SQLException, IllegalAccessException {
        // Prepare a dish
        Dish dish = new Dish(17L , "Burger", 5000, List.of(
                new Ingredient(1L, "Tomato", LocalDateTime.now(), List.of(new Price(LocalDateTime.now(), 10.0)), Unit.G),
                new Ingredient(2L, "Cheese", LocalDateTime.now(), List.of(new Price(LocalDateTime.now(), 20.0)), Unit.G)
        ));

        // Create the dish in the database
        dishesDao.createDishes(dish);

        // Verify the dish was correctly inserted into the database
        Dish retrievedDish = dishesDao.getDishesById(17);
        assertNotNull(retrievedDish);
        assertEquals(17, retrievedDish.getDishId());
        assertEquals("Burger", retrievedDish.getDishName());
        assertEquals(5000, retrievedDish.getPrice());
    }
}
