package nz.ac.otago.flatty.dao;

import com.aceql.jdbc.driver.free.AceQLDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.h2.jdbcx.JdbcConnectionPool;

public class DbConnection {

    private static final String DEFAULT_URL = "jdbc:mysql://13.239.27.21:3306/FlattyApp"; // Production Database - Amazon AWS
    private static JdbcConnectionPool pool;

    public static Connection getTestConnection(String url, String username, String password) {
        if (pool == null) {
            pool = JdbcConnectionPool.create(url, username, password);
        }

        try {
            return pool.getConnection();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Connection getDefaultConnection(String username, String password) {
        try {
            String server = "http://13.239.27.21:9090/aceql";
            String database = "FlattyApp";

            DriverManager.registerDriver(new AceQLDriver());
            Class.forName(AceQLDriver.class.getName());

            Properties info = new Properties();
            info.put("user", username);
            info.put("password", password);
            info.put("database", database);

            Connection conn = DriverManager.getConnection(server, info);
            return conn;
        } catch (SQLException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
}
