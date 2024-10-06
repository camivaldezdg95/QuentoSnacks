package dao;
import model.Producto;


/*
La interfaz IProductoDAO define los métodos para realizar operaciones CRUD en la entidad Producto.
Esta interfaz proporciona una abstracción para la gestión de productos en el sistema.
*/

public interface IProductoDAO {
    Producto buscarProducto(int idProducto);
    double obtenerPrecioUnitario(int idProducto);
}
