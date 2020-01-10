package org.ftd.educational.oldschool.dao.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Fabio Tavares Dippold
 * @version 2017-03-07 - 2.0.1
 *
 */
public class ConnectionService {
    private static ConnectionService INSTANCE;
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost/oldschool";
    private static final String USER = "root";
    private static final String PASSWD = "lazaro";

    static {
        ConnectionService.INSTANCE = new ConnectionService();
    }

    public static ConnectionService getInstance() {
        return ConnectionService.INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USER, PASSWD);
        } catch (ClassNotFoundException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public Connection getReadOnlyConnection() throws SQLException {
        Connection conn = this.getConnection();
        conn.setReadOnly(true);

        return conn;
    }

    private ConnectionService() {
    }

}
