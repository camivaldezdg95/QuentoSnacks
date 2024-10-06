package controller;

import dao.IClienteDAO;
import dao.ClienteDAO;
import model.Cliente;

import java.sql.Connection;



/*
La clase ClienteController maneja la lógica de negocio relacionada con la entidad Cliente.
Esta clase interactúa con la capa de datos a través de la interfaz IClienteDAO para realizar operaciones CRUD.
 */

public class ClienteController {
    private IClienteDAO clienteDAO;

    public ClienteController(Connection conexion) {
        this.clienteDAO = new ClienteDAO(conexion);
    }

    public Cliente buscarCliente(int idCliente) {
        return clienteDAO.buscarCliente(idCliente);
    }

    public int crearCliente(Cliente cliente) {
        return clienteDAO.crearCliente(cliente);
    }
}