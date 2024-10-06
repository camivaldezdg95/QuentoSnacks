package dao;

import model.Cliente;

import java.sql.*;


/*
La clase ClienteDAO implementa la interfaz IClienteDAO
proporciona la implementación de los métodos para realizar operaciones CRUD en la entidad Cliente.
Aunque la gestión de clientes excede el alcance central del aplicativo, se consideró necesario
incluirla para una funcionalidad básica.
 */

@SuppressWarnings("CallToPrintStackTrace")
public class ClienteDAO implements IClienteDAO {
    private Connection conexion;

    public ClienteDAO(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public Cliente buscarCliente(int idCliente) {
        String query = "SELECT * FROM Cliente WHERE idCliente = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setInt(1, idCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                            rs.getInt("idCliente"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("direccion"),
                            rs.getString("telefono")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // inserta un nuevo cliente en la base de datos.
    @Override
    public int crearCliente(Cliente cliente) {
        String query = "INSERT INTO Cliente (nombre, apellido, direccion, telefono) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getApellido());
            stmt.setString(3, cliente.getDireccion());
            stmt.setString(4, cliente.getTelefono());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Devolver el ID generado
                } else {
                    throw new SQLException("No se pudo obtener el ID generado.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Indicar error
        }
    }

}