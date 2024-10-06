package model;

import java.util.List;

/*
Representa el detalle de un pedido
Un detalle de pedido tiene ID, ID del pedido relacionado y una lista con los items.
 */
public class DetallePedido {
    private int idDetallePedido;
    private int idPedido;
    private List<DetalleItem> detalleItems;

    // Constructor completo
    public DetallePedido(int idDetallePedido, int idPedido, List<DetalleItem> detalleItems) {
        this.idDetallePedido = idDetallePedido;
        this.idPedido = idPedido;
        this.detalleItems = detalleItems;
    }

    // Constructor vacío
    public DetallePedido() {}

    // Getters y setters
    public int getIdDetallePedido() {
        return idDetallePedido;
    }

    public void setIdDetallePedido(int idDetallePedido) {
        this.idDetallePedido = idDetallePedido;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public List<DetalleItem> getDetalleItems() {
        return detalleItems;
    }

    public void setDetalleItems(List<DetalleItem> detalleItems) {
        this.detalleItems = detalleItems;
    }
}
