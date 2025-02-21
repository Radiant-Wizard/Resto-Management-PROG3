package org.radiant_wizard.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Datasource {
    private final String db_host = System.getenv("HOST");
    private final String db_port = System.getenv("PORT");
    private final String db_name = System.getenv("DB_NAME");
    private final String db_user = System.getenv("USERNAME");
    private final String db_user_password = System.getenv("USER_PASSWORD");
    private final String url;

    public Datasource() {
        this.url = "jdbc:postgresql://"+ db_host + ":" + db_port + "/" + db_name;
    }

    public Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, db_user, db_user_password);
            return connection;
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
