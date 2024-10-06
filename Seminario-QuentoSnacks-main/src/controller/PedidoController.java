package controller;

import dao.IPedidoDAO;
import dao.PedidoDAO;
import model.DetalleItem;
import model.Pedido;

import java.sql.Connection;
import java.util.List;


/**
 * La clase PedidoController maneja la lógica de negocio relacionada con la entidad Pedido.
 * Esta clase interactúa con la capa de datos a través de la interfaz IPedidoDAO para realizar operaciones CRUD.
 * También proporciona métodos adicionales para calcular el total de un pedido y actualizar sus detalles.
 */
public class PedidoController {
    private IPedidoDAO pedidoDAO;

    public PedidoController(Connection conexion) {
        this.pedidoDAO = new PedidoDAO(conexion);
    }

    public void crearPedido(Pedido pedido) {
        pedidoDAO.crearPedido(pedido);
    }
    public double calcularTotal(Pedido pedido) {
        return pedido.getDetalle().getDetalleItems().stream()
                .mapToDouble(DetalleItem::getPrecioTotal)
                .sum();
    }
    public List<Pedido> obtenerTodosLosPedidos() {
        return pedidoDAO.obtenerTodosLosPedidos();
    }
    public Pedido obtenerPedidoPorId(int idPedido) {
        return pedidoDAO.obtenerPedidoPorId(idPedido);
    }
    public void actualizarEstadoPedido(int idPedido, String nuevoEstado) {
        pedidoDAO.actualizarEstadoPedido(idPedido, nuevoEstado);
    }
    public void actualizarProductosPedido(Pedido pedido) {
        pedido.setPrecioTotalPedido(calcularTotal(pedido));
        pedidoDAO.actualizarProductosPedido(pedido);
    }
    public void eliminarPedido(int idPedido) {
        pedidoDAO.eliminarPedido(idPedido);
    }

}
