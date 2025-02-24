package org.radiant_wizard;
import org.radiant_wizard.Entity.Dish;
import org.radiant_wizard.dao.DishesDaoImpl;
import org.radiant_wizard.db.Datasource;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException, IllegalAccessException{
        Datasource datasource = new Datasource();
        DishesDaoImpl dishesDao = new DishesDaoImpl(datasource);

        List<Dish> dishList = dishesDao.getDishes(1, 10);
        System.out.println(dishList);
        }
}