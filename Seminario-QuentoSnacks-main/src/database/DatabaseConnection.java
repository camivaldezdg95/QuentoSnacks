package database;

import java.sql.*;

/*
La clase DatabaseConnection realiza la conexión a la base de datos MySQL.
Se encarga de eestablecer la conexión con los parametros de conexión especificados.
 */
public class DatabaseConnection {

    // Constantes para la conexión
    private static final String HOST = "jdbc:mysql://localhost/";
    private static final String USER = "root";
    private static final String PASS = "MC!46d5b0c9";
    private static final String BD = "QuentoSnacks";

    public static Connection conectarBD() {
        Connection conexion;
        System.out.println("Conectando...");

        try {
            // establece conexión.
            conexion = DriverManager.getConnection(HOST + BD, USER, PASS);
            System.out.println("Conexión Exitosa!!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        return conexion;
    }
}
