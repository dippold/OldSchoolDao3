package org.ftd.educational.oldschool.dao.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Fabio Tavares Dippold
 * @version 2017-03-08 - 1.0.0
 *
 */
public class ConnectionFactory {

    public static Connection getConnection(Schema schema) {
        Connection connection;
        try {
            Class.forName(schema.getDriver());
            String url = schema.getUrl();
            String username = schema.getUsuario();
            String password = schema.getSenha();
            connection = DriverManager.getConnection(url, username, password);

            return connection;
        } catch (ClassNotFoundException e) {
            System.out.println("O driver expecificado nao foi encontrado.");

            return null;
        } catch (SQLException e) {
            System.out.println("Nao foi possivel conectar ao banco de dados.");

            return null;
        }
    }

    public static Connection getReadOnlyConnection(Schema schema) {
        try {
            Connection connection = ConnectionFactory.getConnection(schema);
            connection.setReadOnly(true);

            return connection;
        } catch (SQLException e) {
            System.out.println("Nao foi possivel conectar ao banco de dados.");

            return null;
        }
    }

    public ConnectionFactory() {
    }

}
