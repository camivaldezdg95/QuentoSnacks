package dao;
import model.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



/*
La clase ProductoDAO implementa la interfaz IProductoDAO
y proporciona los métodos para realizar operaciones CRUD en la entidad Producto.
Aunque la gestión de productos no es el foco central del proyecto, se incluyó para complementar
las funcionalidades del sistema.
*/
@SuppressWarnings("CallToPrintStackTrace")

public class ProductoDAO implements IProductoDAO {
    private Connection conexion;

    public ProductoDAO (Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public Producto buscarProducto(int idProducto) {
        String query = "SELECT * FROM Producto WHERE idProducto = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setInt(1, idProducto);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Producto(
                        rs.getInt("idProducto"),
                        rs.getString("descripcion"),
                        rs.getDouble("precioUnitario"),
                        rs.getInt("stock")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public double obtenerPrecioUnitario(int idProducto) {
        Producto producto = buscarProducto(idProducto);
        if (producto != null) {
            return producto.getPrecioUnitario();
        }
        return -1;
    }
}

