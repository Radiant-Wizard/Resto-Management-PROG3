import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.radiant_wizard.Entity.Ingredient;
import org.radiant_wizard.Entity.Enum.LogicalOperator;
import org.radiant_wizard.dao.IngredientDaoImpl;
import org.radiant_wizard.db.Criteria;
import org.radiant_wizard.db.Datasource;

import java.sql.SQLException;
import java.util.ArrayList;
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
        List<Criteria> criteriaList = new ArrayList<>();
        criteriaList.add(new Criteria("ingredient_name", "oil", "LIKE", LogicalOperator.AND));
//        criteriaList.add(new Criteria("unit_price", 10, ">", LogicalOperator.AND));

        List<Ingredient> ingredients = ingredientDao.getIngredientByCriteria(criteriaList, null, null, 2,  1);

        Assertions.assertEquals(1, ingredients.size());
    }
}
