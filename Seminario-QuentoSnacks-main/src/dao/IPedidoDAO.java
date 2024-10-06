package dao;

import model.Pedido;

import java.util.List;

/*
La interfaz IPedidoDAO define los métodos para realizar operaciones CRUD en la entidad Pedido.
Esta interfaz proporciona una abstracción para la gestión de pedidos en el sistema.
*/

public interface IPedidoDAO {
    void crearPedido(Pedido pedido);
    List<Pedido> obtenerTodosLosPedidos();
    Pedido obtenerPedidoPorId(int idPedido);
    void actualizarEstadoPedido(int idPedido, String nuevoEstado);
    void actualizarProductosPedido(Pedido pedido);
    void eliminarPedido(int idPedido);

}
