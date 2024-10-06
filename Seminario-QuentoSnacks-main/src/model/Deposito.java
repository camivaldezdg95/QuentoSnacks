package model;


/*
Representa un depósito en el sistema.
Un deposito tiene un ID, nombre y ubicación.
Se relaciona con los pedidos para determinar desde que depósito se realizan. y que usuarios.
 */
public class Deposito {
    private int idDeposito;
    private String nombre;
    private String direccion;

    public Deposito(int idDeposito, String nombre, String direccion) {
        this.idDeposito = idDeposito;
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public Deposito() {
    }

    public int getIdDeposito() {
        return idDeposito;
    }

    public void setIdDeposito(int idDeposito) {
        this.idDeposito = idDeposito;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
