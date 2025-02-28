import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.radiant_wizard.Entity.Ingredient;
import org.radiant_wizard.dao.IngredientDaoImpl;
import org.radiant_wizard.db.Criteria;
import org.radiant_wizard.db.Datasource;

import java.sql.SQLException;
import java.util.List;

public class IngredientDaoImplTest {
    Datasource datasource;
    IngredientDaoImpl ingredientDao;
    @BeforeEach
    void setUp(){
        datasource = new Datasource();
        ingredientDao = new IngredientDaoImpl(datasource);
    }

    @Test
    public void testGetIngredientByCriteria() throws SQLException {
        List<Criteria> criteriaList = List.of(
                new Criteria("17", "ingredient_id"),
                new Criteria("oil", "ingredient_name")
        );

        List<Ingredient> ingredients = ingredientDao.getIngredientByCriteria(criteriaList, null, null, 2,  1);

        Assertions.assertEquals(2, ingredients.size());
    }
}
