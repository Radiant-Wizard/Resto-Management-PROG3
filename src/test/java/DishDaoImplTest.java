import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.radiant_wizard.Entity.Dish;
import org.radiant_wizard.dao.DishesDaoImpl;
import org.radiant_wizard.db.Datasource;

import java.sql.SQLException;

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
        Assertions.assertEquals(5500, dish.getTotalCostIngredient());
    }
}
