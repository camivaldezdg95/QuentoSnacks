package model;

import java.util.Date;

/*
Representa un pedido en el sistema.
Un pedido contiene ID, ID del cliente que solicito el pedido, Fecha , estado, Deposito, precio total del pedido
y su relacion con el detalle.
 */

public class Pedido {
    private int idPedido;
    private int idCliente;
    private Date fecha;
    private String estado;
    private int idDeposito;
    private double precioTotalPedido;
    private DetallePedido detalle;

    // Constructor completo
    public Pedido(int idPedido, int idCliente, Date fecha, String estado, int idDeposito, double precioTotalPedido, DetallePedido detalle) {
        this.idPedido = idPedido;
        this.idCliente = idCliente;
        this.fecha = fecha;
        this.estado = estado;
        this.idDeposito = idDeposito;
        this.precioTotalPedido = precioTotalPedido;
        this.detalle = detalle;
    }

    // Constructor vacío
    public Pedido() {}

    // Getters y setters
    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIdDeposito() {
        return idDeposito;
    }

    public void setIdDeposito(int idDeposito) {
        this.idDeposito = idDeposito;
    }

    public double getPrecioTotalPedido() {
        return precioTotalPedido;
    }

    public void setPrecioTotalPedido(double precioTotalPedido) {
        this.precioTotalPedido = precioTotalPedido;
    }

    public DetallePedido getDetalle() {
        return detalle;
    }

    public void setDetalle(DetallePedido detalle) {
        this.detalle = detalle;
    }
}
