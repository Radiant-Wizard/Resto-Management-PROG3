import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.radiant_wizard.Entity.*;
import org.radiant_wizard.Entity.Enum.LogicalOperator;
import org.radiant_wizard.Entity.Enum.Unit;
import org.radiant_wizard.dao.DishesDaoImpl;
import org.radiant_wizard.db.Criteria;
import org.radiant_wizard.db.Datasource;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DishDaoImplTest {
    Datasource datasource;
    DishesDaoImpl dishesDao;

    @BeforeEach
    void setup() throws SQLException {
        datasource = new Datasource();
        dishesDao = new DishesDaoImpl(datasource);
    }

    @Test
    public void testGetDishes() throws SQLException, IllegalAccessException {
        Dish dish = dishesDao.getDishesById(1);
        assertEquals(5500, dish.getTotalCostIngredient(LocalDateTime.now()));
    }

    @Test
    public void testCreateDishesWithoutHotdog() throws SQLException, IllegalAccessException {
        List<Ingredient> ingredients = List.of(
                new Ingredient(1L, "tomato", LocalDateTime.now(), Unit.G, List.of(), List.of()),
                new Ingredient(2L, "Chicken Breast", LocalDateTime.now(), Unit.G, List.of(), List.of()),
                new Ingredient(3L, "Lettuce", LocalDateTime.now(), Unit.G, List.of(), List.of())
        );
        // Prepare a dish
        Dish dish = new Dish(17L, "Burger", 5000, ingredients);

        // Create the dish in the database
        dishesDao.saveDishes(List.of(dish));

        // Verify the dish was correctly inserted into the database
        Dish retrievedDish = dishesDao.getDishesById(17);
        assertNotNull(retrievedDish);
        assertEquals(17, retrievedDish.getDishId());
        assertEquals("Burger", retrievedDish.getDishName());
        assertEquals(5000, retrievedDish.getPrice());
    }

    @Test
    public void testGetAvailable() throws SQLException, IllegalAccessException {
        LocalDateTime testDate = LocalDateTime.of(2025, 2, 24, 12, 0);

        Dish hotdog = dishesDao.getDishesById(1);

        assertEquals(30, hotdog.getAvailableQuantity(testDate));
        assertEquals(30, hotdog.getAvailableQuantity());
    }
    @Test
    @DisplayName("test if the getDishByCriteria work fine")
    public void testGetByCriteria() throws SQLException {
        List<Criteria> criteriaList = new ArrayList<>();
        criteriaList.add(new Criteria("dish_name", "BURGER", "LIKE", LogicalOperator.AND));
//        criteriaList.add(new Criteria("dish_price", 8000, "=", LogicalOperator.AND));
        List<Dish> dishes = dishesDao.getDishes(criteriaList, "dish_name", true, 2, 1);


        assertEquals(1, dishes.size());
    }
}
