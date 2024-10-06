package dao;

import model.Pedido;
import model.DetallePedido;
import model.DetalleItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



/*
La clase pedidoDAO implementa la interfaz IPedidoDAO
proporciona la implementación de los métodos para realizar el CRUD en la entidad pedido.
recibe una conexión a la base de datos y es responsable de gestionar la persistencia de los pedidos.
 */

@SuppressWarnings("CallToPrintStackTrace")

public class PedidoDAO implements IPedidoDAO {
    private Connection conexion;

    public PedidoDAO(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public void crearPedido(Pedido pedido) {
        String queryPedido = "INSERT INTO Pedido (idCliente, fecha, estado, idDeposito, precioTotalPedido) VALUES (?, ?, ?, ?, ?)";
        String queryDetallePedido = "INSERT INTO DetallePedido (idPedido) VALUES (?)";
        String queryDetalleItem = "INSERT INTO DetalleItem (idDetallePedido, idProducto, cantidad, precioUnitario, precioTotal) VALUES (?, ?, ?, ?, ?)";

        try {
            // Iniciar una transacción
            conexion.setAutoCommit(false);

            // Insertar el pedido
            try (PreparedStatement stmtPedido = conexion.prepareStatement(queryPedido, Statement.RETURN_GENERATED_KEYS)) {
                stmtPedido.setInt(1, pedido.getIdCliente());
                stmtPedido.setDate(2, new java.sql.Date(pedido.getFecha().getTime()));
                stmtPedido.setString(3, pedido.getEstado());
                stmtPedido.setInt(4, pedido.getIdDeposito());
                stmtPedido.setDouble(5, pedido.getPrecioTotalPedido());
                stmtPedido.executeUpdate();

                // Obtener el ID generado para el pedido
                try (ResultSet generatedKeys = stmtPedido.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idPedidoGenerado = generatedKeys.getInt(1);

                        // Insertar el detalle del pedido
                        try (PreparedStatement stmtDetallePedido = conexion.prepareStatement(queryDetallePedido, Statement.RETURN_GENERATED_KEYS)) {
                            stmtDetallePedido.setInt(1, idPedidoGenerado);
                            stmtDetallePedido.executeUpdate();

                            // Obtener el ID generado para el detalle del pedido
                            try (ResultSet generatedDetalleKeys = stmtDetallePedido.getGeneratedKeys()) {
                                if (generatedDetalleKeys.next()) {
                                    int idDetallePedidoGenerado = generatedDetalleKeys.getInt(1);

                                    // Insertar los detalles de los items
                                    try (PreparedStatement stmtDetalleItem = conexion.prepareStatement(queryDetalleItem)) {
                                        for (DetalleItem item : pedido.getDetalle().getDetalleItems()) {
                                            stmtDetalleItem.setInt(1, idDetallePedidoGenerado);
                                            stmtDetalleItem.setInt(2, item.getIdProducto());
                                            stmtDetalleItem.setInt(3, item.getCantidad());
                                            stmtDetalleItem.setDouble(4, item.getPrecioUnitario());
                                            stmtDetalleItem.setDouble(5, item.getPrecioTotal());
                                            stmtDetalleItem.addBatch();
                                        }
                                        stmtDetalleItem.executeBatch();
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Confirmar la transacción
            conexion.commit();
        } catch (SQLException e) {
            try {
                // Revertir la transacción en caso de error
                conexion.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conexion.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public List<Pedido> obtenerTodosLosPedidos() {
        List<Pedido> pedidos = new ArrayList<>();
        String query = "SELECT * FROM Pedido";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int idPedido = rs.getInt("idPedido");
                int idCliente = rs.getInt("idCliente");
                Date fecha = rs.getDate("fecha");
                String estado = rs.getString("estado");
                int idDeposito = rs.getInt("idDeposito");
                double precioTotalPedido = rs.getDouble("precioTotalPedido");
                Pedido pedido = new Pedido(idPedido, idCliente, fecha, estado, idDeposito, precioTotalPedido, null);
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedidos;
    }

    @Override
    public Pedido obtenerPedidoPorId(int idPedido) {
        Pedido pedido = null;
        String queryPedido = "SELECT * FROM Pedido WHERE idPedido = ?";
        String queryDetallePedido = "SELECT * FROM DetallePedido WHERE idPedido = ?";
        String queryDetalleItem = "SELECT * FROM DetalleItem WHERE idDetallePedido = ?";

        try (PreparedStatement stmtPedido = conexion.prepareStatement(queryPedido)) {
            stmtPedido.setInt(1, idPedido);
            try (ResultSet rsPedido = stmtPedido.executeQuery()) {
                if (rsPedido.next()) {
                    int idCliente = rsPedido.getInt("idCliente");
                    Date fecha = rsPedido.getDate("fecha");
                    String estado = rsPedido.getString("estado");
                    int idDeposito = rsPedido.getInt("idDeposito");
                    double precioTotalPedido = rsPedido.getDouble("precioTotalPedido");

                    // Inicializar el pedido sin el detalle
                    pedido = new Pedido(idPedido, idCliente, fecha, estado, idDeposito, precioTotalPedido, null);

                    // Obtener detalle del pedido
                    DetallePedido detallePedido = null;
                    try (PreparedStatement stmtDetallePedido = conexion.prepareStatement(queryDetallePedido)) {
                        stmtDetallePedido.setInt(1, idPedido);
                        try (ResultSet rsDetallePedido = stmtDetallePedido.executeQuery()) {
                            if (rsDetallePedido.next()) {
                                int idDetallePedido = rsDetallePedido.getInt("idDetallePedido");

                                // Obtener items del detalle
                                List<DetalleItem> detalleItems = new ArrayList<>();
                                try (PreparedStatement stmtDetalleItem = conexion.prepareStatement(queryDetalleItem)) {
                                    stmtDetalleItem.setInt(1, idDetallePedido);
                                    try (ResultSet rsDetalleItem = stmtDetalleItem.executeQuery()) {
                                        while (rsDetalleItem.next()) {
                                            int idProducto = rsDetalleItem.getInt("idProducto");
                                            int cantidad = rsDetalleItem.getInt("cantidad");
                                            double precioUnitario = rsDetalleItem.getDouble("precioUnitario");
                                            double precioTotal = rsDetalleItem.getDouble("precioTotal");
                                            detalleItems.add(new DetalleItem(rsDetalleItem.getInt("idDetalleItem"), idDetallePedido, idProducto, cantidad, precioUnitario, precioTotal));
                                        }
                                    }
                                }
                                detallePedido = new DetallePedido(idDetallePedido, idPedido, detalleItems);
                                pedido.setDetalle(detallePedido); // Asegurarse de asignar el detalle al pedido
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedido;
    }

    @Override
    public void actualizarEstadoPedido(int idPedido, String nuevoEstado) {
        String query = "UPDATE Pedido SET estado = ? WHERE idPedido = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, idPedido);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizarProductosPedido(Pedido pedido) {
        String queryEliminarDetalleItems = "DELETE FROM DetalleItem WHERE idDetallePedido = ?";
        String queryInsertarDetalleItem = "INSERT INTO DetalleItem (idDetallePedido, idProducto, cantidad, precioUnitario, precioTotal) VALUES (?, ?, ?, ?, ?)";
        String queryActualizarPrecioTotal = "UPDATE Pedido SET precioTotalPedido = ? WHERE idPedido = ?";

        try {
            // Iniciar una transacción
            conexion.setAutoCommit(false);

            // Eliminar los items actuales del detalle del pedido
            try (PreparedStatement stmtEliminar = conexion.prepareStatement(queryEliminarDetalleItems)) {
                stmtEliminar.setInt(1, pedido.getDetalle().getIdDetallePedido());
                stmtEliminar.executeUpdate();
            }

            // Insertar los nuevos items del detalle del pedido
            try (PreparedStatement stmtInsertar = conexion.prepareStatement(queryInsertarDetalleItem)) {
                for (DetalleItem item : pedido.getDetalle().getDetalleItems()) {
                    stmtInsertar.setInt(1, pedido.getDetalle().getIdDetallePedido());
                    stmtInsertar.setInt(2, item.getIdProducto());
                    stmtInsertar.setInt(3, item.getCantidad());
                    stmtInsertar.setDouble(4, item.getPrecioUnitario());
                    stmtInsertar.setDouble(5, item.getPrecioTotal());
                    stmtInsertar.addBatch();
                }
                stmtInsertar.executeBatch();
            }

            // Actualizar el precio total del pedido
            try (PreparedStatement stmtActualizarPrecioTotal = conexion.prepareStatement(queryActualizarPrecioTotal)) {
                stmtActualizarPrecioTotal.setDouble(1, pedido.getPrecioTotalPedido());
                stmtActualizarPrecioTotal.setInt(2, pedido.getIdPedido());
                stmtActualizarPrecioTotal.executeUpdate();
            }

            // Confirmar la transacción
            conexion.commit();
        } catch (SQLException e) {
            try {
                conexion.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conexion.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void eliminarPedido(int idPedido) {
        String queryEliminarDetalleItems = "DELETE FROM DetalleItem WHERE idDetallePedido IN (SELECT idDetallePedido FROM DetallePedido WHERE idPedido = ?)";
        String queryEliminarDetallePedido = "DELETE FROM DetallePedido WHERE idPedido = ?";
        String queryEliminarPedido = "DELETE FROM Pedido WHERE idPedido = ?";

        try {
            // Iniciar una transacción
            conexion.setAutoCommit(false);

            // Eliminar los items del detalle del pedido
            try (PreparedStatement stmtEliminarDetalleItems = conexion.prepareStatement(queryEliminarDetalleItems)) {
                stmtEliminarDetalleItems.setInt(1, idPedido);
                stmtEliminarDetalleItems.executeUpdate();
            }

            // Eliminar el detalle del pedido
            try (PreparedStatement stmtEliminarDetallePedido = conexion.prepareStatement(queryEliminarDetallePedido)) {
                stmtEliminarDetallePedido.setInt(1, idPedido);
                stmtEliminarDetallePedido.executeUpdate();
            }

            // Eliminar el pedido
            try (PreparedStatement stmtEliminarPedido = conexion.prepareStatement(queryEliminarPedido)) {
                stmtEliminarPedido.setInt(1, idPedido);
                stmtEliminarPedido.executeUpdate();
            }

            // Confirmar la transacción
            conexion.commit();
        } catch (SQLException e) {
            try {
                conexion.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conexion.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}