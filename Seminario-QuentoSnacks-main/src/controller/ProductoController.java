package controller;

import dao.IProductoDAO;
import dao.ProductoDAO;
import model.Producto;

import java.sql.Connection;



/*
 La clase ProductoController maneja la lógica de negocio relacionada con la entidad Producto.
 Esta clase interactúa con la capa de datos a través de la interfaz IProductoDAO para realizar operaciones CRUD.
 */
public class ProductoController {
    private IProductoDAO productoDAO;

    public ProductoController (Connection conexion) {
        this.productoDAO = new ProductoDAO(conexion);
    }
    public Producto buscarProducto(int idProducto) {
        return productoDAO.buscarProducto(idProducto);
    }

    public double obtenerPrecioUnitario(int idProducto) {
        return productoDAO.obtenerPrecioUnitario(idProducto);
    }
}